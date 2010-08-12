/*
 * Copyright 2009-2010 Tomas Varaneckas
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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * XmlBuilderOutput implementation that uses OutputStream to write XML data.
 *
 * <p>When using XmlBuilderStreamOutput you should take care of preparing and
 * closing the OutputStream, this implementation does not close anything.</p>
 *
 * <p><em>Example use:</em></p>
 * <pre>
 *   OutputStream myXml = new FileOutputStream(myXmlFile);
 *   XmlBuilder xml = XmlBuilder.newXml(
 *       new XmlBuilderStreamOutput(myXml, "UTF-8"),
 *       "UTF-8",
 *       true);
 *   //build the XML...
 *   myXml.close();
 * </pre>
 *
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public class XmlBuilderStreamOutput implements XmlBuilderOutput {

    /**
     * Target OutputStream. Stream must be closed manually after use!
     */
    private final OutputStream target;

    /**
     * Charset for writing the stream.
     */
    private final String charset;

    /**
     * Length of XML that is currently being built.
     */
    private long length = 0L;

    /**
     * Constructor that takes an OutputStream and a charset name for writing
     * the stream correctly. If charset name is null, default charset is used.
     *
     * @param target Target output stream. Close it manually after building
     *               your XML.
     * @param charset Charset for writing data to target stream correctly.
     */
    public XmlBuilderStreamOutput(final OutputStream target,
            final String charset) {
        this.target = target;
        this.charset = charset == null
                ? Charset.defaultCharset().name() : charset;
    }

    public long length() {
        return length;
    }

    public XmlBuilderOutput append(final Object data) {
        final String d = (data instanceof String)
                ? (String) data : String.valueOf(data);
        length += d.length();
        try {
            target.write(d.getBytes(charset));
            return this;
        } catch (final IOException e) {
            throw new XmlZenException(
                    "XmlBuilderStreamOutput.charset is not supported", e);
        }
    }

    @Override
    public String toString() {
        return "XmlBuilderStreamOutput(".concat(target.toString())
                .concat(")[").concat(String.valueOf(length)).concat("]");
    }
}
