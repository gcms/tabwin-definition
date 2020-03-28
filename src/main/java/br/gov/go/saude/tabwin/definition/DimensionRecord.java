package br.gov.go.saude.tabwin.definition;

public interface DimensionRecord {
    String read(String fieldName, int offset, int length);
}
