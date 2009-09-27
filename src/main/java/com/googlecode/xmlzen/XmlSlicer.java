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

import java.util.List;

import com.googlecode.xmlzen.utils.Value;
import com.googlecode.xmlzen.utils.XmlUtils;

/**
 * A Slicer for cutting XML into pieces. Some examples:
 * 
 * <li><em>Getting the value of a tag:</em></li>
 * <pre>
 * String xml = "&lt;some&gt;stuff&lt;/some&gt;";
 * //value of stuff will be 'stuff'
 * String stuff = XmlSlicer.cut(xml).get("some");
 * </pre>
 * 
 * <li><em>Get the value of an attribute:</em></li>
 * <pre>
 * String xml = "&lt;some id="1"&gt;stuff&lt;/some&gt;";
 * //value of id will be '1'
 * String id = XmlSlicer.cut(xml).getTagAttribute("some", "id");
 * </pre>
 * 
 * <li><em>Get a list of values from tags with same name:</em></li>
 * <pre>
 * String xml = 
 * "&lt;birds&gt;" +
 *   "&lt;bird&gt;pidgeon&lt;/bird&gt;" +
 *   "&lt;bird&gt;crow&lt;/bird&gt;" +
 * "&lt;/birds&gt;
 * //would print: "Bird: pidgeon" and "Bird: crow" 
 * for (String bird : XmlSlicer.cut(xml).getAll(bird).asList()) {
 *     System.out.println("Bird: " + bird);
 * }
 * </pre>
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public class XmlSlicer {

    /**
     * A piece of XML that is currently being sliced
     */
    private final String xml;
    
    /**
     * A private constructor that builds the XmlSlicer. Use 
     * {@link #cut(String)} for instantiating XmlSlicer.
     * 
     * @see #cut(String)
     * @param xml
     */
    private XmlSlicer(final String xml) {
        this.xml = xml;
    }
    
    /**
     * Factory method that requires an XML in form of a String.
     * You can make a String from file by using FileUtils.readFile()
     * 
     * @param xml
     */
    public static XmlSlicer cut(final String xmlString) {
        return new XmlSlicer(xmlString);
    }
    
    /**
     * Gets the contents of an XML tag.
     * <p>
     * Example:</p>
     * <pre>
     * //val will be 'xml'
     * String val = XmlSlicer.cut("&lt;an&gt;xml&lt;/an&gt;").getValue("an");
     * </pre>
     * <p>
     * Warning. You lose any attributes that <code>tag</code> was holding.
     * Use {@link #getTagAttribute(String, String)} before {@link #get(String)} if 
     * you need them.</p>
     * 
     * @param tag Target tag name
     * @return Contents that are between &lt;tag&gt; and &lt;/tag&gt;
     */
    public XmlSlicer get(final String tag) {
        return new XmlSlicer(XmlUtils.getTagValue(xml, tag));
    }

    /**
     * Gets the full XML tag.
     * <p>
     * Example:</p>
     * <pre>
     * //val will be '&lt;an&gt;xml&lt/an&gt;'
     * String val = XmlSlicer.cut("&lt;exampleOf&gt;&lt;an&gt;xml&lt;/an&gt;" +
     *     "&lt;/exampleOf&gt;").getValue("an");
     * </pre>
     * 
     * @param tag Target tag name
     * @return Contents of the tag, including &lt;tag&gt; and &lt;/tag&gt;
     */
    public XmlSlicer getTag(final String tag) {
        return new XmlSlicer(XmlUtils.getTagValue(xml, tag, null, null, false));
    }
    
    /**
     * Gets an XmlSlicerList (which is a {@link List}&lt;XmlSlicer&gt) that 
     * contains values of tags that share the same <code>tag</code> name.
     * <p>Example use:</p>
     * 
     * <pre>
     * String xml = 
     * "&lt;birds&gt;" +
     *   "&lt;bird&gt;pidgeon&lt;/bird&gt;" +
     *   "&lt;bird&gt;crow&lt;/bird&gt;" +
     * "&lt;/birds&gt;
     * //would print: "Bird: pidgeon" and "Bird: crow" 
     * for (String bird : XmlSlicer.cut(xml).getAll(bird).asList()) {
     *     System.out.println("Bird: " + bird);
     * }
     * </pre>
     * 
     * @see XmlSlicerList
     * @param tag Target xml tag name
     * @return XmlSlicerList - a List of XmlSlicer objects
     */
    public XmlSlicerList getAll(final String tag) {
        return getTagValues(tag, true);
    }
    
    /**
     * Gets an XmlSlicerList (which is a {@link List}&lt;XmlSlicer&gt) that 
     * contains tags that share the same <code>tag</code> name.
     * <p>Example use:</p>
     * 
     * <pre>
     * String xml = 
     * "&lt;birds&gt;" +
     *   "&lt;bird&gt;pidgeon&lt;/bird&gt;" +
     *   "&lt;bird&gt;crow&lt;/bird&gt;" +
     * "&lt;/birds&gt;
     * //would print: "Bird: &lt;bird&gt;pidgeon&lt;/bird&gt;" 
     * //and "Bird: &lt;bird&gt;crow&lt;/bird&gt;" 
     * for (String bird : XmlSlicer.cut(xml).getAll(bird).asList()) {
     *     System.out.println("Bird: " + bird);
     * }
     * </pre>
     * 
     * @see XmlSlicerList
     * @param tag Target xml tag name
     * @return XmlSlicerList - a List of XmlSlicer objects
     */    
    public XmlSlicerList getTags(final String tag) {
        return getTagValues(tag, false);
    }

    /**
     * Used internally for getting a list of tags or tag values
     * 
     * @param tag Target tag name
     * @param valuesOnly
     * @return List of Tag values or Tags
     */
    private XmlSlicerList getTagValues(final String tag, boolean valuesOnly) {
        final Value<Integer> startOffset = new Value<Integer>(0);
        final Value<Integer> lastOffset = new Value<Integer>(0);
        final XmlSlicerList results = new XmlSlicerList();
        while (lastOffset.getValue() != -1) {
            String chunk = XmlUtils.getTagValue(xml, tag, 
                    startOffset, lastOffset, valuesOnly);
            startOffset.setValue(lastOffset.getValue());
            if (lastOffset.getValue() != -1) {
                results.add(new XmlSlicer(chunk));
            }
        }
        return results;
    }
    
    /**
     * Gets the attribute value from an XML tag. Example:
     * 
     * <pre>
     * //value of attr will be 'val'
     * String attr = XmlSlicer.cut("&lt;xml attr="val"/&gt;")\
     *     .getTagAttribute("xml", "attr");
     * </pre>
     * 
     * @param tag tag name
     * @param attribute attribute name
     * @return tag attribute's value 
     */
    public String getTagAttribute(final String tag, final String attribute) {
        return XmlUtils.getAttribute(xml, tag, attribute);
    }
    
    /**
     * Gets the attribute value from the first XML tag. Example:
     * 
     * <pre>
     * //value of attr will be 'val'
     * String attr = XmlSlicer.cut("&lt;xml attr="val"/&gt;")
     *     .getTag("xml").attribute("attr");
     * </pre>
     * 
     * @param attribute Attribute name
     * @return First tag attribute's value 
     */
    public String attribute(final String attribute) {
        return XmlUtils.getFirstTagAttribute(xml, attribute);
    }
    
    @Override
    public String toString() {
        return xml;
    }
    
}
