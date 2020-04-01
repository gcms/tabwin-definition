package br.gov.go.saude.tabwin.definition;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Associates a dimension variable to a mapping from values to categories.
 * Enable mapping values/description to categories.
 */
public class DimensionConversor {
    private final Dimension dimension;
    private final CategoryMapping mapping;

    public DimensionConversor(Dimension dimension, CategoryMapping mapping) {
        this.dimension = dimension;
        this.mapping = mapping;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public CategoryMapping getMapping() {
        return mapping;
    }

    public Optional<? extends Category> extractCategory(DimensionRecord record) {
        String fieldValue = record.read(dimension.getField(), mapping.getStartIndex(), mapping.getValueLength());

        return Optional.ofNullable(fieldValue)
                .map(it -> mapping.findByValue(it).orElse(null));
    }

    public Optional<String> extractDescription(DimensionRecord record) {
        return extractCategory(record).map(Category::getDescription);
    }


    public Stream<? extends Category> getEntries() {
        return mapping.getEntries();
    }

    public Stream<String> getAllDescriptions() {
        return getEntries().map(Category::getDescription);
    }

    public Category get(String value) {
        return mapping.get(value);
    }

    public Optional<? extends Category> findByValue(String value) {
        return mapping.findByValue(value);
    }

    public Optional<? extends Category> findByDescription(String description) {
        return mapping.findByDescription(description);
    }

    public int getDescriptionLength() {
        return mapping.getDescriptionLength();
    }
}
