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
package com.googlecode.xmlzen.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * Unit tests for {@link XmlUtils}
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public class XmlUtilsTest {

    private static final Log log = LogFactory.getLog(XmlUtilsTest.class);

    private final String xml =
            "<data type=\"raw\" id=\"1\">" +
            "<tree1>" +
            "<forest>" +
            "   <another>" +
            "       <forest one='134'><forest>" +
            "           <tree ok='true'>oak</tree>" +
            "       </forest></forest>" +
            "           </another>" +
            "           </forest>" +
            "           </tree1>" +
            "       </data>";

    @Test
    public void testGetTagValue() throws Exception {
        Value<Integer> start = new Value<Integer>(0);
        Value<Integer> offset = new Value<Integer>();
        String b = XmlUtils.getTagValue("<a>b</a>", "a", start, offset, true);
        log.debug("Start: " + start);
        log.debug("End: " + offset);
        assertEquals("b", b);
        String c = XmlUtils.getTagValue("<a b=\"z\">\nc\n</a>\n", "a");
        assertEquals("c", c);
        String tricky = XmlUtils.getTagValue("<a a=\"a\">a</a>", "a");
        assertEquals("a", tricky);
        String none = XmlUtils.getTagValue("<dummy/>", "dummy");
        assertSame(null, none);
        
        String forest = XmlUtils.getTagValue(xml, "forest", null, null, false);
        log.debug(forest);
    }

    @Test
    public void getClosedTagTest() {
        String tag = XmlUtils.getTagValue("<some><tag/></some>", "tag", 
                null, null, false);
        log.debug(tag);
        assertEquals("<tag/>", tag);
        tag = XmlUtils.getTagValue("<some><tag attr=\"omg\"/></some>",
                "tag", null, null, false);
        log.debug(tag);
        assertEquals("<tag attr=\"omg\"/>", tag);
        tag = XmlUtils.getTagValue("<some><tag attr=\"omg\"/></some>", "tag");
        assertEquals(null, tag);
    }
    
    @Test
    public void ultimateTest() throws Exception {
        String first = XmlUtils.getTagValue(xml, "forest");
        assertTrue(first.replaceAll("\\s+", "").trim().endsWith("another>"));
        String oak = XmlUtils.getTagValue(first, "tree");
        assertEquals("oak", oak);
        int one = XmlUtils.getIntAttribute(first, "forest", "one");
        assertEquals(134, one);
    }

    @Test
    public void testSpeed() throws Exception {
        long start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            XmlUtils.getIntAttribute(
                    XmlUtils.getTagValue(xml, "forest"), "forest", "one");
        }
        long end = System.nanoTime();
        log.debug("Million mixed requests took: " + (end - start)
                / 1000000000.0 + " sec ");
    }

    @Test
    public void testTagValueSpeed() {
        long start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            XmlUtils.getTagValue(xml, "tree");
        }
        long end = System.nanoTime();
        log.debug("Million tag value requests took: " + (end - start)
                / 1000000000.0 + " sec ");
    }

    @Test
    public void testAttributeSpeed() {
        long start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            XmlUtils.getAttribute(xml, "tree", "ok");
        }
        long end = System.nanoTime();
        log.debug("Million attribute requests took: " + (end - start)
                / 1000000000.0 + " sec ");
    }

    @Test
    public void testMultiple() {
        String body = "<data type=\"echo\" id=\"123\">give me echo!</data>";
        String id = XmlUtils.getAttribute(body, "data", "id");
        String type = XmlUtils.getAttribute(body, "data", "type");
        String responseId = XmlUtils.getAttribute(body, "data", "responseId");
        String content = XmlUtils.getTagValue(body, "data");
        log.debug(type);
        log.debug(id);
        log.debug(responseId);
        log.debug(content);
        assertEquals("echo", type);
        assertEquals("123", id);
        assertEquals(null, responseId);
        assertEquals("give me echo!", content);
    }

    public void testAttributes() {
        String data = "<data id=\"123\" responseTo=\"431\" type=\"sometype\">" +
        		"someshit</data>\n\n";
        String res = XmlUtils.getAttribute(data, "data", "responseTo");
        assertEquals("431", res);
    }

    @Test
    public void testGuessCharset() throws Exception {
        String charset = XmlUtils.guessCharset(
                FileUtils.getClassPathFile("xmls/note.xml"));
        log.debug(charset);
        assertEquals("ISO-8859-1", charset);
        charset = XmlUtils.guessCharset(FileUtils
                .getClassPathFile("xmls/broken.xml"));
        log.debug(charset);
        assertEquals(Charset.defaultCharset().name(), charset);
        try {
            XmlUtils.guessCharset(new File("dummy"));
            fail("File 'dummy' should not exist");
        } catch (final Exception e) {
            // expected
        }
        charset = XmlUtils.guessCharset("<?xml encoding='bla'?><dummy/>");
        log.debug(charset);
    }

}
