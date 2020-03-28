package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.cnv.CNV;
import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;

public interface CNVCategoryFinder {
    CNVCategory findEntry(String value);

    static CNVCategoryFinder createFinder(CNV.CNVHeader header, CNVCategory[] lines) {
        if (header.isRange() && !header.hasRange()) {
            return new CNVBinSearchRangeFinder(lines);
        } else if (header.isLong() && !header.hasRange()) {
            return new CNVMapFinder(lines);
        } else if (!header.isLong() && header.hasOnlyInts()) {
            return new CNVArrayIndexFinder(lines, header.getLength());
        } else if (header.hasRange()) {
            return new CNVTreeRangeFinder(lines);
        }

        return new CNVSeqScanFinder(lines);
    }
}
