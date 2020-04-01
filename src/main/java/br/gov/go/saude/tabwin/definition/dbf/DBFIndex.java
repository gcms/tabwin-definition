package br.gov.go.saude.tabwin.definition.dbf;

import br.gov.go.saude.tabwin.definition.Utils;
import org.jamel.dbf.structure.DbfField;
import org.jamel.dbf.structure.DbfRow;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Represents the categories of a DBF file, indexed by a given key field.
 * The same DBFIndex can be reused for multiple mappings.
 */
public class DBFIndex {

    private final Map<String, DbfRow> entries;
    private final DbfField keyField;

    public DBFIndex(final DbfField keyField, final Stream<DbfRow> rows) {
        this.keyField = keyField;
        this.entries = Utils.indexBy(rows, it -> it.getString(keyField.getName()));
    }

    public DbfRow get(String key) {
        return entries.get(key);
    }


    public DBFCategory getEntry(String keyValue, String descField) {
        return buildEntry(keyValue, get(keyValue).getString(descField));
    }

    public Stream<DBFCategory> getEntries(String descField) {
        return entries.entrySet().stream()
                .map(it -> buildEntry(it.getKey(), it.getValue(), descField));
    }

    private DBFCategory buildEntry(String keyValue, DbfRow row, String descField) {
        return buildEntry(keyValue, row.getString(descField));
    }

    private DBFCategory buildEntry(String keyValue, String description) {
        return new DBFCategory(keyValue, description);
    }

    public int getKeyFieldLength() {
        return keyField.getFieldLength();
    }

}
