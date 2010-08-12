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

/**
 * XmlBuilderOutput implementation that uses StringBuilder to write XML data.
 * It keeps the data in memory, so it's not good with big XMLs.
 *
 * This implementation is used by default. You can get the results of building
 * by calling toString().
 *
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public class XmlBuilderStringOutput implements XmlBuilderOutput {

    /**
     * Target StringBuilder that contains the data.
     */
    private final StringBuilder target = new StringBuilder();

    public XmlBuilderOutput append(final Object data) {
        target.append(data);
        return this;
    }

    public long length() {
        return target.length();
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
