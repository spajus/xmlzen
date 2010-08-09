package com.googlecode.xmlzen;

public class XmlBuilderStringOutput implements XmlBuilderOutput {

    private final StringBuilder target;

    public XmlBuilderStringOutput() {
        target = new StringBuilder();
    }

    public XmlBuilderOutput append(final Object data) {
        target.append(data);
        return this;
    }

    public long length() {
        return target.length();
    }

}
