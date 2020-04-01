package br.gov.go.saude.tabwin.definition.dbf;

import br.gov.go.saude.tabwin.definition.Category;
import br.gov.go.saude.tabwin.definition.ConversionFilter;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class DBFCategory implements Category {
    private final String keyValue;
    private final String descValue;

    private final Collection<ConversionFilter> filters;

    public DBFCategory(String keyValue, String descValue) {
        this.keyValue = keyValue;
        this.descValue = descValue;

        this.filters = Collections.singleton(value -> value.equals(keyValue));
    }


    public String getKeyValue() {
        return keyValue;
    }

    @Override
    public String getDescription() {
        return descValue;
    }

    @Override
    public Collection<? extends ConversionFilter> getFilter() {
        return filters;
    }

    @Override
    public String toString() {
        return String.format("%s %s", keyValue, descValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBFCategory that = (DBFCategory) o;
        return Objects.equals(keyValue, that.keyValue) &&
                Objects.equals(descValue, that.descValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyValue, descValue);
    }

}
