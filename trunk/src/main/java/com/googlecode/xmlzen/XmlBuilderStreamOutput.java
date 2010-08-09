package com.googlecode.xmlzen;

import java.io.IOException;
import java.io.OutputStream;

public class XmlBuilderStreamOutput implements XmlBuilderOutput {

    private final OutputStream target;

    private final String charset;

    private long length = 0L;

    public XmlBuilderStreamOutput(final OutputStream target,
            final String charset) {
        this.target = target;
        this.charset = charset;
    }

    public long length() {
        return length;
    }

    public XmlBuilderOutput append(final Object data) {
        String d = (data instanceof String) ? (String) data : String.valueOf(data);
        length += d.length();
        try {
            target.write(d.getBytes(charset));
            return this;
        } catch (final IOException e) {
            throw new XmlZenException(
                    "XmlBuilderStreamOutput.charset is not supported", e);
        }
    }

}
