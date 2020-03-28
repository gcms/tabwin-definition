package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;

import java.util.Arrays;

public class CNVSeqScanFinder implements CNVCategoryFinder {
    private final CNVCategory[] lines;

    public CNVSeqScanFinder(CNVCategory[] lines) {
        this.lines = lines;
    }

    @Override
    public CNVCategory findEntry(String value) {
        return Arrays.stream(lines)
                .filter(it -> it.includes(value))
                .findFirst()
                .orElse(null);
    }
}
