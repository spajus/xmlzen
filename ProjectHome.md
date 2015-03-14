# Why? #

In a world full of bloat it's hard to move forward. Step aside and look for other ways to deal with XML. Forget W3C, and terrible APIs.

Open your mind.

XML is just a string. You don't need several megabytes of libraries for that. Just cut through and get what you are looking for.

XML Zen is here to help.

If you use Java and share same beliefs, write an email to tomas.varaneckas@gmail.com and join the development.

Read the [Motivation](Motivation.md) or [this blog post](http://paranoid-engineering.blogspot.com/2009/09/xml-processing-in-java.html) for more details.

# Use with Maven #

First add dev.java.net Maven repository to your pom.xml or settings.xml:
```
  <repositories>
    <repository>
      <id>maven2-repository.dev.java.net</id>
      <name>Java.net Repository for Maven</name>
      <url>http://download.java.net/maven/2</url>
    </repository>
    <!-- other repositories -->
  </repositories>
```
Then add the dependency to your pom.xml:
```
<dependency>
  <groupId>com.googlecode.xmlzen</groupId>
  <artifactId>xmlzen</artifactId>
  <version>0.2</version>
</dependency>
```


# Maven Site #
For reports, API docs and more, please check [XML Zen Maven Site](http://www.varaneckas.com/xmlzen/).

# How? #


## XmlBuilder Example ##
### Code ###
```
String xml = XmlBuilder.newXml("UTF-8", true)
    .openTag("xml").withAttribute("id", 1)
        .openTag("thisishow")
            .withValue("you can build")
        .closeTag()
        .openTag("your").withAttribute("xml", "nicely").toString(true);
```
### XML ###
```
<?xml version="1.0" encoding="UTF-8"?>
<xml id="1">
  <thisishow>you can build</thisishow>
  <your xml="nicely"/>
</xml>
```

## XmlSlicer Example ##
### XML ###
```
<xml>
  <tagA>
    <innerTag1>
      <tag1>1</tag1>
      <tag2>2</tag2>
      <tag3>3</tag3>
    </innerTag1>
  </tagA>
  <tagB>
    <innerTag1>
      <tag1>4</tag1>
      <tag2>5</tag2>
      <tag3>6</tag3>
    </innerTag1>
    <innerTag2>
      <tag1>7</tag1>
      <tag2>8</tag2>
      <tag3>9</tag3>
    </innerTag2>
  </tagB>
</xml>
```
### Code ###
```
//gets "6"
String value = XmlSlicer.cut(xmlString).get("tagB").get("innerTag1").get("tag3").toString();
```
### Explanation ###

The following code creates a new instance of XmlSlicer for input String (xmlString).
```
XmlSlicer.cut(xmlString)
```


As slicer can slice into a unique tag, to get the value of /tagB/innerTag1/tag3 we have to get deeper two times before tag3 becomes unique.

First cut (tagB) returns:
```
<innerTag1>
  <tag1>4</tag1>
  <tag2>5</tag2>
  <tag3>6</tag3>
</innerTag1>
<innerTag2>
  <tag1>7</tag1>
  <tag2>8</tag2>
  <tag3>9</tag3>
</innerTag2>
```

Second cut (innerTag1) returns:

```
<tag1>4</tag1>
<tag2>5</tag2>
<tag3>6</tag3>
```

And the last cut (tag3) gets our value - 6.