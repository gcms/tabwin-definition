package br.gov.go.saude.tabwin.definition;

public interface ConversionFile {
    CategoryMapping createMapping(Dimension dimension);

    default DimensionConversor createConversor(Dimension dimension) {
        return new DimensionConversor(dimension, createMapping(dimension));
    }
}