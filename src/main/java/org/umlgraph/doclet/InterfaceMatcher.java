package org.umlgraph.doclet;

import java.util.regex.Pattern;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import jdk.javadoc.internal.doclets.toolkit.util.*;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;

/**
 * Matches every class that implements (directly or indirectly) an
 * interfaces matched by regular expression provided.
 */
public class InterfaceMatcher implements ClassMatcher {

    protected DocletEnvironment root;
    protected Pattern pattern;
    protected static Utils utils;

    public InterfaceMatcher(DocletEnvironment root, Pattern pattern) {
	this.root = root;
	this.pattern = pattern;
    }

    public boolean matches(TypeElement cd) {
	// if it's the interface we're looking for, match
	List<? extends TypeMirror> interfaces = cd.getInterfaces();
	
	// for each interface, recurse, since classes and interfaces 
	// are treated the same in the doclet API
	for (int i = 0; i < interfaces.size(); i++) {
	    TypeMirror arg = interfaces.get(i);
	    if(matches(utils.asTypeElement(arg)))
		return true;
	}
	// recurse on supeclass, if available
	return cd.superclass() == null ? false : matches(utils.asTypeElement(cd.getSuperclass()));
    }

    public boolean matches(String name) {
	TypeElement found = null;
	Set<? extends Element> incElements = root.getIncludedElements();
	for (Element el : incElements) {
            if (el.toString().equals(name)) {
                found = (TypeElement) el;
	    }
	}
	return found == null ? false : matches(found);
    }

}
