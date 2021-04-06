package br.gov.go.saude.tabwin.definition;

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

    public static TabWinDefinitionException except(String message, RuntimeException ex) {
        return new TabWinDefinitionException(message, ex);
    }


    public static TabWinDefinitionException illegalArgument(String message) {
        return except(message);
    }


    public static TabWinDefinitionException illegalArgument(IllegalArgumentException ex) {
        return new TabWinDefinitionException(ex.getMessage(), ex);
    }

    public static TabWinDefinitionException illegalArgument(String message, IllegalArgumentException ex) {
        return new TabWinDefinitionException(message, ex);
    }
}
