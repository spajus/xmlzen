package com.googlecode.xmlzen;

import static org.junit.Assert.assertEquals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class XmlBuilderTest {

    private static final Log log = LogFactory.getLog(XmlBuilderTest.class);

    @Test
    public void testBuild() throws Exception {
        String xml = XmlBuilder.newXml()
            .openTag("xml")
                .withAttribute("id", 1)
                .openTag("thisishow")
                    .withValue("you can build")
                .closeTag()
                .openTag("your").withAttribute("xml", "nicely")
            .toString();
        log.debug(xml);
        assertEquals("<xml id=\"1\"><thisishow>you can build</thisishow>" +
        		"<your xml=\"nicely\"/></xml>", xml);
    }
    
    @Test
	public void testComplexBuild() throws Exception {
        String xml = XmlBuilder.newXml("utf-8")
  		    .openTag("root").withAttribute("id", 1).withAttribute("one", "two")
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
    
}
