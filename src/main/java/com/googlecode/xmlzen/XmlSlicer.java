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
 * <p>#1 - get the value of a tag:</p>
 * <pre>
 * String xml = "&lt;some&gt;stuff&lt;/some&gt;";
 * //value of stuff will be 'stuff'
 * String stuff = XmlSlicer.cut(xml).get("some");
 * </pre>
 * 
 * TODO - add more examples
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
     * Gets the contents of an XML tag. Example:
     * 
     * <pre>
     * //val will be 'xml'
     * String val = XmlSlicer.cut("&lt;an&gt;xml&lt;/an&gt;").getValue("an");
     * </pre>
     * 
     * Warning. You lose any attributes that <code>tag</code> was holding.
     * Use {@link #getAttribute(String, String)} before {@link #get(String)} if 
     * you need them.
     * 
     * @param tag target tag name
     * @return contents that are between <tag> and </tag>
     */
    public XmlSlicer get(final String tag) {
        return new XmlSlicer(XmlUtils.getTagValue(xml, tag));
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
     * @param tag xml tag name
     * @return XmlSlicerList - a List of XmlSlicer objects
     */
    public XmlSlicerList getAll(final String tag) {
        final Value<Integer> startOffset = new Value<Integer>(0);
        final Value<Integer> lastOffset = new Value<Integer>(0);
        final XmlSlicerList results = new XmlSlicerList();
        while (lastOffset.getValue() != -1) {
            String chunk = XmlUtils.getTagValue(xml, tag, 
                    startOffset, lastOffset);
            System.out.println("Start: " + startOffset);
            System.out.println("End: " + lastOffset);
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
     *     .getAttribute("xml", "attr");
     * </pre>
     * 
     * @param tag tag name
     * @param attribute attribute name
     * @return tag attribute's value 
     */
    public String getAttribute(final String tag, final String attribute) {
        return XmlUtils.getAttribute(xml, tag, attribute);
    }
    
    @Override
    public String toString() {
        return xml;
    }
    
}
