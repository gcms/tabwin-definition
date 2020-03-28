package br.gov.go.saude.tabwin.definition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Stream;

public class DEF {
    private final File path;

    private final String description;
    private final String filePattern;

    private final List<Increment> increments;
    private final List<Dimension> dimensions;

    private ConversionLoader conversionLoader;

    public DEF(File path, String description, String filePattern,
               List<Increment> increments,
               List<Dimension> dimensions,
               ConversionLoader conversionLoader) {
        this.path = path;
        this.description = description;
        this.filePattern = filePattern;
        this.increments = increments;
        this.dimensions = dimensions;
        this.conversionLoader = conversionLoader;
    }

    public File getPath() {
        return path;
    }

    public CategoryMapping getMapping(Dimension dimension) {
        return conversionLoader.loadMapping(dimension);
    }

    public DimensionConversor getConversor(Dimension dimension) {
        return conversionLoader.loadConversor(dimension);
    }


    public String getName() {
        return path.getName();
    }

    public String getDescription() {
        return description;
    }

    public File getDefDir() {
        return getPath().getParentFile();
    }

    public File getDataDir() throws FileNotFoundException {
        String dirPath = getFilePattern().replaceFirst("[\\\\/][^\\\\/]*$", "");

        return Utils.resolveCaseInsensitivePath(getDefDir(), dirPath);
    }

    public String getFilePattern() {
        return filePattern;
    }

    public Stream<Dimension> getDimensions() {
        return dimensions.stream();
    }

    public Stream<Dimension> getColumns() {
        return dimensions.stream()
                .filter(it -> it.type == Variable.Type.C || it.type == Variable.Type.X || it.type == Variable.Type.D || it.type == Variable.Type.T)
                .filter(Utils.distinctByKey(Variable::getDescription));
    }

    public Stream<Dimension> getLines() {
        return dimensions.stream()
                .filter(it -> it.type == Variable.Type.L || it.type == Variable.Type.X || it.type == Variable.Type.D || it.type == Variable.Type.T)
                .filter(Utils.distinctByKey(Variable::getDescription));
    }

    public Stream<Dimension> getSelections() {
        return dimensions.stream()
                .filter(it -> it.type == Variable.Type.S || it.type == Variable.Type.X)
                .filter(Utils.distinctByKey(Variable::getDescription));
    }

    public Stream<Dimension> getFilters() {
        return dimensions.stream()
                .filter(Utils.distinctByKey(Variable::getDescription));
    }

    public Optional<Dimension> findDimension(String varName) {
        return this.dimensions.stream()
                .filter(it -> it.field.equals(varName) || it.description.equals(varName))
                .findFirst();
    }


    public Dimension getDimension(String varName) {
        return findDimension(varName)
                .orElseThrow(() -> new NoSuchElementException(String.format("Dimension named '%s' not found in %s", varName, getName())));
    }

    public Stream<Increment> getIncrements() {
        return increments.stream();
    }


    public Optional<Increment> findIncrement(String varName) {
        return this.increments.stream()
                .filter(it -> it.field.equals(varName) || it.description.equals(varName))
                .findFirst();
    }

    public Increment getIncrement(String varName) {
        return findIncrement(varName)
                .orElseThrow(() -> new NoSuchElementException(String.format("Increment named '%s' not found in %s", varName, getName())));
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        pw.printf(";%s%n", getDescription());
        pw.printf("A%s%n", getFilePattern());

        for (Variable v : increments)
            pw.println(v.toLineString());

        for (Variable v : dimensions)
            pw.println(v.toLineString());

        pw.flush();

        return sw.getBuffer().toString();
    }
}