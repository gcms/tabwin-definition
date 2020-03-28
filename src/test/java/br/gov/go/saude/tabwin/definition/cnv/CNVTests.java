package br.gov.go.saude.tabwin.definition.cnv;

import org.junit.Test;

import br.gov.go.saude.tabwin.definition.CategoryMapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

public class CNVTests {
    @Test
    public void testCNV() throws IOException {
        InputStream is = getClass().getResourceAsStream("/tabwin/CNV/br_municgestor.cnv");
        CNV cnv = new CNVParser().parse("br_municgestor.cnv", is);

        assertNotNull(cnv.findEntryByDescription("330455 Rio de Janeiro"));
    }

    @Test
    public void testMapping() throws IOException {
        InputStream is = getClass().getResourceAsStream("/tabwin/CNV/IDENT.CNV");
        CNV cnv = new CNVParser().parse("IDENT.CNV", is);

        CategoryMapping conv = cnv.createConversor("1");
        assertNotNull(conv.findByValue("1"));
    }

    @Test
    public void testRange() throws IOException {
        InputStream ano = getClass().getResourceAsStream("/tabwin/CNV/PERM.CNV");
        assertNotNull(ano);

        CNV cnv = new CNVParser().parse("PERM.CNV", ano);

        for (int i = 8; i <= 14; i++)
            assertEquals("8-14 dias", cnv.findEntryByValue(String.valueOf(i)).getDescription());
        for (int i = 15; i <= 21; i++)
            assertEquals("15-21 dias", cnv.findEntryByValue(String.valueOf(i)).getDescription());
        for (int i = 22; i <= 28; i++)
            assertEquals("22-28 dias", cnv.findEntryByValue(String.valueOf(i)).getDescription());

        assertEquals("29 dias e +", cnv.findEntryByValue(String.valueOf(1000)).getDescription());
    }



}
