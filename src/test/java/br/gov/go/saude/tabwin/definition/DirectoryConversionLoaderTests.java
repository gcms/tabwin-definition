package br.gov.go.saude.tabwin.definition;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class DirectoryConversionLoaderTests {

    @Test
    public void testLoadUnsupportedFormat() throws URISyntaxException {
        ConversionLoader loader = new DirectoryConversionLoader(TestUtils.getTabWinDir());

        try {
            loader.loadFile("CNV/unsupported.csv");
            assertFalse("Should have thrown TabWinDefinitionException", false);
        } catch (TabWinDefinitionException ex) {
            assertTrue(ex.getMessage().startsWith("Unsupported file format"));
        }

    }

    @Test
    public void testLoadInexistentFile() throws URISyntaxException {
        ConversionLoader loader = new DirectoryConversionLoader(TestUtils.getTabWinDir());

        try {
            loader.loadFile("teste.cnv");
            assertFalse("Should have thrown TabWinDefinitionException", false);
        } catch (TabWinDefinitionException ex) {
            assertTrue(ex.getCause() instanceof FileNotFoundException);
        }

    }
}
