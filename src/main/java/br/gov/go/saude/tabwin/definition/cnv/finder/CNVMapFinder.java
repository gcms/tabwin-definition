package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.TabWinDefinitionException;
import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilter;

import java.util.HashMap;
import java.util.Map;

public class CNVMapFinder implements CNVCategoryFinder {
    public static final String RANGE_FILTER = "Range filter: %s. CNVMapFinder doesn't support range filters.";
    private static final String DUPLICATE_KEYS = "Duplicate values: %s. CNVMapFinder doesn't support repeated keys.";
    Map<String, CNVCategory> values;

    public CNVMapFinder(CNVCategory[] categories) {
        this(categories, new HashMap<>());
    }

    public CNVMapFinder(CNVCategory[] categories, Map<String, CNVCategory> impl) {
        this.values = impl;
        for (int i = categories.length - 1; i >= 0; i--) {
            CNVCategory category = categories[i];

            for (CNVFilter filter : category.getFilter()) {
                if (filter.isRange())
                    throw TabWinDefinitionException.illegalArgument(String.format(RANGE_FILTER, filter));

                String value = filter.getValue();
                if (values.put(value, category) != null)
                    throw TabWinDefinitionException.illegalArgument(String.format(DUPLICATE_KEYS, categories[i]));
            }
        }
    }

    @Override
    public CNVCategory findEntry(String value) {
        return values.get(value);
    }
}
