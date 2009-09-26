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
        for (XmlSlicer bird : XmlSlicer.cut(xml).getAll("bird")) {
            log.debug(bird);
        }
    }
    
    @Test
    public void testComplexHtmlProcessing() throws Exception {
        String xml = FileUtils.readFile(
                FileUtils.getClassPathFile("xmls/complex.html"));
        String formAction = XmlSlicer.cut(xml).getTagAttribute("form", "action");
        log.debug("Form Action: " + formAction);
        List<String> divs = XmlSlicer.cut(xml).getAll("div").asList();
        log.debug(divs.size());
        log.debug(divs.get(1));
    }
    
}
