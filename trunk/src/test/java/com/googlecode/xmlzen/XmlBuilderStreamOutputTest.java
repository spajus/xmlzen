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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.io.*;
import java.nio.CharBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link com.googlecode.xmlzen.XmlBuilderStreamOutput}
 *
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public class XmlBuilderStreamOutputTest {

    private static final Log log = LogFactory.getLog(XmlBuilderStreamOutputTest.class);

    @Test
    public void testBuild() throws Exception {
        File myXml = File.createTempFile("xmlzentest", ".xml");
        OutputStream myXmlStream = new FileOutputStream(myXml);

        String xml = XmlBuilder.newXml(
                new XmlBuilderStreamOutput(myXmlStream, "UTF-8"), "UTF-8", false)
            .openTag("xml")
                .withAttribute("id", 1)
                .openTag("thisishow")
                    .withValue("you can build")
                .closeTag()
                .openTag("your").withAttribute("xml", "nicely")
                .withAttributeIf(1 < 0, "shouldnot", "happen")
            .toString(true);
        log.debug(xml);
        myXmlStream.close();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(myXml), "UTF-8");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int c = 0;
        while ((c = reader.read()) != -1) {
            bout.write(c);
        }
        reader.close();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                     "<xml id=\"1\"><thisishow>you can build</thisishow>" +
        	     "<your xml=\"nicely\"/></xml>", bout.toString("UTF-8"));
    }

}