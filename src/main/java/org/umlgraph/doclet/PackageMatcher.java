package org.umlgraph.doclet;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import java.util.List;

public class PackageMatcher implements ClassMatcher {
    protected PackageElement packageDoc;

    public PackageMatcher(PackageElement packageDoc) {
	super();
	this.packageDoc = packageDoc;
    }

    public boolean matches(TypeElement cd) {
	return cd.getQualifiedName().toString().equals(packageDoc.toString());
    }

    List<? extends Element> allElements = packageDoc.getEnclosedElements();
    public boolean matches(String name) {
	for (Element cd : allElements)
	    if (cd.getSimpleName().toString().equals(name))
		return true;
	return false;
    }

}
