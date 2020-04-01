package br.gov.go.saude.tabwin.definition.cnv;

import br.gov.go.saude.tabwin.definition.cnv.CNV.CNVHeader;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilter;

import java.util.List;

public class CNVParsingContext {
    private int numLines;
    private int fieldLength;
    private String type;

    protected CNVCategory[] categories;
    private boolean hasRange;
    private boolean hasSubtotals;
    private boolean hasCharOnFilters;
    private String name;
    private String comment;


    public int getCategoriesCount() {
        return numLines;
    }

    public void setCategoriesCount(int numLines) {
        this.numLines = numLines;
        this.categories = new CNVCategory[numLines];
    }

    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHasRange(boolean hasRange) {
        this.hasRange = hasRange;
    }

    public void addLine(String subtotal, String order, String description, List<CNVFilter> filter) {
        if (!subtotal.isEmpty())
            this.hasSubtotals = true;

        int index = Integer.parseInt(order) - 1;
        if (categories[index] == null) {
            categories[index] = new CNVCategory(index, description, filter);
        } else {
            assert categories[index].getDescription().equals(description);
            categories[index].addFilter(filter);
        }

    }

    public CNV build() throws CNVParseException {
        CNVHeader header = new CNVHeader(name, comment, numLines, fieldLength, type, hasRange, !hasCharOnFilters);

        for (int i = 0; i < categories.length; i++) {
            CNVCategory category = categories[i];
            if (category == null) {
                throw CNVParseException.missingCategories(header, i);
            }
        }

        return new CNV(header, categories);
    }

    public void setHasCharOnFilter(boolean hasCharOnFilters) {
        this.hasCharOnFilters = hasCharOnFilters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    private int currentLine;

    public void countLine() {
        currentLine++;
    }

    public int getCurrentLineCount() {
        return currentLine;
    }

}