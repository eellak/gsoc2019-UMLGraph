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

import org.umlgraph.doclet.SubclassMatcher;
import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import java.util.regex.Pattern;
import org.mockito.Mock;
import org.junit.Test;

public class SubclassMatcherTest {
    @Mock
    public TypeElement tel = mock(TypeElement.class);
    public DocletEnvironment docEnv = mock(DocletEnvironment.class);
    public String pattern = "(.*)(\\d+)(.*)";
    public String name = "SubclassMatcher";
    public Pattern pat = Pattern.compile(pattern);
    public SubclassMatcher subclassMatch = new SubclassMatcher(docEnv,pat);
    public boolean matched1, matched2;
    
    @Test
    public void testConstructor() {
        assertNotNull(subclassMatch);
    }
    @Test
    public void testMatchesTypeEl() {
        matched1 = subclassMatch.matches(tel);
        assertTrue(matched1);
    }
    @Test
    public void testMatchesStr() {
        matched2 = subclassMatch.matches(name);
        assertFalse(matched2);
    }
    
}
