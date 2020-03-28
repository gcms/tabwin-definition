package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.TabWinDefinitionException;
import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;
import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

import java.math.BigDecimal;

public class CNVTreeRangeRangeFinder extends CNVRangeFinder implements CNVCategoryFinder {
    private final TreeRangeMap<BigDecimal, CNVCategory> rangeMap = TreeRangeMap.create();

    public CNVTreeRangeRangeFinder(CNVCategory[] lines) {
        BigDecimal val = toNumber(getValue(lines[0]));
        Range<BigDecimal> range = Range.closed(val, val);

        rangeMap.put(range, lines[0]);

        for (int i = 1; i < lines.length; i++) {
            BigDecimal prev = val;
            val = toNumber(getValue(lines[i]));

            if (prev.compareTo(val) > 0)
                throw TabWinDefinitionException.illegalArgument(CATEGORIES_NOT_ASCENDING_ORDER);

            range = Range.openClosed(prev, val);
            rangeMap.put(range, lines[i]);
        }
    }

    @Override
    public CNVCategory findEntry(String value) {
        return rangeMap.get(toNumber(value));
    }


}
