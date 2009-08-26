package com.googlecode.xmlzen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public abstract class XmlUtils {

	public static String guessCharset(final File file) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			//<?xml version="1.0" encoding="Windows-1257"?> ~50 bytes
			//100 bytes should be more than enough to read such a header
			byte[] head = new byte[100]; 
			in.read(head);
			final String header = new String(head);
			int offEnc = header.indexOf("encoding");
			if (offEnc > 0) {
				//find the quote
				int offQuote = header.indexOf('"', offEnc + 8);
				if (offQuote < 1) {
					offQuote = header.indexOf('\'', offEnc + 8);
				}
				if (offQuote < 1) {
					return Charset.defaultCharset().name();
				}
				//find the ending quote offset and increase start offset
				int offQuoteEnd = header.indexOf(header.charAt(offQuote++), offQuote + 1);
				if (offQuoteEnd < 1) {
					//sounds like an incomplete header
					return Charset.defaultCharset().name();
				}
				//we'we located a header
				return header.substring(offQuote, offQuoteEnd);
			} else {
				return Charset.defaultCharset().name();
			}
		} catch (final Exception e) {
			throw new XmlZenException("Failed guessing charset for file: " 
					+ file, e);
		} finally {
			FileUtils.close(in);
		}
	}
}
