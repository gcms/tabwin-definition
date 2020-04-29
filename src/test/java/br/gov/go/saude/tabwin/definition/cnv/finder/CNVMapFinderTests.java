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

    @Test
    public void testRepeatedValueSameCategory() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/DUPLICADO_OK.CNV");
        CNVMapFinder finder = new CNVMapFinder(cnv.getCategories().toArray(CNVCategory[]::new));

        assertEquals("Ignorado", finder.findEntry("0").getDescription());
        assertEquals("Feminino", finder.findEntry("2").getDescription());
        assertEquals("Feminino", finder.findEntry("3").getDescription());
    }

    @Test(expected = TabWinDefinitionException.class)
    public void testRepeatedValueSameCategoryInvalid() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/DUPLICADO_NOK.CNV");
        new CNVMapFinder(cnv.getCategories().toArray(CNVCategory[]::new));
    }
}
