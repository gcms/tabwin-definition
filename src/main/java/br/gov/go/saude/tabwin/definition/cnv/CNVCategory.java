package br.gov.go.saude.tabwin.definition.cnv;

import br.gov.go.saude.tabwin.definition.Category;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilter;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CNVCategory implements Category, Comparable<CNVCategory> {
    private int order;
    private String description;
    private List<CNVFilter> filter;

    public CNVCategory(int order, String description, List<CNVFilter> filter) {
        this.order = order;
        this.description = description;
        this.filter = new ArrayList<>(filter);
    }

    public int getOrder() {
        return order;
    }

    public String getDescription() {
        return description;
    }

    public boolean includes(String value) {
        return filter.stream().anyMatch(it -> it.checkValue(value));
    }

    @Override
    public List<CNVFilter> getFilter() {
        return Collections.unmodifiableList(filter);
    }

    protected void addFilter(List<CNVFilter> filters) {
        filter.addAll(filters);
    }

    @Override
    public int compareTo(CNVCategory o) {
        return this.order - o.order;
    }

    @Override
    public String toString() {
        return String.format("%s\t%s\t%s", order + 1, description, Joiner.on(",").join(filter));
    }
}
