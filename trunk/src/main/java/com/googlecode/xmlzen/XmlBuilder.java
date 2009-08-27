package com.googlecode.xmlzen;

import java.util.Stack;

public class XmlBuilder {

    private StringBuilder body;
    
    private Stack<String> tagStack = new Stack<String>();
    
    private boolean tagIsPendingTermination = false;
    
    private XmlBuilder() {
    }
    
    public static XmlBuilder newXml() {
        return new XmlBuilder().withNewBody();
    }
    
    public String getXml() {
        return body.toString();
    }

    private void appendAttribute(StringBuilder builder, String name, Object value) {
        builder.append(' ').append(name).append("=\"")
                .append(value).append('"');
    }
    
    public XmlBuilder withNewBody() {
        this.body = new StringBuilder();
        if (tagStack.size() > 0) {
            tagStack.clear();
        }
        return this;
    }
    
    public XmlBuilder openTag(String name) {
        checkBody();
        tagStack.push(name);
        body.append('<').append(name);
        tagIsPendingTermination = true;
        return this;
    }
    
    public XmlBuilder withAttribute(String name, Object value) {
        if (!tagIsPendingTermination) {
            throw new XmlZenException("Can't add attribute now, " +
                    "call openTag() first!");
        }
        appendAttribute(body, name, value);
        return this;
    }
    
    public XmlBuilder withAttributeIf(boolean condition, String name, Object value) {
        if (condition) {
            return withAttribute(name, value);
        } else {
            return this;
        }
    }
    
    private void checkBody() {
        if (tagIsPendingTermination) {
            body.append('>');
            tagIsPendingTermination = false;
        }
    }
    
    public XmlBuilder closeTag() {
        if (tagStack.isEmpty()) {
            throw new XmlZenException("All tags are closed already!");
        }
        if (tagIsPendingTermination) {
            tagStack.pop();
            body.append("/>");
            tagIsPendingTermination = false;
        } else {
            body.append("</").append(tagStack.pop()).append('>');
        }
        return this;
    }
    
    public XmlBuilder closeAllTags() {
        while (!tagStack.isEmpty()) {
            closeTag();
        }
        return this;
    }
    
    public XmlBuilder withValue(Object value) {
        if (!tagIsPendingTermination) {
            throw new XmlZenException("Call openTag before setting value!");
        }
        checkBody();
        body.append(value);
        return this;
    }
    
    @Override
    public String toString() {
        return new String(body.toString());
    }
    
}
