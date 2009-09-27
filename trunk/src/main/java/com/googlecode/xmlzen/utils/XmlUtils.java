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
 * Utilities for working with XML Strings. 
 * <p>
 * This class should not be used directly, instead, use XmlSlicer.</p>
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public abstract class XmlUtils {

	/**
	 * Tries to read the <?xml ... encoding="???"?> header. 
	 * <p>
	 * Possible outcomes are listed here: 
	 * http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html</p>
	 * <p>
	 * In case header is not found, Charset.defaultCharset().name() is returned
	 * </p>
	 * 
	 * @param file XML File
	 * @return Possible charset name
	 */
	public static String guessCharset(final File file) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			//<?xml version="1.0" encoding="Windows-1257"?> ~50 bytes
			//100 bytes should be more than enough to read such a header
			final byte[] head = new byte[100]; 
			in.read(head);
			return guessCharset(new String(head));
		} catch (final Exception e) {
			throw new XmlZenException("Failed guessing charset for file: " 
					+ file, e);
		} finally {
			FileUtils.close(in);
		}
	}

	/**
	 * Tries to read the <?xml ... encoding="???"?> header. 
     * <p>
     * Possible outcomes are listed here: 
     * http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html</p>
     * <p>
     * In case header is not found, Charset.defaultCharset().name() is returned
     * </p>
     * 
	 * @param xml XML String
	 * @return Possible charset name
	 */
	public static String guessCharset(final String xml) {
		final int offEnc = xml.indexOf("encoding");
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
			final int offQuoteEnd = xml.indexOf(xml.charAt(offQuote++), 
			        offQuote + 1);
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
	
	/**
	 * Gets the value of XML tag.
	 * <p>
	 * For example, if tag is &lt;a&gt;b&lt;a&gt;, the returned value would 
	 * be "b".</p>
	 * 
	 * @see #getTagValue(String, String, Value, Value, boolean)
	 * @param xml XML String
	 * @param tag Target tag
	 * @return Value of tag's contents
	 */
	public static String getTagValue(final String xml, final String tag) {
	    return getTagValue(xml, tag, null, null, true);
	}
	
    /**
     * Gets the value of XML tag.
     * <p>
     * For example, if tag is &lt;a&gt;b&lt;a&gt;, the returned value would 
     * be "b".</p>
     * If valueOnly is set to false, whole tag is returned, not just content.
     * </p>
     * <p>
     * Start and end offsets can be given to narrow the action to a concrete 
     * region of the XML String. Also, the provided Value objects are filled 
     * with exact start and end offset of the XML tag which value is returned. 
     * </p>
     * 
     * @param xml XML String
     * @param tag Target tag
	 * @param startOffset Two-way {@link Value} with start offset
	 * @param endOffset Two-way {@link Value} with end offset
	 * @param valueOnly Return only the xml tag value, without tag itself
	 * @return Value of tag's contents or whole tag with contents
	 */
	public static String getTagValue(final String xml, final String tag, 
            final Value<Integer> startOffset, 
            final Value<Integer> endOffset, 
            final boolean valueOnly) {
	    //A string that represents tag start, i.e.: "<someTag".
	    //This string does not have any closing ">", because tag can have 
	    //attributes or it can be autoclosed with "/>".
        final String tagStart = "<".concat(tag);
        //If tag has value it usually ends nicely with "</someTag>"
        final String tagEnd = "</".concat(tag).concat(">");
        int start = xml.indexOf(tagStart, getValue(startOffset, 0));
        if (start == -1) {
            setValue(endOffset, -1);
            return null;
        }
        char next = xml.charAt(start + tagStart.length());
        //tag is immediately closed
        if (next == '/') {
            //offset of />
            setValue(startOffset, start);
            setValue(endOffset, start + tagStart.length() + 2);
            if (valueOnly) {
                return null;
            } else {
                return xml.substring(start, start + tagStart.length() + 2);
            }
        }
        while (next != '>' && next != ' ') {
            start = xml.indexOf(tagStart, start + tagStart.length());
            next = xml.charAt(start + tagStart.length());
        }
        final int fullStart = start;
        start = xml.indexOf('>', start) + 1;
        //check if tag has no value
        if (xml.charAt(start - 2) == '/') {
            setValue(startOffset, fullStart);
            setValue(endOffset, start);
            if (valueOnly) {
                return null;
            } else {
                return xml.substring(fullStart, start);
            }
        }
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
        if (!valueOnly) {
            result = xml.substring(fullStart, end + tagEnd.length());
        }
        return result;
    }
    
	/**
	 * Gets int value from {@link Value} object. If the object is null, you can
	 * provide the default value.
	 * 
	 * @param source Source Value object
	 * @param ifNull Default value that is returned if source is null
	 * @return Source int value or default value if source is null
	 */
    private static int getValue(final Value<Integer> source, final int ifNull) {
        if (source == null) {
            return ifNull;
        } else {
            return source.getValue();
        }
    }
    
    /**
     * Sets value on {@link Value} object if it's not null
     * 
     * @param target Value object or null
     * @param value Value to be set
     */
    private static void setValue(final Value<Integer> target, 
                final Integer value) {
        if (target != null) {
            target.setValue(value);
        }
    }
    
    /**
     * Gets the value of a tag attribute
     * 
     * @param inputXml Source XML to look the tag for
     * @param tag Tag name
     * @param attribute Attribute name
     * @return Value of the attribute
     */
    public static String getAttribute(final String inputXml, final String tag, 
            final String attribute) {
        final String tagStart = "<".concat(tag).concat(" ");
        final int start = inputXml.indexOf(tagStart) + tagStart.length();
        final int end = inputXml.indexOf('>', start);
        return getAttributeInRange(inputXml, attribute, start, end);
    }

    /**
     * Looks for attribute value in XML String within a given range 
     * 
     * @param inputXml Source XML String
     * @param attribute Attribute name
     * @param start Range start
     * @param end Range end
     * @return Value of the attribute
     */
    private static String getAttributeInRange(final String inputXml,
            final String attribute, int start, int end) {
        final String attributes = inputXml.substring(start, end);
        start = (" ".concat(attributes)).indexOf(
                " ".concat(attribute).concat("="));
        if (start == -1) {
            return null;
        }
        start += attribute.length() + 1;
        final char quote = attributes.charAt(start);
        start++;
        end = attributes.indexOf(quote, start);
        return attributes.substring(start, end);
    }
    
    /**
     * Return gets the attribute value of the first tag in given XML String
     * 
     * @param inputXml Source XML
     * @param attribute Attribute name
     * @return Value of the attribute
     */
    public static String getFirstTagAttribute(final String inputXml, 
            final String attribute) {
        final String tagStart = "<";
        int start = inputXml.indexOf(tagStart);
        if (inputXml.charAt(start + 1) == '?') {
            start = inputXml.indexOf(tagStart, start + 2);
        }
        final int end = inputXml.indexOf('>', start);
        return getAttributeInRange(inputXml, attribute, start, end);
    }
    
    /**
     * Gets the long value of an attribute in given XML
     * 
     * @see #getAttribute(String, String, String)
     * @param inputXml Source XML
     * @param tag Tag name
     * @param attribute Attribute name
     * @return Value of the attribute parsed as long
     */
    public static long getLongAttribute(final String inputXml, final String tag, 
            final String attribute) {
        return Long.parseLong(getAttribute(inputXml, tag, attribute));
    }

    /**
     * Gets the int value of an attribute in given XML
     * 
     * @see #getAttribute(String, String, String)
     * @param inputXml Source XML
     * @param tag Tag name
     * @param attribute Attribute name
     * @return Value of the attribute parsed as int
     */
    public static int getIntAttribute(final String inputXml, final String tag, 
            final String attribute) {
        return Integer.parseInt(getAttribute(inputXml, tag, attribute));
    }
}
