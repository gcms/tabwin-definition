package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.TabWinDefinitionException;
import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilter;

import java.util.HashMap;
import java.util.Map;

public class CNVMapFinder implements CNVCategoryFinder {
    public static final String RANGE_FILTER = "Range filter: %s. CNVMapFinder doesn't support range filters.";
    private static final String DUPLICATE_KEYS = "Duplicate values: %s. CNVMapFinder doesn't support repeated keys.";

    private final Map<String, CNVCategory> values;

    public CNVMapFinder(CNVCategory[] categories) {
        this(categories, new HashMap<>(categories.length));
    }

    public CNVMapFinder(CNVCategory[] categories, Map<String, CNVCategory> impl) {
        this.values = impl;
        for (int i = categories.length - 1; i >= 0; i--) {
            Map<String, CNVCategory> categoryValues = getCategoryValuesMap(categories[i]);

            for (Map.Entry<String, CNVCategory> e : categoryValues.entrySet()) {
                if (values.put(e.getKey(), e.getValue()) != null) {
                    throw TabWinDefinitionException.illegalArgument(String.format(DUPLICATE_KEYS, categories[i]));
                }
            }
        }
    }

    private Map<String, CNVCategory> getCategoryValuesMap(CNVCategory category) {
        Map<String, CNVCategory> currentCategoryValues = new HashMap<>();
        for (CNVFilter filter : category.getFilter()) {
            if (filter.isRange())
                throw TabWinDefinitionException.illegalArgument(String.format(RANGE_FILTER, filter));

            String value = filter.getValue();
            currentCategoryValues.put(value, category);
        }
        return currentCategoryValues;
    }

    @Override
    public CNVCategory findEntry(String value) {
        return values.get(value);
    }
}
