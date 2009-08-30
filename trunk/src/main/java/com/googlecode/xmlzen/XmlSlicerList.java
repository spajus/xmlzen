package com.googlecode.xmlzen;

import java.util.ArrayList;
import java.util.List;

public class XmlSlicerList extends ArrayList<XmlSlicer> {

    private static final long serialVersionUID = 4963346323773728803L;

    public List<String> asList() {
        synchronized (this) {
            final List<String> result = new ArrayList<String>();
            for (XmlSlicer slice : this) {
                result.add(slice.toString());
            }
            return result;
        }
    }
    
    
}
