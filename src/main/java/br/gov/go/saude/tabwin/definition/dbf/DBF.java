package br.gov.go.saude.tabwin.definition.dbf;

import br.gov.go.saude.tabwin.definition.ConversionFile;
import br.gov.go.saude.tabwin.definition.Dimension;
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


    public DBF(InputStream is, Charset charset) {
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
        return indexes.computeIfAbsent(index < 0 ? 0 : index,
                (i) -> new DBFIndex(header.getField(i), rows.stream()));
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

}