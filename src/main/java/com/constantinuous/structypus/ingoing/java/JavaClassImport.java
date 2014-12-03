package com.constantinuous.structypus.ingoing.java;

public class JavaClassImport {

	private String name;
	private JavaClass javaClass;
	
	public JavaClassImport(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setJavaClass(JavaClass javaClass){
		this.javaClass = javaClass;
	}
	
	public JavaClass getJavaClass(){
		return this.javaClass;
	}
	
}
