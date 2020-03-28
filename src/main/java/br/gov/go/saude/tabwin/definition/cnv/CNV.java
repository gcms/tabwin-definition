package br.gov.go.saude.tabwin.definition.cnv;

import br.gov.go.saude.tabwin.definition.*;
import br.gov.go.saude.tabwin.definition.cnv.finder.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class CNV implements ConversionFile {
    public static class CNVHeader {
        private final String name;

        private final String description;

        private final int lines;
        private final int len;
        private final String type;

        private final boolean hasRange;
        private final boolean onlyInts;

        public CNVHeader(String name, String description, final int lines, final int len, final String type, boolean hasRange, boolean onlyInts) {
            this.name = name;
            this.description = description;
            this.lines = lines;
            this.len = len;
            this.type = type;
            this.hasRange = hasRange;
            this.onlyInts = onlyInts;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getNumLines() {
            return lines;
        }

        public int getLength() {
            return len;
        }

        public boolean isLong() {
            return len > 2 || (type != null && type.equals("L"));
        }

        public boolean isRange() {
            return type != null && type.startsWith("F");
        }

        public boolean hasRange() {
            return this.hasRange;
        }

        public boolean hasOnlyInts() {
            return onlyInts;
        }
    }

    private CNVHeader header;

    protected CNVHeader getHeader() {
        return header;
    }

    private CNVCategory[] categories;
    private int descriptionLength;

    private CNVCategoryFinder finder;
    private Map<String, CNVCategory> descriptionIndex;

    protected CNV(CNVHeader header, CNVCategory[] categories) {
        this.header = header;
        this.categories = categories;

        if (this.header.getNumLines() != categories.length)
            throw TabWinDefinitionException.missingCategories(header);

        for (int i = 0; i < categories.length; i++) {
            CNVCategory category = categories[i];

            if (category == null)
                throw TabWinDefinitionException.missingCategories(header, i);

            descriptionLength = Math.max(descriptionLength, category.getDescription().length());
        }

        finder = CNVCategoryFinder.createFinder(header, categories);
        descriptionIndex = Utils.indexBy(categories, CNVCategory::getDescription);
    }

    public Stream<Category> getEntries() {
        return Arrays.stream(categories);
    }

    public Stream<CNVCategory> getCategories() {
        return Arrays.stream(categories);
    }

    public Category findEntryByValue(String value) {
        return finder.findEntry(value);
    }

    public int getValueLength() {
        return header.getLength();
    }

    public int getDescriptionLength() {
        return descriptionLength;
    }

    public Category findEntryByDescription(String description) {
        return descriptionIndex.get(description);
    }

    @Override
    public CategoryMapping createMapping(Dimension dimension) {
        return createConversor(dimension.getSelector());
    }

    public CNVConversor createConversor(String offset) {
        return createConversor(Integer.parseInt(offset) - 1);
    }

    public CNVConversor createConversor(int offset) {
        return new CNVConversor(this, offset);
    }

}
