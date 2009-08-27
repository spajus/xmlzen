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
    
}
