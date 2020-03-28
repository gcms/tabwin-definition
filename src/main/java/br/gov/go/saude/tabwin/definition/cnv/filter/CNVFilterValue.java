package br.gov.go.saude.tabwin.definition.cnv.filter;

import java.util.Objects;

public class CNVFilterValue extends CNVFilter {
    protected String value;

    public CNVFilterValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean checkValue(String value) {
        return this.value.equals(value);
    }

    @Override
    public String getMin() {
        return value;
    }

    @Override
    public String getMax() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CNVFilterValue that = (CNVFilterValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value) + getClass().hashCode();
    }
}