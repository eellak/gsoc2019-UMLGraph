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

import org.umlgraph.doclet.ClassGraph;
import org.umlgraph.doclet.Options;
import static org.mockito.Mockito.*;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.element.Element;
import static org.junit.Assert.*;

public class ClassGraphTest {
    public ClassGraph classGr = mock(ClassGraph.class);
    public TypeElement typeEl = mock(TypeElement.class);
    public DeclaredType declType = mock(DeclaredType.class);
    public Element el = mock(Element.class);
    public Options opts = new Options();
    public String visibility, classToUrl;
    public boolean hidd;
    
    @Test
    public void testVisibility() {
        visibility = classGr.visibility(opts, el);
        assertNull(visibility);
    }
    @Test
    public void testHidden() {
        hidd = classGr.hidden(el);
        assertFalse(hidd);
    }
    @Test
    public void testClassToUrl() {
        classToUrl = classGr.classToUrl(typeEl, true);
        assertNull(classToUrl);
    }
    @Test
    public void testQualifiedName() {
        String val = ClassGraph.qualifiedName(opts, "name");
        assertNotNull(val);
        assertEquals(val,"name");
    }
    @Test
    public void testTypeParameters() {
        String typeParams = classGr.typeParameters(opts, declType);
        assertNull(typeParams);
    }
    
}
