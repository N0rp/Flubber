package com.constantiuous.structypus;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Recognizer;
import org.junit.Test;

import com.foo.antlr.Java8Lexer;
import com.foo.antlr.Java8Parser;

public class AntlrTest {
	/**
	 * Abstract syntax tree generated from "Hello word!" should have an unnamed
	 * root node with two children. First child corresponds to salutation token
	 * and second child corresponds to end symbol token.
	 *
	 * Token type constants are defined in generated S001HelloWordParser class.
	 */
//	@Test
//	public void testCorrectExpression() {
//		// compile the expression
//		Java8Compiler compiler = new S001HelloWordCompiler();
//		CommonTree ast = compiler.compile("Hello word!");
//		CommonTree leftChild = ast.getChild(0);
//		CommonTree rightChild = ast.getChild(1);
//
//		// check ast structure
//		assertEquals(S001HelloWordParser.SALUTATION, leftChild.getType());
//		assertEquals(S001HelloWordParser.ENDSYMBOL, rightChild.getType());
//	}

	
	@Test
	public void testExampleField() throws Exception {
		String filePath = TestProperties.RES_JAVA + "com/foo/ImportsSimpleFile.java";
		
		File f = new File(filePath);
		
	    Java8Lexer l = new Java8Lexer(new ANTLRInputStream(new FileInputStream(f)));
	    Java8Parser p = new Java8Parser(new CommonTokenStream(l));
	    p.addErrorListener(new BaseErrorListener() {
	        
	    });
	    
	    System.out.println("Packagename: "+p.packageName());
	}
}
