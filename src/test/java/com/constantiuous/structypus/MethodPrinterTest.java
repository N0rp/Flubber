package com.constantiuous.structypus;

import japa.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.constantinuous.structypus.ingoing.Importer;
import com.constantinuous.structypus.ingoing.MethodPrinter;

public class MethodPrinterTest {

    String resources = "src/test/resources/";
    String javares = resources + "parser/java/src/";
	
    @Test
    public void testImportsSimpleFile() {
    	String filePath = javares + "com/foo/ImportsSimpleFile.java";
        try {
            MethodPrinter printer = new MethodPrinter(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assert.fail("We should not get this error for the path: " + filePath);
        } catch (ParseException e) {
			e.printStackTrace();
			Assert.fail("We should not get this error for the path: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("We should not get this error for the path: " + filePath);
		}
    }

}
