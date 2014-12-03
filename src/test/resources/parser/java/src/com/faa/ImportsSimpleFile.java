package com.faa;

import com.foo.SimpleFile;

public class ImportsSimpleFile {

	/**
	 * Comment for simple code
	 * @return nothing
	 */
	public String simpleMethod() {
		int foo = 5 + 5;
		return "none "+foo;
	}
	
	/**
	 * Comment for complex code
	 */
	public void complexMethod() {
		SimpleFile simple = new SimpleFile();
		// comment
		simpleMethod();
		
		simple.complexMethod("foo");
		new SimpleFile().simpleMethod();
	}
	
	private class DoesNothing{
		
	}

}
