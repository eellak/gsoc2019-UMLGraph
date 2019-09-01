/*
 * UmlGraph class diagram testing framework
 *
 * Contibuted by Evangelos Karatarakis
 * (C) Copyright 2005 Diomidis Spinellis
 *
 * Permission to use, copy, and distribute this software and its
 * documentation for any purpose and without fee is hereby granted,
 * provided that the above copyright notice appear in all copies and that
 * both that copyright notice and this permission notice appear in
 * supporting documentation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 *
 *
 */
 
 package org.umlgraph.test;
 
 import javax.lang.model.element.Element;
 import org.umlgraph.doclet.UmlGraphDoc;
 import static org.mockito.Mockito.mock;
 import javax.lang.model.SourceVersion;
 import static org.junit.Assert.*;
 import org.mockito.Mock;
 import org.junit.Test;
 import java.util.Set;
 
 public class UmlGraphDocTest {
     public String option = "UmlOption";
     public String outputPath;
     public UmlGraphDoc umlGrDoc;
     public int optLength;
     public SourceVersion sVersion;
     
     @Test
     public void testLanguageVersion() {
         sVersion = UmlGraphDoc.getSupportedSourceVersion();
         assertEquals(sVersion,SourceVersion.RELEASE_5);
     }
     
}
