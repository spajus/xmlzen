package com.googlecode.xmlzen;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.googlecode.xmlzen.utils.FileUtils;

public class XmlSlicerTest {
    
    private static final Log log = LogFactory.getLog(XmlSlicerTest.class);

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
    
    @Test
    public void testGetAll() throws Exception {
        String xml = FileUtils.readFile(
                FileUtils.getClassPathFile("xmls/birds.xml"));
        List<String> birds = XmlSlicer.cut(xml).getAll("bird").asList();
        log.debug(xml);
        log.debug(birds);
    }
    
}
