package br.gov.go.saude.tabwin.definition;

import br.gov.go.saude.tabwin.definition.cnv.CNV;

import java.io.IOException;

public class TabWinDefinitionException extends RuntimeException {

    public TabWinDefinitionException(String message) {
        super(message);
    }

    public TabWinDefinitionException(String message, Exception inner) {
        super(message, inner);
    }

    public static TabWinDefinitionException ioException(String name, IOException io) {
        return new TabWinDefinitionException(String.format("Couldn't read from %s", name), io);
    }


    public static TabWinDefinitionException unsupportedFormat(String name) {
        return except(String.format("Unsupported file format '%s'", name));
    }

    private static TabWinDefinitionException except(String message) {
        return new TabWinDefinitionException(message);
    }

    public static TabWinDefinitionException parseException(String message, Exception ex) {
        return except(message, ex);
    }

    private static TabWinDefinitionException except(String message, Exception ex) {
        return new TabWinDefinitionException(message, ex);
    }

    public static TabWinDefinitionException parseException(String message) {
        return except(message);
    }

    public static TabWinDefinitionException illegalState(String message) {
        return except(message);
    }

    public static TabWinDefinitionException illegalArgument(String message) {
        return except(message);
    }

    public static TabWinDefinitionException missingCategories(CNV.CNVHeader header) {
        return illegalArgument(String.format("Invalid CNV file '%s', missing categories", header.getName()));
    }

    public static TabWinDefinitionException missingCategories(CNV.CNVHeader header, int i) {
        return illegalArgument(String.format("Invalid CNV file '%s', missing category with sort order %d", header.getName(), i));
    }
}
