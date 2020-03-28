package br.gov.go.saude.tabwin.definition;

public class Increment extends Variable {

    public Increment(Type type, String description, String field) {
        super(type, description, field);
    }

    @Override
    public String toLineString() {
        return String.format("%s%s, %s", type, description, field);
    }

}