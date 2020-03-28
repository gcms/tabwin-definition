package br.gov.go.saude.tabwin.definition;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static br.gov.go.saude.tabwin.definition.TestUtils.getTabWinDir;
import static br.gov.go.saude.tabwin.definition.TestUtils.loadRD2008;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DEFParserTests {
    @Test
    public void parseDefFile() throws URISyntaxException, IOException {
        DEFParser parser = new DEFParser();

        DEF def = parser.parse(new File(getTabWinDir(), "RD2008.DEF"), Charset.forName("Windows-1252"));
        assertNotNull(def);
    }

    @Test
    public void parseBasicProperties() throws IOException, URISyntaxException {
        DEF def = loadRD2008();

        assertEquals("Movimento de AIH - Arquivos Reduzidos", def.getDescription());
        assertEquals("DADOS\\RD*.DBC", def.getFilePattern());
    }


    @Test
    public void checkFilterVariableIsLoaded() throws URISyntaxException, IOException {
        DEF def = loadRD2008();

        Dimension sexo = def.getDimension("Sexo");
        assertEquals(Variable.Type.X, sexo.getType());
        assertEquals("Sexo", sexo.getDescription());
        assertEquals("1", sexo.getSelector());
        assertEquals("SEXO", sexo.getField());
        assertEquals("CNV\\SEXO.CNV", sexo.getConversionFile());
    }


}
