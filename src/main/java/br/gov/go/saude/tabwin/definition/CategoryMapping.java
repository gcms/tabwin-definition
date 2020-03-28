package br.gov.go.saude.tabwin.definition;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

public interface CategoryMapping {
    ConversionFile getFile();

    Stream<? extends Category> getEntries();

    default Category get(String value) {
        return findByValue(value)
                .orElseThrow(() -> new NoSuchElementException(String.format("Couldn't find entry for value '%s'", value)));
    }

    Optional<? extends Category> findByValue(String value);

    Optional<? extends Category> findByDescription(String description);

    int getStartIndex();

    int getValueLength();

    int getDescriptionLength();
}