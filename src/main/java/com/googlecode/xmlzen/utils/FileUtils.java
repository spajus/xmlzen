package com.googlecode.xmlzen.utils;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.xmlzen.XmlZenException;

public abstract class FileUtils {

	private static final Log log = LogFactory.getLog(FileUtils.class);

	private static final int BUFFER = 2048;

	public static String readFile(final File file, final String charset) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			final BufferedInputStream buffIn = new BufferedInputStream(in);
			final byte[] buffer = new byte[(int) Math
					.min(file.length(), BUFFER)];
			int read;
			final StringBuilder result = new StringBuilder();
			while ((read = buffIn.read(buffer)) != -1) {
				result.append(new String(buffer, 0, read, charset));
			}
			return result.toString();
		} catch (final Exception e) {
			throw new XmlZenException("Failed reading file: " + file, e);
		} finally {
			close(in);
		}
	}

	public static String readFile(final File file) {
		return readFile(file, Charset.defaultCharset().name());
	}
	
	public static void close(final Closeable closeable) {
		if (closeable == null)
			return;
		try {
			closeable.close();
		} catch (final IOException e) {
			log.debug("Failed closing " + closeable + ". Ignoring exception.");
		}
	}

	public static File getClassPathFile(final String path,
			final ClassLoader classLoader) {
		final URL url = classLoader.getResource(path);
		if (url == null) {
			return null;
		}
		return new File(url.getFile());
	}

	public static File getClassPathFile(final String path) {
		return getClassPathFile(path, ClassLoader.getSystemClassLoader());
	}

}
