package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.TabWinDefinitionException;
import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilter;

import java.math.BigDecimal;
import java.util.Collection;

public abstract class CNVRangeFinder implements CNVCategoryFinder {

    public static final String CATEGORIES_NOT_ASCENDING_ORDER = "Invalid range categories for CNVBinSearchRangeFinder. Should have ascending order.";
    public static final String RANGE_FILTER_IN_CATEGORIES = "Invalid range categories for CNVBinSearchRangeFinder. Only a single value is allowed by category.";


    protected String getValue(CNVCategory line) {
        Collection<CNVFilter> filters = line.getFilter();
        if (filters.size() != 1)
            throw TabWinDefinitionException.illegalArgument(RANGE_FILTER_IN_CATEGORIES);

        CNVFilter filter = filters.iterator().next();
        if (filter.isRange())
            throw TabWinDefinitionException.illegalArgument(RANGE_FILTER_IN_CATEGORIES);

        return filter.getValue();
    }

    protected BigDecimal toNumber(String value) {
        return new BigDecimal(value);
    }


    protected int compare(BigDecimal a, BigDecimal b) {
        return a.compareTo(b);
    }
}
