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
import static org.mockito.Mockito.mock;
import org.umlgraph.doclet.Options;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {
   public String optStr1 = "qualify";
   public String optStr2 = "nodefillcolor";
   public String optStr3 = "contextPattern";
   public String optStr4 = "randomstring";
   public String str1 = null;
   public String str2 = "ab";
   public String str3 = "a";
   public String str4 = "";
   public String fixApiDocR1, fixApiDocR2, fixApiDocR3, fixApiDocR4;
   public String givenOpt = "nodefillcolor";
   public String expectOpt = "nodefillcolor";
   public Options opt = new Options();
   public Options opt2 = new Options();
   public int optLength1, optLength2, optLength3, optLength4;
   public boolean matchesHideExp, matchesIncludeExp, matchesCollPack, matchOpt;
   public TypeElement tel = mock(TypeElement.class);
   
   @Test
   public void testOptionLength() {
      optLength1 = Options.optionLength(optStr1);
      optLength2 = Options.optionLength(optStr2);
      optLength3 = Options.optionLength(optStr3);
      optLength4 = Options.optionLength(optStr4);
      assertEquals(optLength1,1);
      assertEquals(optLength2,2);
      assertEquals(optLength3,3);
      assertEquals(optLength4,0);
   }
   @Test
   public void testFixApiDocRoot() {
      fixApiDocR1 = opt.fixApiDocRoot(str1);
      fixApiDocR2 = opt.fixApiDocRoot(str2);
      fixApiDocR3 = opt.fixApiDocRoot(str3);
      fixApiDocR4 = opt.fixApiDocRoot(str4);
      assertNull(fixApiDocR1);
      assertEquals(fixApiDocR2,"ab/");
      assertEquals(fixApiDocR3,"a/");
      assertEquals(fixApiDocR4,"");
   }
   @Test
   public void testMatchesHideExpression() {
      matchesHideExp = opt.matchesHideExpression(str2);
      assertFalse(matchesHideExp);
   }
   @Test
   public void testMatchesIncludeExp() {
      matchesIncludeExp = opt.matchesIncludeExpression(str2);
      assertFalse(matchesIncludeExp);
   }
   @Test
   public void testMatchesCollPackageExpression() {
      matchesCollPack = opt.matchesCollPackageExpression(str2);
      assertFalse(matchesCollPack);
   }
   @Test
   public void testGetOptionsFor() {
      opt2 = opt.getOptionsFor(tel);
      assertNotNull(opt2);
   }
   @Test
   public void testMatchOption() {
      matchOpt = Options.matchOption(givenOpt, expectOpt, false);
      assertTrue(matchOpt);
   }
   
}   
