package br.gov.go.saude.tabwin.definition;

/**
 * Represents a conversion file, which can be either a CNV or a DBF.
 */
public interface ConversionFile extends ConversionSource {
    String getFileName();
    String getName();
}