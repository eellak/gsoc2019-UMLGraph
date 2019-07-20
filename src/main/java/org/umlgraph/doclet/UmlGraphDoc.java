package org.umlgraph.doclet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.Set;
import java.util.regex.Pattern;

import javax.lang.model.element.PackageElement;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.StandardDoclet;
import javax.lang.model.element.Element;
import javax.lang.model.SourceVersion;
import jdk.javadoc.doclet.Reporter;
import javax.tools.Diagnostic;


/**
 * Chaining doclet that runs the standart Javadoc doclet first, and on success,
 * runs the generation of dot files by UMLGraph
 * @author wolf
 * 
 * @depend - - - WrappedClassDoc
 * @depend - - - WrappedRootDoc
 */
public class UmlGraphDoc {
    public static Reporter rep;
    static StandardDoclet sDoc;
    /**
     * Option check, forwards options to the standard doclet, if that one refuses them,
     * they are sent to UmlGraph
     */
    public static int optionLength(String option) {
	int result = optionLength(option);
	if (result != 0)
	    return result;
	else
	    return UmlGraph.optionLength(option);
    }

    /**
     * Standard doclet entry point
     * @param root
     * @return
     */
    public static boolean start(DocletEnvironment root) {
	rep.print(Diagnostic.Kind.NOTE, "UmlGraphDoc version " + Version.VERSION +  ", running the standard doclet");
	sDoc.run(root);
	rep.print(Diagnostic.Kind.NOTE, "UmlGraphDoc version " + Version.VERSION + ", altering javadocs");
	try {
	    String outputFolder = findOutputPath(root.getSpecifiedElements());

        Options opt = UmlGraph.buildOptions(root);
	    Set<? extends Element> roots = root.getSpecifiedElements();
	    opt.setOptions(roots);
	    // in javadoc enumerations are always printed
	    opt.showEnumerations = true;
	    opt.relativeLinksForSourcePackages = true;
	    // enable strict matching for hide expressions
	    opt.strictMatching = true;
//	    root.printNotice(opt.toString());

	    generatePackageDiagrams(root, opt, outputFolder);
	    generateContextDiagrams(root, opt, outputFolder);
	} catch(Throwable t) {
	    rep.print(Diagnostic.Kind.WARNING, "Error: " + t.toString());
	    t.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * Standand doclet entry
     * @return
     */
    public static SourceVersion languageVersion() {
	return SourceVersion.RELEASE_5;
    }

    /**
     * Generates the package diagrams for all of the packages that contain classes among those 
     * returned by RootDoc.class() 
     */
    private static void generatePackageDiagrams(DocletEnvironment root, Options opt, String outputFolder)
	    throws IOException {
	Set<String> packages = new HashSet<String>();
	Set<? extends Element> classes = root.getIncludedElements();
	for (Element el : classes()) {
	    Element packageDoc = el.getEnclosingElement();
	    if(!packages.toString().contains(packageDoc.getSimpleName())) {
		packages.add(packageDoc.getSimpleName().toString());
    	    OptionProvider view = new PackageView(outputFolder, (PackageElement) packageDoc, root, opt);
    	    UmlGraph.buildGraph(root, view, packageDoc);
    	    runGraphviz(opt.dotExecutable, outputFolder, packageDoc.getSimpleName().toString(), packageDoc.getSimpleName().toString(), root);
    	    alterHtmlDocs(opt, outputFolder, packageDoc.getSimpleName().toString(), packageDoc.getSimpleName().toString(),
    		    "package-summary.html", Pattern.compile("(</[Hh]2>)|(<h1 title=\"Package\").*"), root);
	    }
	}
    }

    /**
     * Generates the context diagram for a single class
     */
    private static void generateContextDiagrams(DocletEnvironment root, Options opt, String outputFolder)
	    throws IOException {
        Set<Element> classDocs = new TreeSet<Element>(new Comparator<Element>() {
            public int compare(Element cd1, Element cd2) {
                return cd1.getSimpleName().toString().compareTo(cd2.getSimpleName().toString());
            }
        });
	Set<? extends Element> classDoc = root.getIncludedElements();
        for (Element el : classDoc)
            classDocs.add(el);

	ContextView view = null;
	for (Element ElClassDoc : classDocs) {
	    try {
		if(view == null)
		    view = new ContextView(outputFolder, ElClassDoc, root, opt);
		else
		    view.setContextCenter(ElClassDoc);
		UmlGraph.buildGraph(root, view, ElClassDoc);
		runGraphviz(opt.dotExecutable, outputFolder, ElClassDoc.getEnclosingElement().getSimpleName().toString(), 
			    ElClassDoc.getSimpleName().toString(), root);
		alterHtmlDocs(opt, outputFolder, ElClassDoc.getEnclosingElement().getSimpleName().toString(), ElClassDoc.getSimpleName().toString(),
			ElClassDoc.getSimpleName().toString() + ".html", Pattern.compile(".*(Class|Interface|Enum) " 
			+ ElClassDoc.getSimpleName().toString() + ".*") , root);
	    } catch (Exception e) {
		throw new RuntimeException("Error generating " + ElClassDoc.getSimpleName().toString(), e);
	    }
	}
    }

    /**
     * Runs Graphviz dot building both a diagram (in png format) and a client side map for it.
     */
    private static void runGraphviz(String dotExecutable, String outputFolder, String packageName, String name, DocletEnvironment root) {
    if (dotExecutable == null) {
      dotExecutable = "dot";
    }
	File dotFile = new File(outputFolder, packageName.replace(".", "/") + "/" + name + ".dot");
  	File svgFile = new File(outputFolder, packageName.replace(".", "/") + "/" + name + ".svg");

	try {
	    Process p = Runtime.getRuntime().exec(new String [] {
		dotExecutable,
    "-Tsvg",
		"-o",
		svgFile.getAbsolutePath(),
		dotFile.getAbsolutePath()
	    });
	    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	    String line;
	    while((line = reader.readLine()) != null)
		rep.print(Diagnostic.Kind.WARNING, line);
	    int result = p.waitFor();
	    if (result != 0)
		rep.print(Diagnostic.Kind.WARNING, "Errors running Graphviz on " + dotFile);
	} catch (Exception e) {
	    e.printStackTrace();
	    System.err.println("Ensure that dot is in your path and that its path does not contain spaces");
	}
    }

    //Format string for the uml image div tag.
    private static final String UML_DIV_TAG = 
	"<div align=\"center\">" +
	    "<object width=\"100%%\" height=\"100%%\" type=\"image/svg+xml\" data=\"%1$s.svg\" alt=\"Package class diagram package %1$s\" border=0></object>" +
	"</div>";
    
    private static final String UML_AUTO_SIZED_DIV_TAG = 
    "<div align=\"center\">" +
        "<object type=\"image/svg+xml\" data=\"%1$s.svg\" alt=\"Package class diagram package %1$s\" border=0></object>" +
    "</div>";
    
    private static final String EXPANDABLE_UML_STYLE = "font-family: Arial,Helvetica,sans-serif;font-size: 1.5em; display: block; width: 250px; height: 20px; background: #009933; padding: 5px; text-align: center; border-radius: 8px; color: white; font-weight: bold;";

    //Format string for the java script tag.
    private static final String EXPANDABLE_UML = 
	"<script type=\"text/javascript\">\n" + 
	"function show() {\n" + 
	"    document.getElementById(\"uml\").innerHTML = \n" + 
	"        \'<a style=\"" + EXPANDABLE_UML_STYLE + "\" href=\"javascript:hide()\">%3$s</a>\' +\n" +
	"        \'%1$s\';\n" + 
	"}\n" + 
	"function hide() {\n" + 
	"	document.getElementById(\"uml\").innerHTML = \n" + 
	"	\'<a style=\"" + EXPANDABLE_UML_STYLE + "\" href=\"javascript:show()\">%2$s</a>\' ;\n" +
	"}\n" + 
	"</script>\n" + 
	"<div id=\"uml\" >\n" + 
	"	<a href=\"javascript:show()\">\n" + 
	"	<a style=\"" + EXPANDABLE_UML_STYLE + "\" href=\"javascript:show()\">%2$s</a> \n" +
	"</div>";
    
    /**
     * Takes an HTML file, looks for the first instance of the specified insertion point, and
     * inserts the diagram image reference and a client side map in that point.
     */
    private static void alterHtmlDocs(Options opt, String outputFolder, String packageName, String className,
	    String htmlFileName, Pattern insertPointPattern, DocletEnvironment root) throws IOException {
	// setup files
	File output = new File(outputFolder, packageName.replace(".", "/"));
	File htmlFile = new File(output, htmlFileName);
	File alteredFile = new File(htmlFile.getAbsolutePath() + ".uml");
	if (!htmlFile.exists()) {
	    System.err.println("Expected file not found: " + htmlFile.getAbsolutePath());
	    return;
	}

	// parse & rewrite
	BufferedWriter writer = null;
	BufferedReader reader = null;
	boolean matched = false;
	try {
	    writer = new BufferedWriter(new OutputStreamWriter(new
		    FileOutputStream(alteredFile), opt.outputEncoding));
	    reader = new BufferedReader(new InputStreamReader(new
		    FileInputStream(htmlFile), opt.outputEncoding));

	    String line;
	    while ((line = reader.readLine()) != null) {
		writer.write(line);
		writer.newLine();
		if (!matched && insertPointPattern.matcher(line).matches()) {
		    matched = true;
			
		    String tag;
		    if (opt.autoSize)
		        tag = String.format(UML_AUTO_SIZED_DIV_TAG, className);
		    else
                tag = String.format(UML_DIV_TAG, className);
		    if (opt.collapsibleDiagrams)
		    	tag = String.format(EXPANDABLE_UML, tag, "Show UML class diagram", "Hide UML class diagram");
		    writer.write("<!-- UML diagram added by UMLGraph version " +
		    		Version.VERSION + 
				" (http://www.spinellis.gr/umlgraph/) -->");
		    writer.newLine();
		    writer.write(tag);
		    writer.newLine();
		}
	    }
	} finally {
	    if (writer != null)
		writer.close();
	    if (reader != null)
		reader.close();
	}

	// if altered, delete old file and rename new one to the old file name
	if (matched) {
	    htmlFile.delete();
	    alteredFile.renameTo(htmlFile);
	} else {
	    rep.print(Diagnostic.Kind.WARNING, "Warning, could not find a line that matches the pattern '" + insertPointPattern.pattern() 
		    + "'.\n Class diagram reference not inserted");
	    alteredFile.delete();
	}
    }

    /**
     * Returns the output path specified on the javadoc options
     */
    private static String findOutputPath(Set<? extends Element> options) {
	for (Element el : options) {
	    if (el.toString().equals("-d"))
		return el.toString();
	}
	return ".";
    }
}
