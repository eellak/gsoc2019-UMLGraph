package org.umlgraph.doclet;

import java.util.regex.Pattern;
import java.util.Set;
import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import javax.lang.model.element.Element;

/**
 * Matches every class that extends (directly or indirectly) a class
 * matched by the regular expression provided.
 */
public class SubclassMatcher implements ClassMatcher {

    protected DocletEnvironment root;
    protected Pattern pattern;

    public SubclassMatcher(DocletEnvironment root, Pattern pattern) {
	this.root = root;
	this.pattern = pattern;
    }

    public boolean matches(TypeElement cd) {
	// if it's the class we're looking for return
	if(pattern.matcher(cd.toString()).matches())
	    return true;
	
	// recurse on supeclass, if available
	return cd.getSuperclass() == null ? false : matches((TypeElement) cd.getSuperclass());
    }

    public boolean matches(String name) {
	TypeElement found = null;
	Set<? extends Element> incElements = root.getIncludedElements();
	for (Element el : incElements) {
	    if (el.getSimpleName().toString().equals(name)) {
                found = (TypeElement) el;
	    }
	}
	return found == null ? false : matches(found);
    }

}
