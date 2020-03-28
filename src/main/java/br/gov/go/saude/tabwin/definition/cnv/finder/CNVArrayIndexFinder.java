package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilter;

public class CNVArrayIndexFinder implements CNVCategoryFinder {
    private CNVCategory[] array;

    public CNVArrayIndexFinder(CNVCategory[] categories, int len) {
        this.array = new CNVCategory[(int) Math.pow(10, len)];

        for (int i = categories.length - 1; i >= 0; i--) {
            CNVCategory category = categories[i];

            for (CNVFilter filter : category.getFilter()) {
                setValue(category, filter);
            }
        }
    }

    private void setValue(CNVCategory category, CNVFilter range) {
        int start = Integer.parseInt(range.getMin());
        int end = Integer.parseInt(range.getMax());

        for (int i = start; i <= end; i++)
            setValue(category, i);
    }


    private void setValue(CNVCategory category, int index) {
        array[index] = category;
    }


    @Override
    public CNVCategory findEntry(String value) {
        try {
            return array[Integer.parseInt(value)];
        } catch (NumberFormatException ex) {
            return null;
        }

    }
}
