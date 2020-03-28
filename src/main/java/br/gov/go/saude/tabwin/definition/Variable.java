package br.gov.go.saude.tabwin.definition;

public abstract class Variable {
    public enum Type {
        I, E, G, L, S, D, X, T, C
    }

    protected final Type type;
    protected final String description;
    protected final String field;

    public Variable(Type type, String description, String field) {
        this.type = type;
        this.description = description;
        this.field = field.toUpperCase();
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getField() {
        return field;
    }

    public abstract String toLineString();

    @Override
    public String toString() {
        return String.format("%s (%s)", description, field);
    }

}