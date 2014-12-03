package com.constantinuous.structypus.ingoing.java;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaProject {

	private List<JavaClass> classes = new LinkedList<JavaClass>();
	
	private Map<String, JavaClass> fullPackage2Class = new HashMap<String, JavaClass>();

	public void parseFolderStructure(String folderPath) throws IOException {
		File dir = new File(folderPath);
		parseFolder(dir);
		analyseClasses();
	}
	
	public void printClassDependencies(){
		for(JavaClass cl : classes){
			Set<JavaClassImport> imports = cl.getImports();
			for(JavaClassImport imp : imports){
				if(imp.getJavaClass()!= null){
					System.out.println(cl.getName()+" -> "+imp.getJavaClass());
				}else{
					System.out.println(cl.getName()+" -> ?"+imp.getName()+"?");
				}
			}
		}
	}
	
	private void analyseClasses(){
		for(JavaClass cl : classes){
			cl.setAvailablePackages(fullPackage2Class);
		}
	}
	
	private void parseFolder(File dir) throws IOException{
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if(child.isDirectory()){
					parseFolder(child);
				}else{
					parseSingleFile(child);
				}
			}
		} else {
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
			if(!dir.isDirectory()){
				parseSingleFile(dir);
			}
		}
	}
	
	private void parseSingleFile(File file) throws IOException{
		String fileName = file.getName();
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		    if(extension.equals("java")){
		    	JavaClass javaClass = new JavaClass();
		    	javaClass.parse(file.getPath());
		    	classes.add(javaClass);
		    	fullPackage2Class.put(javaClass.getFullPackage(), javaClass);
		    }
		}
		
	}

}
