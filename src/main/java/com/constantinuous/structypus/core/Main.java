package com.constantinuous.structypus.core;

import com.constantinuous.structypus.configuration.StructypusApplicationProperties;

/**
 * Entry point for the program. Contains only the main method and command line
 * parsing.
 * 
 */
public class Main {

    static StructypusApplicationProperties properties = new StructypusApplicationProperties();

    /**
     * Main method. <br/>
     * 
     * @param args
     *            . Arguments
     */
    public static void main(String[] args) {
        properties.initialize();
    }

}
