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
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * Unit tests for {@link XmlBuilder}
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public class XmlBuilderTest {

    private static final Log log = LogFactory.getLog(XmlBuilderTest.class);

    @Test
    public void testBuild() throws Exception {
        String xml = XmlBuilder.newXml("UTF-8")
            .openTag("xml")
                .withAttribute("id", 1)
                .openTag("thisishow")
                    .withValue("you can build")
                .closeTag()
                .openTag("your").withAttribute("xml", "nicely")
                .withAttributeIf(1 < 0, "shouldnot", "happen")
            .toString(true);
        log.debug(xml);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                     "<xml id=\"1\"><thisishow>you can build</thisishow>" +
        	     "<your xml=\"nicely\"/></xml>", xml);
    }
    
    @Test
    public void testWrongBuild() throws Exception {
        try {
            XmlBuilder.newXml("UTF-8").withDeclaration("");
            fail("Should not allow adding declaration again");
        } catch (final XmlZenException e) {
            //expected
        }
        try {
            XmlBuilder.newXml().openTag("tag").withDeclaration("");
            fail("Should not allow adding declaration here");
        } catch (final XmlZenException e) {
            //expected
        }
        try {
            XmlBuilder.newXml().withAttribute("a", "b");
            fail("Should not allow adding attribute here");
        } catch (final XmlZenException e) {
            //expected
        }
        try {
            XmlBuilder.newXml().withValue("test");
            fail("Should not allow adding value here");
        } catch (final XmlZenException e) {
            //expected
        }
        try {
            XmlBuilder.newXml().closeTag();
            fail("Should not allow closing tag here");
        } catch (final XmlZenException e) {
            //expected
        }
    }
    
    @Test
	public void testComplexBuild() throws Exception {
        String xml = XmlBuilder.newXml("utf-8")
  		    .openTag("root").withAttribute("id", 1)
  		        .withAttributeIf(true, "one", "two")
  		        .openTag("inner1")
  		        .openTag("inner11").withAttribute("x", "y") 
       			    .withValue("somthing inside")
       			.closeTag()	
       			.openTag("empty").closeTag()
       			.openTag("attr-only").withAttribute("attr", 123)
       			.closeTag()
       		.closeTag()
       		.openTag("inner2")
       		    .withValue("another boring value")
       		.closeAllTags()
       	.toString();
        assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
        		"<root id=\"1\" one=\"two\"><inner1>" +
        		"<inner11 x=\"y\">somthing inside</inner11><empty/>" +
        		"<attr-only attr=\"123\"/></inner1>" +
        		"<inner2>another boring value</inner2></root>", xml);
        log.debug(xml);
	}
    
    @Test
    public void testBeautifulBuild() throws Exception {
        String xml = XmlBuilder.newXml("UTF-8", true)
        .openTag("test")
            .openTag("inner").withAttribute("x", "y").closeTag()
            .openTag("deeper")
                .openTag("core").withValue("hard").closeTag()
                .openTag("check")
                    .openTag("levels")
                        .openTag("of")
                            .openTag("deepness").withAttribute("good", "1")
                                .withValue("so deep")
                            .closeTag()
                        .closeTag()
                    .closeTag()
                .openTag("checker").withValue("unexpected").closeTag()
        .toString(true);
        log.debug(xml);
    }
    
    @Test
    public void testBeautifulBuild2() throws Exception {
        String xml = XmlBuilder.newXml("UTF-8", true)
        .openTag("xml").withAttribute("id", 1)
            .openTag("thisishow")
                .withValue("you can build")
            .closeTag()
            .openTag("your").withAttribute("xml", "nicely").toString(true);
        log.debug(xml);
    }
    
    @Test
    public void testBeautifulClose() throws Exception {
        String xml = XmlBuilder.newXml(true).openTag("x").openTag("a")
        .withAttribute("b", "c").closeTag().closeTag().toString();
        log.debug(xml);
    }
    
    @Test
    public void testCDATA() throws Exception {
        String xml = XmlBuilder.newXml(true).openTag("data")
                .withCDATA("<xml>cdata</xml>").toString(true);
        log.debug(xml);
        assertEquals("<data><![CDATA[<xml>cdata</xml>]]></data>", xml);
    }

    @Test
    public void testDefaults() throws Exception {
        String xml = XmlBuilder.newXml().toString();
        log.debug(xml);
        assertEquals("", xml);
        XmlBuilder.setDefaultFormatting(true);
        XmlBuilder.setDefaultXmlEncoding("Windows-1257");
        assertEquals("Windows-1257", XmlBuilder.newXml().getEncoding());
        xml = XmlBuilder.newXml().openTag("a")
                .openTag("b").withValue("c").closeAllTags().toString();
        log.debug(xml);
        assertTrue(xml.contains("\n"));
    }
    
}
