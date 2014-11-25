package com.constantiuous.structypus;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Test;

import com.constantinuous.structypus.ingoing.Importer;

public class ImporterTest {

    @Test
    public void testSimpleJava() {
        String path = "src/test/resources/parser/java";
        try {
            Importer imp = new Importer(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assert.fail("We should not get this error for the path: " + path);
        }
    }

}
