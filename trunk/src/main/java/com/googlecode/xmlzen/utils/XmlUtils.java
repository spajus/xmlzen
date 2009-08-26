package com.googlecode.xmlzen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public abstract class XmlUtils {

	/**
	 * Tries to read the <?xml ... encoding="???"?> header. 
	 * 
	 * Possible outcomes are listed here: 
	 * http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html 
	 * 
	 * In case header is not found, Charset.defaultCharset().name() is returned
	 * 
	 * @param file
	 * @return
	 */
	public static String guessCharset(final File file) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			//<?xml version="1.0" encoding="Windows-1257"?> ~50 bytes
			//100 bytes should be more than enough to read such a header
			byte[] head = new byte[100]; 
			in.read(head);
			return guessCharset(new String(head));
		} catch (final Exception e) {
			throw new XmlZenException("Failed guessing charset for file: " 
					+ file, e);
		} finally {
			FileUtils.close(in);
		}
	}

	public static String guessCharset(final String xml) {
		int offEnc = xml.indexOf("encoding");
		if (offEnc > 0) {
			//find the quote
			int offQuote = xml.indexOf('"', offEnc + 8);
			if (offQuote < 1) {
				offQuote = xml.indexOf('\'', offEnc + 8);
			}
			if (offQuote < 1) {
				return Charset.defaultCharset().name();
			}
			//find the ending quote offset and increase start offset
			int offQuoteEnd = xml.indexOf(xml.charAt(offQuote++), offQuote + 1);
			if (offQuoteEnd < 1) {
				//sounds like an incomplete header
				return Charset.defaultCharset().name();
			}
			//we'we located a header
			return xml.substring(offQuote, offQuoteEnd);
		} else {
			return Charset.defaultCharset().name();
		}
	}
}
