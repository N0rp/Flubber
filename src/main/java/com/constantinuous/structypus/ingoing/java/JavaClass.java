package com.constantinuous.structypus.ingoing.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.foo.antlr.Java8BaseListener;
import com.foo.antlr.Java8Lexer;
import com.foo.antlr.Java8Parser;
import com.foo.antlr.Java8Parser.ClassBodyContext;
import com.foo.antlr.Java8Parser.ClassBodyDeclarationContext;
import com.foo.antlr.Java8Parser.ClassDeclarationContext;
import com.foo.antlr.Java8Parser.ClassMemberDeclarationContext;
import com.foo.antlr.Java8Parser.ImportDeclarationContext;
import com.foo.antlr.Java8Parser.MethodDeclarationContext;
import com.foo.antlr.Java8Parser.MethodInvocationContext;
import com.foo.antlr.Java8Parser.MethodNameContext;
import com.foo.antlr.Java8Parser.NormalClassDeclarationContext;
import com.foo.antlr.Java8Parser.PackageDeclarationContext;
import com.foo.antlr.Java8Parser.PrimaryContext;
import com.foo.antlr.Java8Parser.TypeDeclarationContext;
import com.foo.antlr.Java8Parser.TypeNameContext;

public class JavaClass {
	
	private String packageName;
	private String name;
	private Set<JavaClassImport> imports = new HashSet<JavaClassImport>();
	private Set<String> innerClasses = new HashSet<String>();
	private Set<JavaMethod> methods = new HashSet<JavaMethod>();
	
	/**
	 * Parse single java file and store its contents
	 * @param filePath
	 * @return <code>false</code> if there was a parse exception. Data could be retained however. Else <code>true</code>
	 * @throws IOException
	 */
	public boolean parse(String filePath) throws IOException {

		Lexer lexer = null;
		try {
			lexer = new Java8Lexer(new ANTLRFileStream(filePath));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			// long start = System.currentTimeMillis();
			// tokens.fill(); // load all and check time
			// long stop = System.currentTimeMillis();
			// lexerTime += stop-start;
			// Create a parser that reads from the scanner
			Java8Parser parser = new Java8Parser(tokens);

			parser.addErrorListener(new DiagnosticErrorListener());
			
			parser.addErrorListener(new BaseErrorListener() {

			});

			parser.setErrorHandler(new BailErrorStrategy());

			parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

			parser.addParseListener(new JavaClassListener());

			// start parsing at the compilationUnit rule
			ParserRuleContext t = parser.compilationUnit();
			return true;
		}catch(ParseCancellationException e){
			return false;
		}
	}
	
	@Override
	public String toString(){
		String result = "Java Class:["+name+"] Package:["+packageName+"] ["+imports.size()+" Imports] ["+methods.size()+" methods]";
		return result;
	}
	
	/**
	 * Get the package name+"."+class name
	 * @return
	 */
	public String getFullPackage(){
		return packageName+"."+name;
	}
	
	public Set<JavaClassImport> getImports(){
		return imports;
	}
	
	public String getName(){
		return name;
	}
	
	public void setAvailablePackages(Map<String, JavaClass> fullpackage2Class){
		Iterator<Entry<String, JavaClass>> it = fullpackage2Class.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, JavaClass> entry = (Map.Entry<String, JavaClass>)it.next();
	        JavaClassImport imp = getImport(entry.getKey());
	        if(imp != null){
	        	imp.setJavaClass(entry.getValue());
	        }
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	private JavaClassImport getImport(String name){
		for(JavaClassImport imp : imports){
			if(imp.getName().equals(name)){
				return imp;
			}
		}
		
		return null;
	}

	/**
	 * Parses Java Class and stores important values.
	 * @author richardg
	 *
	 */
	private class JavaClassListener extends Java8BaseListener {

		private MethodDeclarationContext currentMethod;
		private NormalClassDeclarationContext classContext;
		
		@Override
		public void exitPackageDeclaration(PackageDeclarationContext ctx) {
			String pckName = "";
			for(TerminalNode node : ctx.Identifier()){
				if(pckName.length() > 0){
					pckName += ".";
				}
				pckName += node.getText();
			}
//			System.out.println("PackageName: "+pckName);
			packageName = pckName;
		}

		@Override
		public void exitImportDeclaration(ImportDeclarationContext ctx) {
			if(ctx.singleTypeImportDeclaration() != null){
//				System.out.println("Single Type Import Declaration exit: "
//						+ ctx.singleTypeImportDeclaration().typeName().getText());
				imports.add(new JavaClassImport(ctx.singleTypeImportDeclaration().typeName().getText()));
			}
			else if(ctx.typeImportOnDemandDeclaration() != null){
				System.err.println("Type Import OnDemand Declaration exit: "
						+ ctx.typeImportOnDemandDeclaration().packageOrTypeName().getText());
			}
			else if(ctx.singleStaticImportDeclaration() != null){
				System.err.println("Single Static Import Declaration exit: "
						+ ctx.singleStaticImportDeclaration().typeName().getText());
			}
			else if(ctx.staticImportOnDemandDeclaration() != null){
				System.err.println("Static Import OnDemand Declaration exit: "
						+ ctx.staticImportOnDemandDeclaration().typeName().getText());
			}
		}

		@Override
		public void exitMethodInvocation(MethodInvocationContext ctx) {
			String className = "";
			String methodName = "";
			
			if( (ctx.getChildCount() == 3 || ctx.getChildCount() == 4) && ctx.getChild(0) instanceof MethodNameContext){
				// name + ( + args + )
					methodName = ctx.methodName().getText();
			}else if((ctx.getChildCount() == 5 || ctx.getChildCount() == 6) && ctx.getChild(0) instanceof TypeNameContext){
				// obj + . + name + ( + args + )
				className = ctx.getChild(0).getText();
				
				if(!ctx.getChild(1).getText().equals(".")){
					System.err.println("<<<Second child should be a dot '.'");
				}
				methodName = ctx.getChild(2).getText();
			}else if((ctx.getChildCount() == 1 || ctx.getChildCount() == 5) && ctx.getChild(0) instanceof PrimaryContext){
//				System.out.println("<<<Method Invocation on sth weird. Probably method call on anonymous object");
			}
			else{
				System.err.println("<<<Method Invocation on sth weird");
				methodName = "$$$"+ctx.methodName();
			}
//			System.out.println("<<<Method invocation exit: "+className
//					+ " "+methodName);
		}

		@Override
		public void enterMethodDeclaration(MethodDeclarationContext ctx) {
//			System.out.println("<<Method Declaration enter: "
//					+ ctx.methodHeader());
			currentMethod = ctx;
		}
		
		@Override
		public void exitMethodDeclaration(MethodDeclarationContext ctx) {
			// method if a class member declaration -> is a class body declaration -> is a class body -> is a normal class
//			System.out.println("<<Method Declaration exit: "
//					+ ctx.methodHeader().methodDeclarator().getText());
			if(currentMethod != ctx){
				System.err.println("NOT Same Method Context!!!");
			}
			currentMethod = null;
			
			if(ctx.getParent() instanceof ClassMemberDeclarationContext
					&& ctx.getParent().getParent() instanceof ClassBodyDeclarationContext
					&& ctx.getParent().getParent().getParent() instanceof ClassBodyContext){
				if(ctx.getParent().getParent().getParent().getParent() == classContext){
//					System.out.println("Method is inside our Class");
					methods.add(new JavaMethod(ctx.methodHeader().getText()));
				}
			}else{
				System.err.println("!!! Method is somewhere else");
			}
			
		}
		

		@Override
		public void enterNormalClassDeclaration(NormalClassDeclarationContext ctx) {
			// enum classes are non-normal
//			System.out.println("<Normal Class Declaration enter: " + ctx.Identifier());
			classContext = ctx;
		}

		@Override
		public void exitNormalClassDeclaration(NormalClassDeclarationContext ctx) {
			// enum classes are non-normal
			
			// outer classes are: inside classdeclaration -> inside typedeclaration -> inside compilationunit
			// inner classes are: inside classdeclration -> inside classmemberdeclaration -> inside classdeclaration 
			if(ctx.getParent() instanceof ClassDeclarationContext
					&& ctx.getParent().getParent() instanceof TypeDeclarationContext){
				// outer class
				name = ctx.Identifier().getText();
			}else if(ctx.getParent() instanceof ClassDeclarationContext
					&& ctx.getParent().getParent() instanceof ClassMemberDeclarationContext){
				// inner class
				innerClasses.add(ctx.Identifier().getText()); 
			}else{
				System.err.println("!!! Neither inner nor outer class");
			}
		}
		
	}
	
}
