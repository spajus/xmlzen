package com.googlecode.xmlzen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        String formAction = XmlSlicer.cut(xml)
                .getTagAttribute("form", "action");
        log.debug("Form Action: " + formAction);
        assertEquals("http://query.nytimes.com/gst/sitesearch_selector.html",
                formAction);
        String imgSrc = XmlSlicer.cut(xml).getTags("div").get(1).getTag("img")
                .attribute("src");
        log.debug(imgSrc);
        assertTrue(imgSrc
                .startsWith("http://wt.o.nytimes.com/dcsym57yw10000s1s8g0boozt_9t1x/njs.gif"));
    }

}
