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

import javax.lang.model.element.TypeElement;
import org.umlgraph.doclet.PackageMatcher;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.junit.Test;

public class PackageMatcherTest {
   public PackageMatcher packM = mock(PackageMatcher.class);
   public boolean typeElBool, strBool;
   public String stringBool = "myName";
   @Mock
   TypeElement tel = mock(TypeElement.class);
   
   @Test
   public void testMatchesTypeEl() {
      typeElBool = packM.matches(tel);
      assertFalse(typeElBool);
   }
   @Test
   public void testMatchesString() {
      strBool = packM.matches(stringBool);
      assertFalse(strBool);
   }
   
}
