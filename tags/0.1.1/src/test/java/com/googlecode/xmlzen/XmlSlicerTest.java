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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.googlecode.xmlzen.utils.FileUtils;

/**
 * Unit tests for {@link XmlSlicer} and {@link XmlSlicerList}
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public class XmlSlicerTest {

    private static final int SLICER_ITERATIONS = 1000;
    
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
    public void testXmlSlicerListConcurrency() throws Exception {
        final CountDownLatch latch = new CountDownLatch(SLICER_ITERATIONS);
        final XmlSlicerList xsl = new XmlSlicerList();
        xsl.add(XmlSlicer.cut("1"));
        xsl.add(XmlSlicer.cut("2"));
        xsl.add(XmlSlicer.cut("3"));
        for (int i = 0; i < SLICER_ITERATIONS; i++) {
            new Thread(new Runnable() {
                public void run() {
                    assertEquals(Arrays.asList(new String[] {"1", "2", "3"}), 
                            xsl.asList());
                    latch.countDown();
                }
            }).start();
        }
        latch.await();
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
    
    @Test
    public void testValue() throws Exception {
        String val = XmlSlicer.cut("<xml>123</xml>").value();
        assertEquals("123", val);
    }

}
