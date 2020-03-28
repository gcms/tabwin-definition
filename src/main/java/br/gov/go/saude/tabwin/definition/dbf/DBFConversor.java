package br.gov.go.saude.tabwin.definition.dbf;

import br.gov.go.saude.tabwin.definition.CategoryMapping;
import br.gov.go.saude.tabwin.definition.ConversionFile;
import org.jamel.dbf.structure.DbfField;

import java.util.Optional;
import java.util.stream.Stream;

public class DBFConversor implements CategoryMapping {
    private final DBF dbf;
    private final DbfField descField;
    private final DBFIndex index;


    public DBFConversor(DBF dbf, DbfField descField, DBFIndex index) {
        this.dbf = dbf;
        this.index = index;
        this.descField = descField;
    }

    @Override
    public ConversionFile getFile() {
        return dbf;
    }

    @Override
    public Stream<DBFCategory> getEntries() {
        return index.getEntries(descField.getName());
    }

    @Override
    public Optional<DBFCategory> findByValue(String value) {
        return Optional.ofNullable(index.getEntry(value, descField.getName()));
    }

    @Override
    public Optional<DBFCategory> findByDescription(String description) {
        return getEntries().filter(it -> it.getDescription().equals(description)).findFirst();
    }

    @Override
    public int getStartIndex() {
        return 0;
    }

    @Override
    public int getValueLength() {
        return index.getKeyFieldLength();
    }

    @Override
    public int getDescriptionLength() {
        return descField.getFieldLength();
    }

}