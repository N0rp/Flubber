package com.constantiuous.structypus;

import java.io.IOException;

import org.junit.Test;

import com.constantinuous.structypus.ingoing.java.JavaProject;

public class JavaProjectTest {

	@Test
	public void testSimple() throws IOException{
		String folderPath = TestProperties.RES_JAVA;
		JavaProject project = new JavaProject();
		project.parseFolderStructure(folderPath);
		project.printClassDependencies();
	}
	
}
