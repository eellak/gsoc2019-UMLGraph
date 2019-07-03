package org.umlgraph.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import javax.lang.model.element.Element;
import org.umlgraph.doclet.Visibility;

public class VisibilityTest {
	@Mock
	Element el = mock(Element.class); 
	@Test
	public void visibilityTest() {
		 Visibility.get(el);
		 assertEquals(Visibility.get(el), Visibility.PACKAGE);
	}
}
