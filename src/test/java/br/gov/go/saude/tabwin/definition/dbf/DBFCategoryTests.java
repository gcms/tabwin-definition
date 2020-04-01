package br.gov.go.saude.tabwin.definition.dbf;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DBFCategoryTests {
    @Test
    public void testProperties() {
        DBFCategory cat = new DBFCategory("001", "Categoria 1");

        assertEquals("001", cat.getKeyValue());
        assertEquals("Categoria 1", cat.getDescription());

        assertTrue(cat.getFilter().stream().anyMatch( it -> it.checkValue("001")));
        assertTrue(cat.getFilter().stream().allMatch( it -> it.checkValue("001")));
    }

}
