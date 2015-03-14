# XmlSlicer. Case 1 #
XML
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
Code
```
//gets "6"
String value = XmlSlicer.cut(xmlString).get("tagB").get("innerTag1").get("tag3").toString();
```
Explanation

The following code creates a new thread-safe instance of XmlSlicer for input String (xmlString).
```
XmlSlicer.cut(xmlString)
```


As slicer can slice into a unique tag, to get the value of /tagB/innerTag1/tag3 we have to get deeper two times before tag3 becomes unique.

First cut (tag2) returns:
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

And the last cut (tag2) gets our value - 6.

# XmlBuilder. Case 1 #
Code:
```
String xml = XmlBuilder.newXml()
    .openTag("xml").withAttribute("id", 1)
        .openTag("thisishow")
            .withValue("you can build")
        .closeTag()
        .openTag("your").withAttribute("xml", "nicely").toString(true);
```
XML (formatted):
```
<xml id="1">
  <thisishow>
    you can build
  </thisishow>
  <your xml="nicely"/>
</xml>
```

# XmlSlicer. Case 2 #
XML:
```
<nest>
  <bird>crow</bird>
  <bird>tweety</bird>
  <bird>chicken</bird>
</nest> 
```
Code:
```
//Should be a list of "crow", "tweety" and "chicken"
List<String> birds = XmlSlicer.cut(xmlString).getAll("bird").asList();
```