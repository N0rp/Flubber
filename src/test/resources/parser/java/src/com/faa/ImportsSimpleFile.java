package com.faa;

import com.faa.SimpleFile;

public class ImportsSimpleFile {

	public String simpleMethod() {
		int foo = 5 + 5;
		return "none "+foo;
	}
	
	public void complexMethod() {
		simpleMethod();
	}

}
