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

import org.umlgraph.doclet.ContextMatcher;
import static org.mockito.Mockito.*;
import javax.lang.model.element.TypeElement;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContextMatcherTest {
   @Mock
   public ContextMatcher cmatch = mock(ContextMatcher.class);
   public String strName = "newName";
   public String pattern = "(.*)(\\d+)(.*)";
   public boolean strBool, typeElBool;
   public TypeElement tel = mock(TypeElement.class);
   public Pattern pat = Pattern.compile(pattern);
   List<TypeElement> matched;
   
   @Test
   public void testMatchesStr() {
      strBool = cmatch.matches(strName);
      assertFalse(strBool);
   }
   @Test
   public void testMatchesTypeEl() {
      typeElBool = cmatch.matches(tel);
      assertFalse(typeElBool);
   }
   @Test
   public void testSetContextCenter() {
      matched = new ArrayList<TypeElement>();
      cmatch.setContextCenter(pat);
      assertEquals(pat.matcher(tel.toString()).matches(),true);
      if (pat.matcher(tel.toString()).matches()) {
         matched.add(tel);
         assertEquals(matched.size(),1);
         cmatch.addToGraph(tel);
      }
   }

}
