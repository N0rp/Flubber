package com.constantiuous.structypus;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Stack;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

import com.foo.antlr.ComponentFunction;
import com.foo.antlr.Java8BaseListener;
import com.foo.antlr.Java8Lexer;
import com.foo.antlr.Java8Parser;
import com.foo.antlr.Java8Parser.ClassBodyDeclarationContext;
import com.foo.antlr.Java8Parser.ImportDeclarationContext;
import com.foo.antlr.Java8Parser.MethodDeclarationContext;
import com.foo.antlr.Java8Parser.NormalClassDeclarationContext;
import com.foo.antlr.Java8Parser.SingleTypeImportDeclarationContext;
import com.foo.antlr.Java8Parser.VariableDeclaratorContext;

public class AntlrTest {

	@Test
	public void testParseFile() throws Exception {
		System.out.println("------------------------------");
		String filePath = TestProperties.RES_JAVA
				+ "com/faa/ImportsSimpleFile.java";
		parseFile(filePath);
	}

	@Test
	public void testExampleField() throws Exception {
		System.out.println("------------------------------");
		String filePath = TestProperties.RES_JAVA
				+ "com/faa/ImportsSimpleFile.java";

		File f = new File(filePath);

		Java8Lexer l = new Java8Lexer(new ANTLRInputStream(new FileInputStream(
				f)));
		Java8Parser p = new Java8Parser(new CommonTokenStream(l));
		p.addErrorListener(new BaseErrorListener() {

		});

		p.addParseListener(new AnnListener());

		System.out.println("CompilationUnit: " + p.compilationUnit());
		System.out.println("Packagename: " + p.packageName());
		
		System.out.println(p);
	}

	// This method decides what action to take based on the type of
	// file we are looking at
	// public static void doFile_(File f) throws Exception {
	// // If this is a directory, walk each file/dir in that directory
	// if (f.isDirectory()) {
	// String files[] = f.list();
	// for(int i=0; i < files.length; i++) {
	// doFile_(new File(f, files[i]));
	// }
	// }
	//
	// // otherwise, if this is a java file, parse it!
	// else if ( ((f.getName().length()>5) &&
	// f.getName().substring(f.getName().length()-5).equals(".java")) )
	// {
	// System.err.println(f.getAbsolutePath());
	// parseFile(f.getAbsolutePath());
	// }
	// }
	public static void parseFile(String f) {
		try {
			System.err.println("Parsing Filepath: " + f);
			// Create a scanner that reads from the input stream passed to us
			Lexer lexer = new Java8Lexer(new ANTLRFileStream(f));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			// long start = System.currentTimeMillis();
			// tokens.fill(); // load all and check time
			// long stop = System.currentTimeMillis();
			// lexerTime += stop-start;
			// Create a parser that reads from the scanner
			Java8Parser parser = new Java8Parser(tokens);

			parser.addErrorListener(new DiagnosticErrorListener());

			parser.setErrorHandler(new BailErrorStrategy());

			parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

			parser.addParseListener(new AnnListener());

			// start parsing at the compilationUnit rule
			ParserRuleContext t = parser.compilationUnit();

			parser.setBuildParseTree(true);

			t.inspect(parser);
			
			
			
			ParseTreeWalker.DEFAULT.walk(new VarListener(), t);

			System.out.println("Tree: " + t.toStringTree());
			System.out.println("Tree: " + t.toStringTree(parser));
//			System.out.println(""+parser.classBodyDeclaration());
			
//			// invoke the entry point of our parser and generate a DOT image of the tree  
//		    CommonTree tree = (CommonTree)parser.parse().getTree();  
//		    DOTTreeGenerator gen = new DOTTreeGenerator();  
//		    StringTemplate st = gen.toDOT(tree);  
//		    System.out.println(st);  
			
			ParserRuleContext tree = parser.compilationUnit(); // parse
			ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
			MyListener extractor = new MyListener();
			walker.walk(extractor, t); // initiate walk of tree with listener
		} catch (Exception e) {
			System.err.println("parser exception: " + e);
			e.printStackTrace(); // so we can get stack trace
		}
	}

	// private void printDrink(String drinkSentence) {
	// // Get our lexer
	// Java8Lexer lexer = new Java8Lexer(new ANTLRInputStream(drinkSentence));
	//
	// // Get a list of matched tokens
	// CommonTokenStream tokens = new CommonTokenStream(lexer);
	//
	// // Pass the tokens to the parser
	// Java8Parser parser = new Java8Parser(tokens);
	//
	// // Specify our entry point
	// DrinkSentenceContext drinkSentenceContext = parser.drinkSentence();
	//
	// // Walk it and attach our listener
	// ParseTreeWalker walker = new ParseTreeWalker();
	// AntlrDrinkListener listener = new AntlrDrinkListener();
	// walker.walk(listener, drinkSentenceContext);
	// }
	
	
	public  static class MyListener implements ParseTreeListener  {

		@Override
		public void enterEveryRule(ParserRuleContext ctx) {
			// TODO Auto-generated method stub
			System.out.println("-enterEveryRule: ["+ctx.getText()+"] RuleCtx: ["+ctx.getRuleContext().getText()+"] Payload: ["+ctx.getPayload().getText()+"] Childcount: ["+ctx.getChildCount()+"] Depth: ["+ctx.depth()+"]");
		}

		@Override
		public void exitEveryRule(ParserRuleContext ctx) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visitErrorNode(ErrorNode ctx) {
			// TODO Auto-generated method stub
			System.err.println("visitErrorNode: "+ctx.getText());
		}

		@Override
		public void visitTerminal(TerminalNode ctx) {
			// TODO Auto-generated method stub
			
			System.out.println("visitTerminal: ["+ctx+"] Text: ["+ctx.getText()+"] Source Interval: ["+ctx.getSourceInterval()+"] child count: ["+ctx.getChildCount()+"] Line ["+ctx.getSymbol().getLine()+"]");
			
		}

		
	}
	
	public static class AnnListener extends Java8BaseListener {
		public HashMap<String, ComponentFunction> functions;
		private MethodDeclarationContext currentMethod;
		
		public AnnListener() {
			functions = new HashMap<String, ComponentFunction>();
		}
		
		@Override
		public void enterMethodDeclaration(MethodDeclarationContext ctx) {
			System.out.println("<<Method Declaration enter: "
					+ ctx.methodHeader());
			currentMethod = ctx;
		}
		
		@Override
		public void exitMethodDeclaration(MethodDeclarationContext ctx) {
			System.out.println("<<Method Declaration exit: "
					+ ctx.methodHeader().methodDeclarator().getText());
			if(currentMethod == ctx){
				System.out.println("Same Method Context!!!");
			}
			
			
//			String functionName = ctx.methodName().getText();
//			currentFunction = functionName;
//			ComponentFunction function = new ComponentFunction(functionName);
//			functions.put(functionName, function);
		}

		@Override
		public void exitImportDeclaration(ImportDeclarationContext ctx) {
			if(ctx.singleTypeImportDeclaration() != null){
				System.out.println("Single Type Import Declaration exit: "
						+ ctx.singleTypeImportDeclaration().typeName().getText());
			}
			else if(ctx.typeImportOnDemandDeclaration() != null){
				System.out.println("Type Import OnDemand Declaration exit: "
						+ ctx.typeImportOnDemandDeclaration().packageOrTypeName().getText());
			}
			else if(ctx.singleStaticImportDeclaration() != null){
				System.out.println("Single Static Import Declaration exit: "
						+ ctx.singleStaticImportDeclaration().typeName().getText());
			}
			else if(ctx.staticImportOnDemandDeclaration() != null){
				System.out.println("Static Import OnDemand Declaration exit: "
						+ ctx.staticImportOnDemandDeclaration().typeName().getText());
			}
		}

		@Override
		public void enterNormalClassDeclaration(NormalClassDeclarationContext ctx) {
			// enum classes are non-normal
			System.out.println("<Normal Class Declaration enter: " + ctx.Identifier());
		}

		@Override
		public void exitNormalClassDeclaration(NormalClassDeclarationContext ctx) {
			// enum classes are non-normal
			System.out.println("<Normal Class Declaration exit: " + ctx.Identifier());
		}

		@Override
		public void exitMethodInvocation(
				@NotNull Java8Parser.MethodInvocationContext ctx) {
			System.out.println("<<Method invocation exit: "
					+ ctx.methodName().getText());

		}
	}
}
