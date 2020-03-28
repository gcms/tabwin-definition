package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.TabWinDefinitionException;
import br.gov.go.saude.tabwin.definition.TestUtils;
import br.gov.go.saude.tabwin.definition.cnv.CNV;
import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CNVMapFinderTests {
    @Test
    public void testSimple() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/AgravoNot.CNV");

        CNVCategoryFinder finder = new CNVMapFinder(cnv.getCategories().toArray(CNVCategory[]::new));
        assertEquals("A21.- Tularemia", finder.findEntry("A21 ").getDescription());
        assertEquals("B03   Variola", finder.findEntry("B03 ").getDescription());
        assertEquals("B03   Variola", finder.findEntry("B030").getDescription());
    }


    @Test(expected = TabWinDefinitionException.class)
    public void testInvalidInput() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/SEXO.CNV");
        new CNVMapFinder(cnv.getCategories().toArray(CNVCategory[]::new));
    }
}
