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

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link ArrayList} of {@link XmlSlicer} objects
 * 
 * It acts just like a normal {@link List}, just with additional methods
 * for data conversion. XmlSlicerList is returned from XmlSlicer when 
 * slicing ends up with multiple results (i.e. similar tags).
 * 
 * Example use:
 * <pre>
 * List&lt;String&gt; tags = XmlSlicer.cut(someBigXml).getAll('sometag').asList();
 * //Equivalent to following:
 * XmlSlicerList tagList = XmlSlicer.cut(someBigXml).getAll('sometag');
 * List&lt;String&gt; tags = tagList.asList();
 * </pre>
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public class XmlSlicerList extends ArrayList<XmlSlicer> {

    private static final long serialVersionUID = 4963346323773728803L;

    /**
     * Retuns a List of String values of underlying XmlSlicer objects
     * 
     * @return List of Strings with tag values
     */
    public List<String> asList() {
        synchronized (this) {
            final List<String> result = new ArrayList<String>();
            for (XmlSlicer slice : this) {
                result.add(slice.toString());
            }
            return result;
        }
    }
}
