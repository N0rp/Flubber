package com.constantinuous.structypus.ingoing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Importer {
	
	private final String resources = "StructypusLogger.java";

    public Importer(String path) throws FileNotFoundException {

        File file = new File(path);
        if (file.isDirectory()) {
            System.out.println("File is dir: " + file.getAbsolutePath());
        } else {
            Scanner input = new Scanner(file);

            while (input.hasNext()) {
                String nextToken = input.next();
                // or to process line by line
                String nextLine = input.nextLine();
            }

            input.close();
        }
    }

}
