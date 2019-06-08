package org.umlgraph.doclet;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import java.util.List;

public class PackageMatcher implements ClassMatcher 
{
    protected PackageElement packageDoc; // edit PackageDoc
    public PackageMatcher(PackageElement packageDoc) 
    {
		super();
		this.packageDoc = packageDoc;
    }
    public boolean matches(PackageElement cd) // edit ClassDoc -> TypeElement -> PackageElement
    {
    	return cd.getQualifiedName().toString().equals(packageDoc.toString()); // edit 
    }

    List<? extends Element> allElements = packageDoc.getEnclosedElements(); // Returns the top-level classes and interfaces within this package.
    public boolean matches(String name) 
    {
		for (Element cd : allElements) // edit allClasses() -> getEnclosedElements()
		{
		    if (cd.getSimpleName().toString().equals(name)) // getQualifiedName() 
			return true;			
		}
		return false;
    }
    @Override
    public boolean matches(TypeElement cd) {
     	        // TODO Auto-generated method stub
	        return false;
    }
}
