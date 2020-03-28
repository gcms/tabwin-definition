package br.gov.go.saude.tabwin.definition.cnv.filter;

import br.gov.go.saude.tabwin.definition.ConversionFilter;

public abstract class CNVFilter implements ConversionFilter {
    public abstract String getMin();

    public abstract String getMax();

    public boolean isRange() {
        return !getMin().equals(getMax());
    }

    public String getValue() {
        if (!isRange())
            throw new UnsupportedOperationException("Range filter");

        return getMin();
    }
}