package br.gov.go.saude.tabwin.definition;

/**
 * Represents a conversion file, which can be either a CNV or a DBF.
 */
public interface ConversionFile {
    CategoryMapping createMapping(Dimension dimension);

    default DimensionConversor createConversor(Dimension dimension) {
        return new DimensionConversor(dimension, createMapping(dimension));
    }
}