package com.constantiuous.structypus;

import java.util.Stack;

import com.foo.antlr.Java8BaseListener;
import com.foo.antlr.Java8Parser.VariableDeclaratorContext;

public class VarListener extends Java8BaseListener {

    private Stack scopes;

    public VarListener() {
        scopes = new Stack();
        
    } 

    @Override
    public void enterVariableDeclarator(VariableDeclaratorContext ctx) {
        System.out.println("enterVariableDeclarator: "+ctx.variableDeclaratorId().getText());
    }
}
