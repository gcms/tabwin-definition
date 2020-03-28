package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilter;
import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

public class CNVTreeRangeFinder implements CNVCategoryFinder {
    private final TreeRangeMap<String, CNVCategory> rangeMap = TreeRangeMap.create();

    public CNVTreeRangeFinder(CNVCategory[] lines) {
        for (int i = lines.length - 1; i >= 0; i--) {
            CNVCategory line = lines[i];

            for (CNVFilter filter : line.getFilter())
                rangeMap.put(toRange(filter), line);
        }
    }


    @Override
    public CNVCategory findEntry(String value) {
        return rangeMap.get(value);
    }

    private Range<String> toRange(CNVFilter filter) {
        return Range.closed(filter.getMin(), filter.getMax());
    }
}
