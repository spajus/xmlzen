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
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.googlecode.xmlzen.XmlZenException;

/**
 * Unit tests for {@link FileUtils}
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 */
public class FileUtilsTest {
	
	private static final Log log = LogFactory.getLog(FileUtils.class);
	
	@Test
    public void testBadFile() throws Exception {
        String n = FileUtils.readFile(new File("/dev/null"));
        assertTrue(n == null);
        File f = FileUtils.getClassPathFile("nonexistent");
        assertTrue(f == null);
        try {
            n = FileUtils.readFile(FileUtils.getClassPathFile("xmls/note.xml"), 
                    "FAKE-8");
            fail("Fake charset passed through");
        } catch (final XmlZenException e) {
            //expected
        }
    }
	
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
		try {
		    FileUtils.close(new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
                @Override
                public void close() throws IOException {
                    throw new IOException("Fake close exception");
                }
		    });
		} catch (final RuntimeException e) {
		    fail("Should be caught somewhere else");
		}
 	}
}
