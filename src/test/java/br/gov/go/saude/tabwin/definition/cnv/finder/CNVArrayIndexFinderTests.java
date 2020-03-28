package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.TestUtils;
import br.gov.go.saude.tabwin.definition.cnv.CNV;
import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CNVArrayIndexFinderTests {
    @Test
    public void testSimpleSearch() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/SEXO.CNV");
        CNVCategoryFinder finder = new CNVArrayIndexFinder(cnv.getCategories().toArray(CNVCategory[]::new), cnv.getValueLength());


        assertEquals("Masculino", finder.findEntry("1").getDescription());
        assertEquals("Feminino", finder.findEntry("2").getDescription());
        assertEquals("Feminino", finder.findEntry("3").getDescription());

        for (int i = 4; i < 10; i++)
            assertEquals("Ignorado", finder.findEntry("" + i).getDescription());

        assertNull(finder.findEntry(""));
    }
}
