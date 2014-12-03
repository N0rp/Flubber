package com.constantiuous.structypus;

import japa.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.constantinuous.structypus.ingoing.Importer;
import com.constantinuous.structypus.ingoing.MethodPrinter;
import com.constantiuous.structypus.TestProperties;

public class MethodPrinterTest {

    
	
    @Test
    public void testImportsSimpleFile() {
    	String filePath = TestProperties.RES_JAVA + "com/foo/ImportsSimpleFile.java";
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
