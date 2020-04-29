package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.TabWinDefinitionException;
import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

            Map<String, CNVCategory> currentCategory = new HashMap<>();
            for (CNVFilter filter : category.getFilter()) {
                if (filter.isRange())
                    throw TabWinDefinitionException.illegalArgument(String.format(RANGE_FILTER, filter));

                String value = filter.getValue();
                currentCategory.put(value, category);
            }

            for (Map.Entry<String, CNVCategory> e : currentCategory.entrySet()) {
                if (values.put(e.getKey(), e.getValue()) != null)
                    throw TabWinDefinitionException.illegalArgument(String.format(DUPLICATE_KEYS, categories[i]));
            }


        }
    }

    @Override
    public CNVCategory findEntry(String value) {
        return values.get(value);
    }
}
