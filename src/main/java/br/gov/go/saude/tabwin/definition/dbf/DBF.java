package br.gov.go.saude.tabwin.definition.dbf;

import br.gov.go.saude.tabwin.definition.ConversionFile;
import br.gov.go.saude.tabwin.definition.Dimension;
import br.gov.go.saude.tabwin.definition.TabWinDefinitionException;
import org.jamel.dbf.DbfReader;
import org.jamel.dbf.structure.DbfHeader;
import org.jamel.dbf.structure.DbfRow;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DBF implements ConversionFile {
    private final DbfHeader header;
    private final List<DbfRow> rows;

    private final Map<Integer, DBFIndex> indexes;
    private String fileName;


    public DBF(String fileName, InputStream is, Charset charset) {
        this.fileName = fileName;
        try (DbfReader reader = new DbfReader(is, charset)) {
            this.header = reader.getHeader();
            this.rows = readRows(reader);
        }

        this.indexes = new ConcurrentHashMap<>();
    }

    private static List<DbfRow> readRows(DbfReader reader) {
        List<DbfRow> rows = new ArrayList<>();

        DbfRow row = reader.nextRow();
        while (row != null) {
            rows.add(row);
            row = reader.nextRow();
        }

        return rows;
    }

    private DBFIndex getIndex(int index) {
        try {
            return indexes.computeIfAbsent(Math.max(0, index),
                    (i) -> new DBFIndex(header.getField(i), rows.stream()));
        } catch (IllegalArgumentException ex) {
            throw TabWinDefinitionException.illegalArgument(String.format("Error indexing field %d in %s", index, fileName), ex);
        }
    }

    @Override
    public DBFConversor createMapping(Dimension dimension) {
        return createConversor(dimension.getField(), dimension.getSelector());
    }

    public DBFConversor createConversor(String keyFieldName, String descFieldName) {
        int keyFieldIndex = header.getFieldIndex(keyFieldName);
        return createConversor(keyFieldIndex, descFieldName);
    }

    public DBFConversor createConversor(int fieldIndex, String descFieldName) {
        return new DBFConversor(this, header.getField(descFieldName), getIndex(fieldIndex));
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getName() {
        return fileName.replaceFirst("(\\.\\w+$)", "");
    }
}