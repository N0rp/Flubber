package com.constantinuous.structypus.ingoing;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.io.IOException;

public class MethodPrinter {
    
    public MethodPrinter(String file) throws ParseException, IOException{
    	// creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream(file);

        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        
        System.out.println("Package: "+cu.getPackage());
        System.out.println("Class: "+cu.getClass());
//        System.out.println("ParentNode: "+cu.getParentNode());
//        System.out.println("ChildrenNode: "+cu.getChildrenNodes());
        
        

        // visit and print the methods names
        System.out.println("----------------");
        System.out.println("Methods");
        new MethodVisitor().visit(cu, null);
        
        System.out.println("----------------");
        System.out.println("Imports");
        new ImportVisitor().visit(cu, null);
        
        System.out.println("----------------");
        System.out.println("Package");
        new PackageVisitor().visit(cu, null);
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes. 
     */
    private static class MethodVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this 
            // CompilationUnit, including inner class methods
            System.out.println(n.getName());
        }
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes. 
     */
    private static class ImportVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(ImportDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this 
            // CompilationUnit, including inner class methods
            System.out.println(n.getName());
        }
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes. 
     */
    private static class PackageVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(PackageDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this 
            // CompilationUnit, including inner class methods
            System.out.println(n.getName());
        }
    }
}