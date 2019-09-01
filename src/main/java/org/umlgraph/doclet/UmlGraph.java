/*
 * Create a graphviz graph based on the classes in the specified java
 * source files.
 *
 * (C) Copyright 2002-2010 Diomidis Spinellis
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

package org.umlgraph.doclet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.spi.ToolProvider;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.element.Element;
import javax.annotation.processing.Messager;
import javax.lang.model.SourceVersion;
import jdk.javadoc.doclet.DocletEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import jdk.javadoc.doclet.Reporter;
import com.sun.source.util.DocTrees;
import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;

/**
 * Doclet API implementation
 * @depend - - - OptionProvider
 * @depend - - - Options
 * @depend - - - View
 * @depend - - - ClassGraph
 * @depend - - - Version
 *
 * @version $Revision$
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
public class UmlGraph {

    private static final String programName = "UmlGraph";
    private static final String docletName = "org.umlgraph.doclet.UmlGraph";
    public static Reporter rep;
    public static Messager mes;
    public static Element viewClass;

    /** Options used for commenting nodes */
    private static Options commentOptions;

    /** Entry point through javadoc */
    public static boolean run(DocletEnvironment root) throws IOException {
	Options opt = buildOptions(root);
	rep.print(Diagnostic.Kind.NOTE, "UMLGraph doclet version " + Version.VERSION + " started");

	View[] views = buildViews(opt, root, root);
	if(views == null)
	    return false;
	if (views.length == 0)
	    buildGraph(root, opt, null);
	else
	    for (int i = 0; i < views.length; i++)
		buildGraph(root, views[i], null);
	return true;
    }

    public static void main(String args[]) {
	PrintWriter err = new PrintWriter(System.err);
        ToolProvider javadoc = ToolProvider.findFirst("javadoc").orElseThrow(null);
	javadoc.run(err, err, args);
    }

    public static Options getCommentOptions() {
    	return commentOptions;
    }

    /**
     * Creates the base Options object.
     * This contains both the options specified on the command
     * line and the ones specified in the UMLOptions class, if available.
     * Also create the globally accessible commentOptions object.
     */
    public static Options buildOptions(DocletEnvironment root) {
	commentOptions = new Options();
	commentOptions.setOptions(root.getSpecifiedElements());
	commentOptions.setOptions(findClass(root, "UMLNoteOptions"));
	commentOptions.shape = Shape.NOTE;

	Options opt = new Options();
	opt.setOptions(root.getSpecifiedElements());
	opt.setOptions(findClass(root, "UMLOptions"));
	return opt;
    }

    /** Return the ClassDoc for the specified class; null if not found. */
    public static Element findClass(DocletEnvironment root, String name) {
	Set<? extends Element> classes = root.getIncludedElements();
	for (Element cd : classes)
	    if(cd.getSimpleName().toString().equals(name))
		return cd;
	return null;
    }

    /**
     * Builds and outputs a single graph according to the view overrides
     */
    public static void buildGraph(DocletEnvironment root, OptionProvider op, Element contextDoc) throws IOException {
	if(getCommentOptions() == null)
	    buildOptions(root);
	Options opt = op.getGlobalOptions();
	rep.print(Diagnostic.Kind.NOTE, "Building " + op.getDisplayName());
	Set<? extends Element> classes = root.getIncludedElements();

	ClassGraph c = new ClassGraph(root, op, contextDoc);
	c.prologue();
	for (Element cd : classes)
	    c.printClass(cd, true);
	for (Element cd : classes)
	    c.printRelations((TypeElement) cd);
	if(opt.inferRelationships)
	    for (Element cd : classes)
		c.printInferredRelations((TypeElement) cd);
        if(opt.inferDependencies)
	    for (Element cd : classes)
		c.printInferredDependencies((TypeElement) cd);

	c.printExtraClasses(root);
	c.epilogue();
    }

    /**
     * Builds the views according to the parameters on the command line
     * @param opt The options
     * @param srcRootDoc The RootDoc for the source classes
     * @param viewRootDoc The RootDoc for the view classes (may be
     *                different, or may be the same as the srcRootDoc)
     */
    public static View[] buildViews(Options opt, DocletEnvironment srcRootDoc, DocletEnvironment viewRootDoc) {
	if (opt.viewName != null) {
	    Set<? extends Element> viewClasses = viewRootDoc.getIncludedElements();
	    for (Element viewCl : viewClasses) {
                if (viewCl.getSimpleName().toString().equals(opt.viewName)) {
		    viewClass = viewCl;
		}
		break;
	    }
	    if(viewClass == null) {
		System.out.println("View " + opt.viewName + " not found! Exiting without generating any output.");
		return null;
	    }
            DocTrees docTrees = viewRootDoc.getDocTrees();
	    DocCommentTree docCommentTree = docTrees.getDocCommentTree(viewClass);
	    List<? extends DocTree> tags = docCommentTree.getBlockTags();
		
	    int viewTag = 0;
	    for (DocTree tag : tags) {
	        if(tag.toString().equals("view"))
		    viewTag++;
	    }
	    if (viewTag == 0) {
	        System.out.println(viewClass + " is not a view!");
		return null;
	    }
	    if(viewClass.getModifiers().contains(Modifier.ABSTRACT)) {
                System.out.println(viewClass + " is an abstract view, no output will be generated!");
		return null;
	    }
	    return new View[] { buildView(srcRootDoc,(TypeElement) viewClass, opt) };
	} else if (opt.findViews) {
	    List<View> views = new ArrayList<View>();
	    Set<? extends Element> classes = viewRootDoc.getIncludedElements();
	    DocTrees docTrees = viewRootDoc.getDocTrees();

	    // find view classes
	    int viewTags = 0;
	    for (Element el : classes) {
		DocCommentTree docCommentTree = docTrees.getDocCommentTree(el);
		List<? extends DocTree> tags = docCommentTree.getBlockTags();
		for (DocTree docTr : tags) {
		    if (docTr.toString().equals("view")) 
		        viewTags++;
		}
		if (viewTags > 0 && el.getModifiers().contains(Modifier.ABSTRACT)) {
		    views.add(buildView(srcRootDoc, (TypeElement) el, opt));
		}
	    }
	    return views.toArray(new View[views.size()]);
	} else
	    return new View[0];
    }

    /**
     * Builds a view along with its parent views, recursively
     */
    public static View buildView(DocletEnvironment root, TypeElement viewClass, OptionProvider provider) {
	TypeMirror superClass = viewClass.getSuperclass();
	Element sclass = (Element)superClass;
	DocTrees docTrees = root.getDocTrees();
	int viewTags = 0;
	DocCommentTree docCommentTree = docTrees.getDocCommentTree(viewClass);
	for (DocTree viewTag : docCommentTree.getBlockTags()) {
	    if (viewTag.toString().equals("view"))
	        viewTags++;
	}
	if(sclass == null || viewTags > 0)
	    return new View(root, viewClass, provider);

	return new View(root, viewClass, buildView(root, (TypeElement) sclass, provider));
    }

    /** Option checking */
    public static int optionLength(String option) {
	return Options.optionLength(option);
    }

    /** Indicate the language version we support */
    public static SourceVersion getSupportedSourceVersion() {
	return SourceVersion.RELEASE_9;
    }
}
