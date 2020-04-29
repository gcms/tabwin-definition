package br.gov.go.saude.tabwin.definition.cnv;

import br.gov.go.saude.tabwin.definition.*;
import br.gov.go.saude.tabwin.definition.cnv.finder.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Represents a CNV File, i.e. it's header and categories. Initializes a CNVCategoryFinder to
 * allow for searching for a category by value.
 */
public class CNV implements ConversionFile {
    /**
     * Keeps CNV Header (num of entries, field length, type) as well as characteristics
     * identified during parsing that will guide the choice of adequate search strategies.
     */
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

        public int getCategoriesCount() {
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

    @Override
    public String getFileName() {
        return header.getName();
    }

    @Override
    public String getName() {
        return header.getName().replaceFirst("(\\.\\w+)$", "");
    }

    private final CNVHeader header;

    private final CNVCategory[] categories;
    private int descriptionLength;

    private final CNVCategoryFinder finder;
    private final Map<String, CNVCategory> descriptionIndex;

    protected CNV(CNVHeader header, CNVCategory[] categories) {
        this.header = header;
        this.categories = categories;

        assert header.getCategoriesCount() == categories.length;

        for (int i = 0; i < categories.length; i++) {
            CNVCategory category = categories[i];
            assert category != null;

            descriptionLength = Math.max(descriptionLength, category.getDescription().length());
        }

        descriptionLength = Math.min(descriptionLength, 50); // 50 is max len for CNV category desc

        finder = CNVCategoryFinder.createFinder(header, categories);
        descriptionIndex = Utils.indexBy(categories, CNVCategory::getDescription);
    }

    protected CNVHeader getHeader() {
        return header;
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
        return descriptionLength; // Maybe we could use the maximum allowed length of 50
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
