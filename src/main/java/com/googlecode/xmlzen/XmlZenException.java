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

/**
 * A unchecked Exception that is thrown when XmlZen get's an error. 
 * <p>
 * If you're a fan of checked exceptions, you can try / catch this exception 
 * anywhere in the code:</p>
 * <pre>
 * try {
 *     XmlSlicer.cut(someXml).get('something');
 * } catch (XmlZenException e) {
 *     log.error("Failed cutting XML", e);
 * }
 * </pre>
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public class XmlZenException extends RuntimeException {

	private static final long serialVersionUID = 2955472974450170367L;

	/**
	 * {@inheritDoc}
	 */
	public XmlZenException() {}

	/**
	 * {@inheritDoc}
	 */
	public XmlZenException(String message) {
		super(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public XmlZenException(Throwable cause) {
		super(cause);
	}

	/**
	 * {@inheritDoc}
	 */
	public XmlZenException(String message, Throwable cause) {
		super(message, cause);
	}
}
