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

import java.util.ArrayList;
import java.util.List;

import com.googlecode.xmlzen.utils.Value;
import com.googlecode.xmlzen.utils.XmlUtils;

public class XmlSlicer {

    private final String xml;
    
    private XmlSlicer(String xml) {
        this.xml = xml;
    }
    
    public static XmlSlicer cut(final String xmlString) {
        return new XmlSlicer(xmlString);
    }
    
    public XmlSlicer get(final String tag) {
        return new XmlSlicer(XmlUtils.getTagValue(xml, tag));
    }

    public List<XmlSlicer> getAll(final String tag) {
        final Value<Integer> startOffset = new Value<Integer>(0);
        final Value<Integer> lastOffset = new Value<Integer>(0);
        final List<XmlSlicer> results = new ArrayList<XmlSlicer>();
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
    
    public String getAttribute(String tag, String attribute) {
        return XmlUtils.getAttribute(xml, tag, attribute);
    }
    
    @Override
    public String toString() {
        return xml;
    }
    
}
