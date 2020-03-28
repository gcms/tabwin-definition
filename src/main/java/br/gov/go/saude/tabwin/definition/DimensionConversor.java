package br.gov.go.saude.tabwin.definition;

import java.util.Optional;
import java.util.stream.Stream;

public class DimensionConversor {
    private final Dimension dimension;
    private final CategoryMapping conversor;

    public DimensionConversor(Dimension dimension, CategoryMapping conversor) {
        this.dimension = dimension;
        this.conversor = conversor;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public CategoryMapping getConversor() {
        return conversor;
    }

    public Optional<? extends Category> extractCategory(DimensionRecord record) {
        String fieldValue = record.read(dimension.getField(), conversor.getStartIndex(), conversor.getValueLength());

        return Optional.ofNullable(fieldValue)
                .map(it -> conversor.findByValue(it).orElse(null));
    }

    public Optional<String> extractDescription(DimensionRecord record) {
        return extractCategory(record).map(Category::getDescription);
    }


    public Stream<? extends Category> getEntries() {
        return conversor.getEntries();
    }

    public Stream<String> getAllDescriptions() {
        return getEntries().map(Category::getDescription);
    }

    public Category get(String value) {
        return conversor.get(value);
    }

    public Optional<? extends Category> findByValue(String value) {
        return conversor.findByValue(value);
    }

    public Optional<? extends Category> findByDescription(String description) {
        return conversor.findByDescription(description);
    }

    public int getDescriptionLength() {
        return conversor.getDescriptionLength();
    }
}
