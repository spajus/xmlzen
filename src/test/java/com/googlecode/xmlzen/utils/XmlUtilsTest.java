package com.googlecode.xmlzen.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class XmlUtilsTest {

	private static final Log log = LogFactory.getLog(XmlUtilsTest.class);
	
	@Test
	public void testGuessCharset() throws Exception {
		String charset = XmlUtils.guessCharset(
				FileUtils.getClassPathFile("xmls/note.xml"));
		log.debug(charset);
		assertEquals("ISO-8859-1", charset);
		charset = XmlUtils.guessCharset(FileUtils.getClassPathFile("xmls/broken.xml"));
		log.debug(charset);
		assertEquals(Charset.defaultCharset().name(), charset);
		try {
			XmlUtils.guessCharset(new File("dummy"));
			fail("File 'dummy' should not exist");
		} catch (final Exception e) {
			//expected
		}
		charset = XmlUtils.guessCharset("<?xml encoding='bla'?><dummy/>");
		log.debug(charset);
	}
	
}
