package com.googlecode.xmlzen;

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
    
    public String getAttribute(String tag, String attribute) {
        return XmlUtils.getAttribute(xml, tag, attribute);
    }
    

    
    @Override
    public String toString() {
        return xml;
    }
    
}
