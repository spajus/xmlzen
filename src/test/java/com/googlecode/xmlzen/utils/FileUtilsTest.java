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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * Unit tests for {@link FileUtils}
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public class FileUtilsTest {
	
	private static final Log log = LogFactory.getLog(FileUtils.class);
	
	@Test
	public void testGetClassPathFile() throws Exception {
		File f = FileUtils.getClassPathFile("xmls/note.xml");
		assertTrue(f.exists());
		f = FileUtils.getClassPathFile("voidvoid");
		assertTrue(f == null);
	}
	
	@Test
	public void testReadFile() throws Exception {
		File noteFile = FileUtils.getClassPathFile("xmls/note.xml");
		String note = FileUtils.readFile(noteFile);
		assertNotNull(note);
		assertTrue(note.length() > 0);
		log.debug("note.xml: " + note);
	}
	
	@Test
	public void testClose() throws Exception {
		FileUtils.close(null);
		FileInputStream in = new FileInputStream(
				FileUtils.getClassPathFile("xmls/note.xml"));
		in.read();
		FileUtils.close(in);
		try {
			in.read();
			fail("Stream should be closed: " + in);
		} catch (final IOException e) {
			//expected
		}
		FileUtils.close(in);
 	}
}
