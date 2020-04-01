package br.gov.go.saude.tabwin.definition.cnv;

import java.io.IOException;

public class CNVParseException extends IOException {
    public CNVParseException(String message) {
        super(message);
    }

    public CNVParseException(String message, Exception e) {
        super(message, e);
    }

    public static CNVParseException cnvHeader(String line, IllegalStateException ex) {
        return new CNVParseException(String.format("Error parsing CNV header in line '%s'", line), ex);
    }


    public static CNVParseException invalidLine(CNVParsingContext ctx, String line, Exception ex) {
        return new CNVParseException(String.format("Invalid category sort order at line %d: '%s'. Category sort order must be numeric.", ctx.getCurrentLineCount(), line), ex);
    }

    public static CNVParseException invalidCategoryIndex(CNVParsingContext ctx, String line, IndexOutOfBoundsException ex) {
        String message = String.format("Invalid index at line %d: '%s'. Expected at most %d categories as specified in file header.", ctx.getCurrentLineCount(), line, ctx.getCategoriesCount());
        return new CNVParseException(message, ex);
    }


    public static CNVParseException missingCategories(CNV.CNVHeader header) {
        return new CNVParseException(String.format("Invalid CNV file '%s', missing category with order %d", header.getName()));
    }

    public static CNVParseException missingCategories(CNV.CNVHeader header, int i) {
        return new CNVParseException(String.format("Invalid CNV file '%s', missing category with sort order %d.", header.getName(), i));
    }
}
