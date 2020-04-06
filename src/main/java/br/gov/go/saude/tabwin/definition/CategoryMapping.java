package br.gov.go.saude.tabwin.definition;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a mapping of values to categories built from a ConversionFile.
 * Multiple CategoryMapping s can reuse the same ConversionFile, using different
 * joining fields.
 */
public interface CategoryMapping {
    ConversionFile getFile();

    Stream<? extends Category> getEntries();

    default Category get(String value) throws NoSuchElementException {
        return findByValue(value)
                .orElseThrow(() -> new NoSuchElementException(String.format("Couldn't find entry for value '%s'", value)));
    }

    Optional<? extends Category> findByValue(String value);

    Optional<? extends Category> findByDescription(String description);

    int getStartIndex();

    int getValueLength();

    int getDescriptionLength();
}