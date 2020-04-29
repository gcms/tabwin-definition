package br.gov.go.saude.tabwin.definition.cnv;

import br.gov.go.saude.tabwin.definition.Category;
import br.gov.go.saude.tabwin.definition.TabWinDefinitionException;
import br.gov.go.saude.tabwin.definition.TestUtils;
import org.junit.Test;

import br.gov.go.saude.tabwin.definition.CategoryMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CNVTests {
    @Test
    public void testCNVHeader() {
        CNV.CNVHeader header = new CNV.CNVHeader("IDENT.CNV", "Teste", 3, 1, "", true, true);

        assertEquals(1, header.getLength());
        assertEquals(3, header.getCategoriesCount());
        assertEquals("IDENT.CNV", header.getName());
        assertEquals("Teste", header.getDescription());
        assertFalse(header.isLong());
        assertTrue(header.hasOnlyInts());
        assertTrue(header.hasRange());
    }

    @Test
    public void testCNVFindEntryByDescription() throws IOException {
        InputStream is = getClass().getResourceAsStream("/tabwin/CNV/br_municgestor.cnv");
        CNV cnv = new CNVParser().parse("br_municgestor.cnv", is);

        assertNotNull(cnv.findEntryByDescription("330455 Rio de Janeiro"));
    }

    @Test
    public void testMappingProperties() throws IOException {
        InputStream is = getClass().getResourceAsStream("/tabwin/CNV/IDENT.CNV");
        CNV cnv = new CNVParser().parse("IDENT.CNV", is);

        CategoryMapping conv = cnv.createConversor("1");

        assertEquals(cnv, conv.getFile());
        assertEquals(0, conv.getStartIndex());
        assertEquals(1, conv.getValueLength());
        assertEquals(17, conv.getDescriptionLength());
    }

    @Test
    public void testMappingFind() throws IOException {
        InputStream is = getClass().getResourceAsStream("/tabwin/CNV/IDENT.CNV");
        CNV cnv = new CNVParser().parse("IDENT.CNV", is);

        CategoryMapping conv = cnv.createConversor("1");

        assertEquals("Normal", conv.findByValue("1").map(Category::getDescription).get());
        assertEquals("Normal", conv.findByDescription("Normal").map(Category::getDescription).get());
    }


    @Test
    public void testMappingEntries() throws IOException {
        InputStream is = getClass().getResourceAsStream("/tabwin/CNV/IDENT.CNV");
        CNV cnv = new CNVParser().parse("IDENT.CNV", is);

        CategoryMapping conv = cnv.createConversor("1");

        List<Category> categories = conv.getEntries().collect(Collectors.toList());
        assertEquals("Normal", categories.get(0).getDescription());
        assertEquals("Longa permanência", categories.get(1).getDescription());
        assertEquals("Outras/ignorado", categories.get(2).getDescription());
    }

    @Test
    public void testCreateConversor() throws IOException {
        InputStream is = getClass().getResourceAsStream("/tabwin/CNV/IDENT.CNV");
        CNV cnv = new CNVParser().parse("IDENT.CNV", is);

        CategoryMapping conv = cnv.createConversor("1");
        assertNotNull(conv);
    }


    @Test
    public void testRange() throws IOException {
        InputStream is = getClass().getResourceAsStream("/tabwin/CNV/PERM.CNV");
        assertNotNull(is);

        CNV cnv = new CNVParser().parse("PERM.CNV", is);

        for (int i = 8; i <= 14; i++)
            assertEquals("8-14 dias", cnv.findEntryByValue(String.valueOf(i)).getDescription());
        for (int i = 15; i <= 21; i++)
            assertEquals("15-21 dias", cnv.findEntryByValue(String.valueOf(i)).getDescription());
        for (int i = 22; i <= 28; i++)
            assertEquals("22-28 dias", cnv.findEntryByValue(String.valueOf(i)).getDescription());

        assertEquals("29 dias e +", cnv.findEntryByValue(String.valueOf(1000)).getDescription());
    }

    @Test
    public void testMissingCategories() throws IOException {
        InputStream is = getClass().getResourceAsStream("/tabwin/CNV/missing.cnv");

        try {
            new CNVParser().parse("missing.cnv", is);
            assertFalse("Should have thrown TabWinDefinitionException", false);
        } catch (CNVParseException ex) {
            assertTrue("Expected 'missing category', found: " + ex.getMessage(),
                    ex.getMessage().contains("missing category"));
        }
    }

    @Test
    public void testGetName() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/PERM.CNV");

        assertEquals("PERM.CNV", cnv.getFileName());
        assertEquals("PERM", cnv.getName());
    }

    @Test
    public void testRepeatedDescription() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/CBO2002.cnv");

        CNVCategory cat = cnv.findEntryByDescription("Professor de educacao fisica no ensino medio");

        assertEquals(1, cat.getFilter().size());
        assertEquals("232120", cat.getFilter().get(0).getValue());

        Collection<CNVCategory> cats = cnv.findEntriesByDescription("Professor de educacao fisica no ensino medio");
        assertEquals(2, cats.size());
    }

}
