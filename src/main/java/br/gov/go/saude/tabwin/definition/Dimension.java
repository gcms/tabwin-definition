package br.gov.go.saude.tabwin.definition;

public class Dimension extends Variable implements Comparable<Dimension> {
    private final String selector;

    private final String conversionFile;

    public Dimension(Type type, String description, String field, String selector, String conversionFile) {
        super(type, description, field);
        this.selector = selector;
        this.conversionFile = conversionFile;
    }

    public String getSelector() {
        return selector;
    }


    public String getConversionFile() {
        return conversionFile;
    }

    public String toLineString() {
        return String.format("%s%s, %s, %s, %s", getType(), getDescription(), getField(), getSelector(), conversionFile);
    }

    @Override
    public String toString() {
        return toLineString();
    }

    @Override
    public int compareTo(Dimension o) {
        if (this.getDescription().equals(o.getDescription()))
            return this.getType().compareTo(o.getType());

        return this.getDescription().compareTo(o.getDescription());
    }
}
