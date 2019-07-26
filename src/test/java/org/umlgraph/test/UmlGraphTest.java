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

import jdk.javadoc.doclet.DocletEnvironment;
import javax.lang.model.element.TypeElement;
import org.umlgraph.doclet.OptionProvider;
import javax.lang.model.element.Element;
import static org.mockito.Mockito.mock;
import javax.lang.model.SourceVersion;
import org.umlgraph.doclet.UmlGraph;
import org.umlgraph.doclet.Options;
import static org.junit.Assert.*;
import org.umlgraph.doclet.View;
import java.io.IOException;
import org.mockito.Mock;
import org.junit.Test;

public class UmlGraphTest {
    
    @Mock
    public DocletEnvironment docEnv = mock(DocletEnvironment.class);
    public TypeElement typeEl = mock(TypeElement.class);
    public UmlGraph umlgraph = new UmlGraph();
    public Element el = mock(Element.class);
    public Options opt = new Options();
    public OptionProvider provider;
    public SourceVersion sVersion;
    public int optionLength;
    public boolean start;
    public View view;
    
    @Test
    public void testLanguageVersion() {
        sVersion = UmlGraph.languageVersion();
        assertEquals(sVersion,SourceVersion.RELEASE_9);
    }
    @Test
    public void testOptionLength() {
        optionLength = UmlGraph.optionLength("qualify");
        assertEquals(optionLength,1);
    }
    @Test
    public void testBuildView() {
        view = UmlGraph.buildView(docEnv, typeEl, provider);
        assertNotNull(view);
        assertTrue(view instanceof View);
    }
    @Test
    public void testFindClass() {
        el = UmlGraph.findClass(docEnv, "Options");
        assertNull(el);
    }
    @Test
    public void testBuildOptions() {
        opt = UmlGraph.buildOptions(docEnv);
        assertNotNull(opt);
        assertTrue(opt instanceof Options);
    }
    
}
