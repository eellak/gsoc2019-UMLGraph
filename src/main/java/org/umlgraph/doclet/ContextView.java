package org.umlgraph.doclet;

import java.io.IOException;
import java.util.regex.Pattern;

//import com.sun.javadoc.ClassDoc;
//import com.sun.javadoc.RootDoc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import javax.lang.model.element.Element;

/**
 * A view designed for UMLDoc, filters out everything that it's not directly
 * connected to the center class of the context.
 * <p>
 * As such, can be viewed as a simplified version of a {@linkplain View} using a
 * single {@linkplain ContextMatcher}, but provides some extra configuration
 * such as context highlighting and output path configuration (and it is
 * specified in code rather than in javadoc comments).
 * @author wolf
 * 
 */
public class ContextView implements OptionProvider 
{
    private Element cd; // edit ClassDoc -> TypeElement -> Element
    private ContextMatcher matcher;
    private Options globalOptions;
    private Options myGlobalOptions;
    private Options hideOptions;
    private Options centerOptions;
    private Options packageOptions;
    private static final String[] HIDE_OPTIONS = new String[] { "hide" };

    public ContextView(String outputFolder, Element cd, DocletEnvironment root, Options parent) // edit ClassDoc -> TypeElement -> Element
	throws IOException                                                                // edit RootDoc -> DocletEnvironment
    {
		this.cd = cd; // edit
		String outputPath = cd.getEnclosingElement().getSimpleName().toString().replace('.', '/') + "/" + cd.getSimpleName() + ".dot"; //containingPackage().name().replace('.', '/') + "/" + cd.name() + ".dot";
	
		// setup options statically, so that we won't need to change them so
		// often
		this.globalOptions = parent.getGlobalOptions();
		
		this.packageOptions = parent.getGlobalOptions();  
		this.packageOptions.showQualified = false;
	
		this.myGlobalOptions = parent.getGlobalOptions();
		this.myGlobalOptions.setOption(new String[] { "output", outputPath });
		this.myGlobalOptions.setOption(HIDE_OPTIONS);
	
		this.hideOptions = parent.getGlobalOptions();
		this.hideOptions.setOption(HIDE_OPTIONS);
	
		this.centerOptions = parent.getGlobalOptions();
		this.centerOptions.nodeFillColor = "lemonChiffon";
		this.centerOptions.showQualified = false;
	
		this.matcher = new ContextMatcher(root, Pattern.compile(Pattern.quote(cd.toString())), myGlobalOptions, true);
    }

    public void setContextCenter(Element contextCenter) // edit ClassDoc -> TypeElement -> Element
    {
		this.cd = contextCenter; // edit
		String outputPath = cd.getEnclosingElement().getSimpleName().toString().replace('.', '/') + "/" + cd.getSimpleName() + ".dot";
		this.myGlobalOptions.setOption(new String[] { "output", outputPath });
		matcher.setContextCenter(Pattern.compile(Pattern.quote(cd.toString())));
    }

    public String getDisplayName() {
    	return "Context view for class " + cd;
    }

    public Options getGlobalOptions() {
    	return myGlobalOptions;
    }

    public Options getOptionsFor(TypeElement cd) // edit ClassDoc -> TypeElement -> Element
    {
		Options opt; // edit
		if (globalOptions.matchesHideExpression(cd.getQualifiedName().toString()) || !(matcher.matches(cd) || globalOptions.matchesIncludeExpression(cd.getQualifiedName().toString()))) {
			opt = hideOptions;
		} else if (cd.equals(this.cd)) {
			opt = centerOptions;
		} else if(cd.getEnclosingElement().equals(this.cd.getEnclosingElement())){ // edit
			opt = packageOptions;
		} else {
			opt = globalOptions;
		}
		Options optionClone = (Options) opt.clone();
		overrideForClass(optionClone, cd);
		return optionClone;
    }

    public Options getOptionsFor(String name) 
    {
		Options opt;
		if (!matcher.matches(name))
			opt = hideOptions;
		else if (name.equals(cd.getSimpleName().toString())) // edit
			opt = centerOptions;
		else
			opt = globalOptions;
		Options optionClone = (Options) opt.clone();
		overrideForClass(optionClone, name);
		return optionClone;
    }

    public void overrideForClass(Options opt, TypeElement cd) // edit ClassDoc -> TypeElement -> Element 
    {
		opt.setOptions(cd);
		if (opt.matchesHideExpression(cd.getQualifiedName().toString()) || !(matcher.matches(cd) || opt.matchesIncludeExpression(cd.getQualifiedName().toString()))) // edit
		    opt.setOption(HIDE_OPTIONS);
		if (cd.equals(this.cd))
		    opt.nodeFillColor = "lemonChiffon";
    }

    public void overrideForClass(Options opt, String className) 
    {
		if (!(matcher.matches(className) || opt.matchesIncludeExpression(className)))
		    opt.setOption(HIDE_OPTIONS);
    }

}
