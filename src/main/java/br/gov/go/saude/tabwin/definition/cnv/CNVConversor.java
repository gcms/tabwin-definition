package br.gov.go.saude.tabwin.definition.cnv;

import java.util.Optional;
import java.util.stream.Stream;

import br.gov.go.saude.tabwin.definition.Category;
import br.gov.go.saude.tabwin.definition.CategoryMapping;
import br.gov.go.saude.tabwin.definition.ConversionFile;

public class CNVConversor implements CategoryMapping {
    private CNV cnv;
    private int index;

    public CNVConversor(CNV cnv, int index) {
        this.cnv = cnv;
        this.index = index;
    }

    @Override
    public ConversionFile getFile() {
        return cnv;
    }

    @Override
    public Stream<Category> getEntries() {
        return cnv.getEntries();
    }

    @Override
    public Optional<Category> findByValue(String value) {
        return Optional.ofNullable(cnv.findEntryByValue(value));
    }

    @Override
    public Optional<Category> findByDescription(String description) {
        return Optional.ofNullable(cnv.findEntryByDescription(description));
    }

    public int getStartIndex() {
        return index;
    }

    @Override
    public int getValueLength() {
        return cnv.getValueLength();
    }

    @Override
    public int getDescriptionLength() {
        return cnv.getDescriptionLength();
    }

}