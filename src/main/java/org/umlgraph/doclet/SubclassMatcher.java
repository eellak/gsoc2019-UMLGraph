package org.umlgraph.doclet;

import java.util.regex.Pattern;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.internal.doclets.toolkit.util.*;

/**
 * Matches every class that extends (directly or indirectly) a class
 * matched by the regular expression provided.
 */
public class SubclassMatcher implements ClassMatcher {

    protected DocletEnvironment root;
    protected Pattern pattern;
    static Utils utils;

    public SubclassMatcher(DocletEnvironment root, Pattern pattern) {
	this.root = root;
	this.pattern = pattern;
    }

    public boolean matches(TypeElement cd) {
	// if it's the class we're looking for return
	if(pattern.matcher(cd.toString()).matches())
	    return true;
	
	// recurse on supeclass, if available
	return cd.getSuperclass() == null ? false : matches(utils.asTypeElement(cd.getSuperclass()));
    }

    public boolean matches(String name) {
	TypeElement cd = root.classNamed(name);
	return cd == null ? false : matches(cd);
    }

}
