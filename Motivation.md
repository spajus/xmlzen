# The story of XML #

XML is a great format for sharing structured hierarchical information across different programs. It's quite readable to people, there are many universal editors and libraries for working with this pretty simple and straightforward format.

However, over the years XML got more and more complicated, things like schemas, namespaces, DOM, SAX, XSLT, XPATH made the pretty simple XML into an ugly format that most developers frown upon. People started looking around for new serialized data formats like JSON, YAML, but XML seems to be too widespread to be easily pushed away.

The illustration below is a map that shows how bloated everything is.

![http://lh4.ggpht.com/_nP8Aa6cTHpo/Spdg41mFFPI/AAAAAAAACUQ/NkchfZ_RWz0/s800/xml.gif](http://lh4.ggpht.com/_nP8Aa6cTHpo/Spdg41mFFPI/AAAAAAAACUQ/NkchfZ_RWz0/s800/xml.gif)

# XML and Java #

Java is currently one of the most used server side programming languages, and casual java development usually involves lot's of XML processing. There are huge APIs with solid implementations that have been there for decades, and yet, all these implementations are still causing problems. What is the API good for, if things that run on one implementation usually does not work with another? Why can a switch from Java 5 to Java 6 break XML parsers and processors? It should be different.

# Less is More #

In many cases you will only need basic features for processing XML files, and in such cases there is no need to choke on huge APIs and implementations that support all the gore displayed above. By using bloatware you create new bloatware. Find the inspiration in minimalism, try XML Zen.