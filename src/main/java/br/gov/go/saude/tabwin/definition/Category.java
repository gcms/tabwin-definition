package br.gov.go.saude.tabwin.definition;

import java.util.Collection;

public interface Category {

    String getDescription();
    Collection<? extends ConversionFilter> getFilter();
}