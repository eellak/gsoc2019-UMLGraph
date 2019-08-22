# UMLGraph - Declarative Drawing of UML Diagrams

[![Build Status](https://travis-ci.org/dspinellis/UMLGraph.svg?branch=master)](https://travis-ci.org/dspinellis/UMLGraph)

*UMLGraph* allows the declarative specification and drawing of UML diagrams.
You can browse the system's documentation
through [this link](http://www.spinellis.gr/umlgraph/doc/index.html),
or print it through [this link](http://www.spinellis.gr/umlgraph/doc/indexw.html).

In order to run *UMLGraph*, you need to have [GraphViz](https://www.graphviz.org/)
installed in your system path. On most Linux distributions, this can be easily
installed using the regular package manager.

To compile the Java doclet from the source code run *ant* on the
*build.xml* file.

If you change the source code, you can run regression tests by
executing *ant test*.

Visit the project's [home page](http://www.spinellis.gr/umlgraph) for more information.

## Compatibility

Currently, only Java 8 is supported by the Doclet.

In Java 9 the JavaDoc Doclet API changed substantially, and the doclet therefore
needs to be largely rewritten.

Sorry, this has not happened yet - volunteers for this task are welcome.

## Development versions

In order to use development versions, you can use [JitPack](https://jitpack.io/#dspinellis/UMLGraph/master-SNAPSHOT).
Note that as this is compiled on demand, you may sometimes see a "Read timed out" when the package is recompiled,
and it should be fine a few seconds later. And because the master branch can change any time, you may want to use
a versioned snapshot instead (see the Jitpack documentation for details).

**Gradle**:
```
repositories { maven { url 'https://jitpack.io' } }
configurations { umlgraph }
dependencies { umlgraph 'com.github.dspinellis:UMLGraph:master-SNAPSHOT' }
javadoc {
  doclet = 'org.umlgraph.doclet.UmlGraphDoc'
  docletpath = configurations.umlgraph.files.asType(List)
  tags("hidden:X", "opt:X", "has:X", "navhas:X", "assoc:X", "navassoc:X",
       "composed:X", "navcomposed:X", "stereotype:X", "depend:X")
}
```

**Maven**:
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
...
<dependency>
    <groupId>com.github.dspinellis</groupId>
    <artifactId>UMLGraph</artifactId>
    <version>master-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

**Jar download**: <https://jitpack.io/com/github/dspinellis/UMLGraph/master-SNAPSHOT/UMLGraph-master-SNAPSHOT.jar>

## GSoC 2019 - Contributions

All the work that needs to be done through the GSoC 2019 will be presented in this section. The main task for this year's GSoC is to update the UMLGraph tool by making it fully functional using the latest jdk.javadoc.doclet doclet API instead of the com.sun.javadoc API that is now being used. In order to achieve that, several changes need to be done on the existing code, which are going to be presented thoroughly here in the next three months. Relevant and necessary information about those APIs are shown in the documentation links below

**com.sun.javadoc**: <https://docs.oracle.com/javase/7/docs/jdk/api/javadoc/doclet/com/sun/javadoc/package-summary.html>   
**jdk.javadoc.doclet**: <https://docs.oracle.com/javase/9/docs/api/jdk/javadoc/doclet/package-summary.html>

## Overview

UMLGraph is a useful and practical tool for generating UML class and sequence diagrams. Using UMLGraph, desingers and developers are able to create graphical representations of UML diagrams automatically, instead of using drawing editors, which require specific coordination skills for placing shapes on the canvas. Meeting the requirement of drawing a UML diagram via UMLGraph, can be a relief for any developer since precious time and effort can be saved.

UMLGraph is implemented as a javadoc doclet, which is a program that satisfies the doclet API and the version of Java that was supporting until recently is Java 8. The development of UMLGraph was based on the usage of the com.sun.javadoc doclet API, which is deprecated in the newest versions of Java after Java 8. The functionalities of com.sun.javadoc where superseded by those of jdk.javadoc.doclet doclet API. Thus, a migration for UMLGraph, from Java 8 and com.sun.javadoc to Java 9 and jdk.javadoc.doclet was necessary.

It is importand to mention that none of the fucntionalities of UMLGraph where supposed to change during the migration to Java 9. The only thing that was necessary to be modified was the doclet API, which UMLGraph is based on.

## Working on UMLGraph during GSoC

The main goal during GSoC 2019 was the successful immigration of UMLGraph from Java 8 to Java 9. At the end of the day, every instance, method or feature of the package com.sun.javadoc should be replaced by its corresponding feature in jdk.javadoc.doclet doclet. This goal was achieved during the three months of the program and now every .java file in UMLGraph package is using interfaces that belong in jdk.javadoc.doclet.

## Typical examples of immigration

Some typical examples of the immigration procedure are the following.

- [ClassDoc](https://docs.oracle.com/javase/7/docs/jdk/api/javadoc/doclet/com/sun/javadoc/ClassDoc.html), which represents a java class or interface and provides necessary information about it, can be superseded by an [TypeElement](https://docs.oracle.com/en/java/javase/11/docs/api/java.compiler/javax/lang/model/element/TypeElement.html), which can represent a java class or interface as well.

- [Doc](https://docs.oracle.com/javase/7/docs/jdk/api/javadoc/doclet/com/sun/javadoc/Doc.html) is a superinterface of ClassDoc, which is also capable of a class/package/constructor/method/field representation. Doc's functionality can be replaced by the [Element](https://docs.oracle.com/en/java/javase/11/docs/api/java.compiler/javax/lang/model/element/Element.html) interface, which is a superinterface of TypeElement and can represent information about program elements such as modules, packages, classes or methods.

- As for the types of Java progamming languages, interface [Type](https://docs.oracle.com/javase/7/docs/jdk/api/javadoc/doclet/com/sun/javadoc/Type.html) was used in UMLGraph under Java 8, which is now superseded by the interface [TypeMirror](https://docs.oracle.com/en/java/javase/11/docs/api/java.compiler/javax/lang/model/type/TypeMirror.html).
