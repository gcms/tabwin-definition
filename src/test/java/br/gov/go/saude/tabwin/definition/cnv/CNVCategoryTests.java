package br.gov.go.saude.tabwin.definition.cnv;

import br.gov.go.saude.tabwin.definition.TestUtils;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CNVCategoryTests {
    @Test
    public void testCategoryValues() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/SEXO.CNV");

        CNVCategory masculino = (CNVCategory) cnv.findEntryByValue("1");
        assertEquals("Masculino", masculino.getDescription());
        assertEquals(1, masculino.getFilter().size());
        assertEquals("1", masculino.getFilter().iterator().next().toString());
        assertEquals(0, masculino.getOrder());

        CNVCategory ignorado = (CNVCategory) cnv.findEntryByValue("7");
        assertEquals("Ignorado", ignorado.getDescription());
        assertEquals(1, ignorado.getFilter().size());
        assertEquals("0-9", ignorado.getFilter().iterator().next().toString());
        assertEquals(2, ignorado.getOrder());

        assertEquals("2\tFeminino\t2,3", cnv.findEntryByValue("2").toString());
        assertTrue(ignorado.compareTo(masculino) > 0);
    }
}
