package com.github.javaparser_new.printer;

import com.github.javaparser_new.ast.Node;
import com.github.javaparser_new.printer.configuration.PrinterConfiguration;

/**
 * Printer interface defines the API for a printer.
 * A printer outputs the AST as formatted Java source code. 
 *
 */
public interface Printer {

    String print(Node node);

    Printer setConfiguration(PrinterConfiguration configuration);
    
    PrinterConfiguration getConfiguration();

}