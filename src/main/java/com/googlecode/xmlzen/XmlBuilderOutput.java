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
 * This interface defines the target of XmlBuilder output which is gathered
 * during the building and then used in some ways (displayed, saved to file,
 * etc). As far as XmlBuilder is concerned, it needs to know length of current
 * output (which equals to character count) and a way to append a String or
 * char.
 *
 * Implementations of this interface should not care about thread safety.
 *
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public interface XmlBuilderOutput {

    /**
     * Gives length of contents in characters
     *
     * @return content length
     */
    public long length();

    /**
     * Appends an object (String or char) to the XML that is currently being
     * built. Unexpected data types will be converted to String representation.
     *
     * @param data Data to be appended to XML that is currently being built.
     * @return self (for call chaining)
     */
    public XmlBuilderOutput append(Object data);

}
