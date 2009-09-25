/*
 * Copyright 2009 Tomas Varaneckas 
 * http://www.varaneckas.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.xmlzen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.googlecode.xmlzen.XmlZenException;

/**
 * XmlUtils - FIXME add documentation 
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
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
	
	public static String getTagValue(String xml, String tag) {
	    return getTagValue(xml, tag, null, null, true);
	}
	
    public static String getTagValue(String xml, String tag, 
            Value<Integer> startOffset, 
            Value<Integer> endOffset, boolean valueOnly) {
        String tagStart = "<".concat(tag);
        String tagEnd = "</".concat(tag).concat(">");
        int start = xml.indexOf(tagStart, getValue(startOffset, 0));
        if (start == -1) {
            setValue(endOffset, -1);
            return null;
        }
        char next = xml.charAt(start + tagStart.length());
        if (next == '/') {
            //offset of />
            setValue(startOffset, start);
            setValue(endOffset, start + tagStart.length() + 1);
            return null;
        }
        while (next != '>' && next != ' ') {
            start = xml.indexOf(tagStart, start + tagStart.length());
            next = xml.charAt(start + tagStart.length());
        }
        //FIXME ended here
        int fullStart = start;
        start = xml.indexOf('>', start) + 1;
        int end = xml.indexOf(tagEnd, start);
        if (end == -1) {
            setValue(startOffset, -1);
            setValue(endOffset, -1);
            return null;
        }
        String result = xml.substring(start, end).trim();
        int newStart = result.indexOf(tagStart); 
        while (newStart != -1) {
            end = xml.indexOf(tagEnd, end + tagEnd.length());
            result = xml.substring(start, end).trim();
            newStart = result.indexOf(tagStart, newStart+tagStart.length());
        } 
        setValue(startOffset, start - tagStart.length() - 1);
        setValue(endOffset, end + tagEnd.length());
        return result;
    }
    
    private static int getValue(Value<Integer> source, int ifNull) {
        if (source == null) {
            return ifNull;
        } else {
            return source.getValue();
        }
    }
    
    private static void setValue(Value<Integer> target, Integer value) {
        if (target != null) {
            target.setValue(value);
        }
    }
    
    public static String getAttribute(String inputXml, String tag, String attribute) {
        String tagStart = "<".concat(tag).concat(" ");
        int start = inputXml.indexOf(tagStart) + tagStart.length();
        int end = inputXml.indexOf('>', start);
        String attributes = inputXml.substring(start, end);
        start = (" ".concat(attributes)).indexOf(" ".concat(attribute).concat("="));
        if (start == -1) {
            return null;
        }
        start += attribute.length() + 1;
        char quote = attributes.charAt(start);
        start++;
        end = attributes.indexOf(quote, start);
        return attributes.substring(start, end);
    }
    
    public static long getLongAttribute(String inputXml, String tag, String attribute) {
        return Long.parseLong(getAttribute(inputXml, tag, attribute));
    }

    public static int getIntAttribute(String inputXml, String tag, String attribute) {
        return Integer.parseInt(getAttribute(inputXml, tag, attribute));
    }
}
