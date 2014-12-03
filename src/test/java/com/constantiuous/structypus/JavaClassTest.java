package com.constantiuous.structypus;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.constantinuous.structypus.ingoing.java.JavaClass;

public class JavaClassTest {
	@Test
	public void testJavaClass() {
		String filePath = TestProperties.RES_JAVA
				+ "com/faa/ImportsSimpleFile.java";
		JavaClass javaClass = new JavaClass();
		try {
			if(!javaClass.parse(filePath)){
//				Assert.fail("Parse Error");
			}
		} catch (IOException e) {
			Assert.fail("IOException");
			e.printStackTrace();
		}
		System.out.println(javaClass);
	}
}
