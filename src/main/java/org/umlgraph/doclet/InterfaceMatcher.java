package org.umlgraph.doclet;

import java.util.regex.Pattern;
import jdk.javadoc.doclet.DocletEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.ElementKind;
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

    public InterfaceMatcher(DocletEnvironment root, Pattern pattern) {
	this.root = root;
	this.pattern = pattern;
    }

    public boolean matches(TypeElement cd) {
	// if it's the interface we're looking for, match
	if (cd.getKind().equals(ElementKind.INTERFACE) && pattern.matcher(cd.toString()).matches()) 
	    return true;
	    
	// for each interface, recurse, since classes and interfaces 
	// are treated the same in the doclet API
	List<? extends TypeMirror> interfaces = cd.getInterfaces();
	for (int i = 0; i < interfaces.size(); i++) {
	    TypeMirror arg = interfaces.get(i);
	    if(matches((TypeElement) arg))
		return true;
	}
	// recurse on supeclass, if available
	return cd.superclass() == null ? false : matches((TypeElement) cd.getSuperclass());
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
