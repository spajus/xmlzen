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
package com.googlecode.xmlzen;

import java.util.Stack;

/**
 * Fluent XML builder for quick and easy construction.
 * 
 * <p><em>Simple example:</em></p>
 * <pre>
 * //will be &lt;root&gt;dummy&lt;/root&gt;
 * String xml = XmlBuilder.newXml()
 *     .openTag("root").withValue("dummy")
 *     .toString(true);
 * </pre>
 * 
 * <p><em>Complex example:</em></p>
 * <pre>
 * String xml = XmlBuilder.newXml("utf-8")
 * 		.openTag("root")
 *      .withAttribute("id", 1).withAttribute("one", "two")
 *      	.openTag("inner1")
 *      		.openTag("inner11").withAttribute("x", "y") 
 *      			.withValue("somthing inside")
 *      		.closeTag()	
 *      		.openTag("empty").closeTag()
 *      		.openTag("attr-only").withAttribute("attr", 123)
 *      		.closeTag()
 *      	.closeTag()
 *      	.openTag("inner2")
 *      		.withValue("another boring value")
 *      	.closeAllTags()
 *      .toString();
 * </pre>
 * 
 * <p><em>Complex example output (formatted):</em></p>
 * <pre>
 * &lt;?xml version="1.0" encoding="utf-8"?&gt;
 * &lt;root id="1" one="two"&gt;
 *   &lt;inner1&gt;
 *     &lt;inner11 x="y"&gt;somthing inside&lt;/inner11&gt;
 *     &lt;empty/&gt;
 *     &lt;attr-only attr="123"/&gt;
 *   &lt;/inner1&gt;
 *   &lt;inner2&gt;another boring value&lt;/inner2&gt;
 * &lt;/root&gt;
 * </pre>
 * 
 * <p>Thread safety is guaranteed if each thread creates {@link #newXml()} and 
 * does not share the created instance with anybody.</p>
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public class XmlBuilder {

	/**
	 * The body buffer of this XML
	 * 
	 * @see #XmlBuilder()
	 */
    private final XmlBuilderOutput body;
    
    /**
     * Stack of open XML tags that will have to be closed at some point
     * 
     * @see #openTag(String)
     * @see #closeTag()
     * @see #closeAllTags()
     */
    private final Stack<String> tagStack = new Stack<String>();
    
    /**
     * Is XML formatted by default?
     * 
     * @see #setDefaultFormatting(boolean)
     */
    private static boolean defaultFormatting = true;
    
    /**
     * Default XML encoding.
     * 
     * @see #setDefaultXmlEncoding(String)
     */
    private static String defaultXmlEncoding = "UTF-8";
    
    
    /**
     * Flag that denotes that currently open tag is not yet terminated with 
     * "&gt;", so you can add attributes. XmlBuilder will terminate the tag 
     * when it will be necessary.
     * 
     * @see #withAttribute(String, Object)
     * @see #withAttributeIf(boolean, String, Object)
     */
    private boolean tagIsPendingTermination = false;
    
    /**
     * Should XML be formed beautifully? If not, it will be in a single line.
     */
    private boolean beautiful;
    
    /**
     * Encoding of current XML document
     */
    private String encoding;
    
    /**
     * Current identation level for beautification
     */
    private int ident = 0;
    
    /**
     * Beautification (new line) has just happened
     */
    private boolean beautified = false;
    
    /**
     * Tab character (two spaces)
     */
    private static final String TAB = "  ";
    
    /**
     * By default, XmlBuilder produces formatted XML. You can switch the default
     * behavior for new instances with this setter.
     * 
     * @param defaultFormatting Is XML being formatted by default?
     */
    public static void setDefaultFormatting(final boolean defaultFormatting) {
        XmlBuilder.defaultFormatting = defaultFormatting;
    }
    
    /**
     * By default, XmlBuilder produces XML with UTF-8 encoding in the 
     * declaration. You can change the encoding here.
     * 
     * @param defaultXmlEncoding Default XML Encoding charset name
     */
    public static void setDefaultXmlEncoding(final String defaultXmlEncoding) {
        XmlBuilder.defaultXmlEncoding = defaultXmlEncoding;
    }
    
    /**
     * Private constructor. Use static factory methods instead.
     * 
     * @see #newXml()
     */
    private XmlBuilder() {
        this(new XmlBuilderStringOutput());
    }

    /**
     * Private constructor. Use static factory methods instead.
     *
     * @param output XmlBuilderOutput implementation
     * @see #newXml()
     */
    private XmlBuilder(final XmlBuilderOutput output) {
        this.beautified = defaultFormatting;
        this.body = output;
    }
    
    /**
     * Static factory method for starting new XML construction process.
     * You should consider adding declaration.
     * 
     * @see #newXml(String)
     * @see #withDeclaration(String)
     * @see #setDefaultFormatting(boolean)
     * @see #setDefaultXmlEncoding(String)
     * @return new instance of XmlBuilder
     */
    public static XmlBuilder newXml() {
        return newXml(defaultXmlEncoding);
    }
    
    /**
     * Static factory method for starting new XML construction process.
     * Forms a declaration with encoding provided in <code>charset</code> 
     * parameter. 
     * 
     * <p>List of available encodings can be found here:<br/>
     * <a href="http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html">
     * http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html</a> 
     * 
     * <p>You should now consider opening a <em>root tag</em>.</p>
     * 
     * @param charset name of XML charset encoding. 
     * @return new instance of XmlBuilder with declaration
     * @see #openTag(String)
     * @see #setDefaultXmlEncoding(String) 
     */
    public static XmlBuilder newXml(final String charset) {
    	final XmlBuilder builder = new XmlBuilder()
    		.withDeclaration("<?xml version=\"1.0\" encoding=\"" 
    				.concat(charset).concat("\"?>"));
    	builder.encoding = charset;
    	return builder;
    }

    /**
     * Static factory method for starting new XML construction process.
     * Allows choosing if XML is formatted or not.
     * You should consider adding declaration.
     * 
     * @param beautiful Should XML be built with formatting?
     * @return new instance of XmlBuilder
     */
    public static XmlBuilder newXml(final boolean beautiful) {
        final XmlBuilder builder = new XmlBuilder();
        builder.beautiful = beautiful;
        return builder;
    }
    
    /**
     * Static factory method for starting new XML construction process.
     * Forms a declaration with encoding provided in <code>charset</code> 
     * parameter. 
     * 
     * <p>List of available encodings can be found here:<br/>
     * <a href="http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html">
     * http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html</a> 
     * 
     * <p>You should now consider opening a <em>root tag</em>.</p>
     * 
     * @param charset name of XML charset encoding. 
     * @param beautiful Should XML be formatted?
     * @return new instance of XmlBuilder with declaration
     * @see #openTag(String)
     */
    public static XmlBuilder newXml(final String charset, 
            final boolean beautiful) {
        final XmlBuilder builder = new XmlBuilder();
        builder.beautiful = beautiful;
        return builder.withDeclaration("<?xml version=\"1.0\" encoding=\"" 
                    .concat(charset).concat("\"?>"));
    }


    /**
     * Static factory method for starting new XML construction process.
     * Output is written to OutputStream instead of memory. Convenient with
     * @param output target OutputStream.
     * @param charset name of XML charset encoding.
     * @param beautiful should XML be formatted?
     * @return new instance of XmlBuilder with declaration
     * @see #openTag(String)
     */
    public static XmlBuilder newXml(final XmlBuilderOutput output,
            final String charset, final boolean beautiful) {
        final XmlBuilder builder = new XmlBuilder(output);
        builder.beautiful = beautiful;
        builder.encoding = charset;
        return builder;
    }
    
    /**
     * Adds a declaration to you XML. 
     * 
     * <p>It can be something like 
     * "&lt;?xml version="1.0" encoding="utf-8"? &gt;"</p>
     * 
     * <p>You can also include a doctype along with your declaration, i.e.:<br/>
     * <pre>
     * &lt;?xml version="1.0" encoding="utf-8" ?&gt;
     * &lt;!DOCTYPE chapter
     *   PUBLIC "-//OASIS//DTD DocBook XML//EN" "../dtds/docbookx.dtd"&gt;
     * </pre>
     * </p>
     * 
     * @param declaration XML declaration. May include DOCTYPE definition.
     * 
     * @return self
     */
    public XmlBuilder withDeclaration(final String declaration) {
    	if (body.length() > 0) {
    		throw new XmlZenException("Cannot add declaration. Some " +
    				"content was added already: " + body);
    	}
    	body.append(declaration);
    	return this;
    }
    
    /**
     * Opens a new XML tag. After this you can add attributes and set a value.
     * 
     * @see #withAttribute(String, Object)
     * @see #withAttributeIf(boolean, String, Object)
     * @see #withValue(Object)
     * @param name Tag name
     * @return self
     */
    public XmlBuilder openTag(final String name) {
        checkBody();
        tagStack.push(name);
        ident++;
        if (body.length() > 0) {
            beautifyNewLine();
        }
        body.append('<').append(name);
        tagIsPendingTermination = true;
        beautified = false;
        return this;
    }

    /**
     * Adds an attribute to an open tag. Can't call it if there is no open tag 
     * or if the tag value was set. 
     * 
     * <p>After adding attributes you can set tag value, close the tag or
     * open a nested tag</p>
     * 
     * @see #openTag(String)
     * @see #closeTag()
     * @see #withValue(Object)
     * @see #withAttributeIf(boolean, String, Object)
     * @param name Attribute name
     * @param value Attribute value
     * @return self
     */
    public XmlBuilder withAttribute(final String name, final Object value) {
        if (!tagIsPendingTermination) {
            throw new XmlZenException("Can't add attribute now, " +
                    "call openTag() first!");
        }
        body.append(' ').append(name).append("=\"")
            .append(value).append('"');
        return this;
    }
    
    /**
     * Adds an attribute to an open tag <b>IF</b> the condition is 
     * <code>true</code>. 
     * 
     * @see #withValue(Object)
     * @param condition Boolean condition for this attribute to appear
     * @param name Attribute name
     * @param value Attribute value
     * @return self
     */    
    public XmlBuilder withAttributeIf(final boolean condition, 
            final String name, final Object value) {
        if (condition) {
            return withAttribute(name, value);
        } else {
            return this;
        }
    }
    
    /**
     * Sets tag value. You have to open a tag fist.
     * 
     * @see #openTag(String)
     * @param value Value of the currently open tag
     * @return self
     */
    public XmlBuilder withValue(final Object value) {
        if (!tagIsPendingTermination) {
            throw new XmlZenException("Call openTag before setting value!");
        }
        checkBody();
        body.append(value);
        return this;
    }
    
    /**
     * Sets tag CDATA value. You have to open a tag fist.
     * 
     * @see #openTag(String)
     * @param value CDATA Value of the currently open tag
     * @return self
     */    
    public XmlBuilder withCDATA(final Object value) {
        if (!tagIsPendingTermination) {
            throw new XmlZenException("Call openTag before setting CDATA!");
        }
        checkBody();
        body.append("<![CDATA[");
        body.append(value);
        body.append("]]>");
        return this;
    }
    
    /**
     * Checks for unterminated open tags and terminates them.
     * 
     * @see #tagIsPendingTermination
     * @see #openTag(String)
     */
    private void checkBody() {
        if (tagIsPendingTermination) {
            body.append('>');
            tagIsPendingTermination = false;
        }
    }
    
    /**
     * Creates beautiful new line if it's needed
     */
    private void beautifyNewLine() {
        if (!beautiful) {
            return;
        }
        if (ident > 0) {
            if (beautified) {
                body.append(TAB);
            } else {
                body.append('\n');
                for (int i = 0; i < ident - 1; i++) {
                    body.append(TAB);
                }
            }
        } else {
            System.out.println("ident zero!" + ident);
        }
    }
    
    /**
     * Closes the tag. Calls to this method can be skipped unless you are going
     * to open new tags ahead.
     * 
     * @see #closeAllTags()
     * @return self
     */
    public XmlBuilder closeTag() {
        if (tagStack.isEmpty()) {
            throw new XmlZenException("All tags are closed already!");
        }
        if (tagIsPendingTermination) {
            tagStack.pop();
            body.append("/>");
            tagIsPendingTermination = false;
        } else {
            body.append("</").append(tagStack.pop()).append('>');
        }
        beautified = false;
        ident--; 
        beautifyNewLine();
        beautified = true;
        return this;
    }
    
    /**
     * Closes all open tags if there are any left. You should rarely call this 
     * method, it's automatically invoked before getting XML string.
     * 
     * @see #toString()
     * @return self
     */
    public XmlBuilder closeAllTags() {
        while (!tagStack.isEmpty()) {
            closeTag();
        }
        return this;
    }
    
    /**
     * Returns current XML Encoding
     * 
     * @return Charset name of current XML Encoding
     */
    public String getEncoding() {
        return encoding;
    }
    
    /**
     * Gets the String representation of the XML that is currently being built.
     * 
     * <p>Shows partial result. Can be called anytime.</p>
     * 
     * @see #toString(boolean)
     * @see #closeAllTags() 
     * @return XML String which is the result of current building process
     */
    @Override
    public String toString() {
        return new String(body.toString());
    }
    
    /**
     * Gets the String representation of the XML that is currently being built.
     * 
     * <p>If autocomplete is set to true, this method calls 
     * {@link #closeAllTags()}, so you shouldn't do so in the middle of 
     * construction</p>
     * 
     * @see #closeAllTags()
     * @param autocomplete Should all open tags be closed before printing?
     * @return XML String which is the result of current building process 
     */
    public String toString(boolean autocomplete) {
        if (autocomplete) {
            closeAllTags();
        }
        return new String(body.toString());
    }
    
}
