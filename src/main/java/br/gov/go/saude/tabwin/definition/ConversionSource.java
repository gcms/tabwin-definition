package br.gov.go.saude.tabwin.definition;

/**
 * Represents a conversion source.
 */
public interface ConversionSource {
    CategoryMapping createMapping(Dimension dimension);

    default DimensionConversor createConversor(Dimension dimension) {
        return new DimensionConversor(dimension, createMapping(dimension));
    }
}