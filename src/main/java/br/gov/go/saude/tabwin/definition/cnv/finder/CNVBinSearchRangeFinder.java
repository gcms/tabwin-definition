package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.TabWinDefinitionException;
import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;

import java.math.BigDecimal;

public class CNVBinSearchRangeFinder extends CNVRangeFinder implements CNVCategoryFinder {
    private CNVCategory[] lines;
    private BigDecimal[] values;

    public CNVBinSearchRangeFinder(CNVCategory[] lines) {
        this.values = new BigDecimal[lines.length];

        values[0] = toNumber(getValue(lines[0]));
        for (int i = 1; i < lines.length; i++) {
            values[i] = toNumber(getValue(lines[i]));

            if (compare(values[i], values[i - 1]) < 0)
                throw TabWinDefinitionException.illegalArgument(CATEGORIES_NOT_ASCENDING_ORDER);
        }

        this.lines = lines;
    }

    public CNVCategory findEntry(String valueAsString) {
        BigDecimal value = toNumber(valueAsString);
        int index = binarySearch(value);
        return lines[index];
    }

    private int binarySearch(BigDecimal value) {
        int result = -1;

        int start = 0;
        int end = values.length - 1;

        while (end >= start) {
            int middle = start + (end - start) / 2;
            result = middle;
            int cmp = compare(value, values[middle]);

            if (cmp == 0) {
                break;
            } else if (cmp < 0) {
                end = middle;
                if (end == start)
                    break;
            } else {
                start = middle + 1;
            }
        }

        return result;
    }


}
