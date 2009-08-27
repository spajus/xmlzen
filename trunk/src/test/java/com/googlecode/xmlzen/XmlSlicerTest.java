package com.googlecode.xmlzen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.googlecode.xmlzen.utils.FileUtils;

public class XmlSlicerTest {

    @Test
    public void testCut() throws Exception {
        String xml = FileUtils.readFile(
                FileUtils.getClassPathFile("xmls/simple.xml"));
        String six = XmlSlicer.cut(xml)
            .get("tagB")
                .get("innerTag1")
                    .get("tag3").toString();
        assertEquals("6", six);
    }
    
}
