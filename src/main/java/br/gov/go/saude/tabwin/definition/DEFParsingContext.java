package br.gov.go.saude.tabwin.definition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class DEFParsingContext {
    private final File file;
    private final ConversionLoader loader;

    private String description;
    private String filePattern;


    private List<Increment> increments = new ArrayList<>();
    private SortedSet<Dimension> dimensions = new TreeSet<>();

    public DEFParsingContext(File file, ConversionLoader loader) {
        this.file = file;
        this.loader = loader;
    }


    protected void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    protected void setFilePattern(String pattern) {
        this.filePattern = pattern;
    }


    void addIncrement(Variable.Type type, String description, String field) {
        increments.add(new Increment(type, description, field));
    }

    void addDimension(Variable.Type type, String description, String field, String selector, String cnvFilePath) {
        dimensions.add(new Dimension(type, description, field, selector, cnvFilePath));
    }

    public DEF build() {
        return new DEF(file, description, filePattern, increments, new ArrayList<>(dimensions), loader);
    }

}
