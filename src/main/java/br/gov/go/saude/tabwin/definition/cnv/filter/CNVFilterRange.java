package br.gov.go.saude.tabwin.definition.cnv.filter;

import java.util.Objects;

public class CNVFilterRange extends CNVFilter {
    protected String start;
    protected String end;

    public CNVFilterRange(String start, String end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean checkValue(String value) {
        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

    @Override
    public String getMin() {
        return start;
    }

    @Override
    public String getMax() {
        return end;
    }

    @Override
    public String toString() {
        return String.format("%s-%s", start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CNVFilterRange that = (CNVFilterRange) o;
        return Objects.equals(start, that.start) &&
                Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end) + getClass().hashCode();
    }
}
