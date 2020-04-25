package br.gov.go.saude.tabwin.definition;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static br.gov.go.saude.tabwin.definition.TestUtils.loadRD2008;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DEFTests {
    @Test
    public void checkDefAndDataDirs() throws URISyntaxException, IOException {
        DEF def = loadRD2008();

        assertEquals("tabwin", def.getDefDir().getName());
        assertEquals("Dados", def.getDataDir().getName());
    }

    @Test
    public void lineVariablesFirst() throws URISyntaxException, IOException {
        DEF def = loadRD2008();

        Dimension var = def.getDimension("Grupo de Procedimentos");
        assertEquals(Variable.Type.L, var.getType());
    }

    @Test
    public void loadMapping() throws URISyntaxException, IOException {
        DEF def = loadRD2008();

        Dimension var = def.getDimension("Tipo de AIH");
        CategoryMapping conv = def.getMapping(var);
        assertEquals("Normal", conv.get("1").getDescription());
    }

    @Test
    public void loadConversion() throws URISyntaxException, IOException {
        DEF def = loadRD2008();

        Dimension var = def.getDimension("Tipo de AIH");
        DimensionConversor conv = def.getConversor(var);
        assertEquals("Normal", conv.get("1").getDescription());
    }

    @Test
    public void getVariables() throws IOException, URISyntaxException {
        DEF def = TestUtils.parseDEF("/tabwin/SIMPLE.DEF");

        assertEquals(2, def.getIncrements().count());

        assertEquals(5, def.getDimensions().count());
        assertEquals(4, def.getLines().count()); // 2L + 2X
        assertEquals(3, def.getColumns().count()); // 3X
        assertEquals(4, def.getFilters().count());

    }

    @Test
    public void findIncrement() throws IOException, URISyntaxException {
        DEF def = TestUtils.parseDEF("/tabwin/SIMPLE.DEF");

        assertTrue(def.findIncrement("Valor Total").isPresent());
        assertTrue(def.findIncrement("Diárias").isPresent());
    }

    @Test
    public void findDimension() throws IOException, URISyntaxException {
        DEF def = TestUtils.parseDEF("/tabwin/SIMPLE.DEF");

        Optional<Dimension> dimension = def.findDimension("Sexo");
        assertTrue(dimension.isPresent());
        assertEquals(Variable.Type.L, dimension.get().getType());
    }

    @Test(expected = NoSuchElementException.class)
    public void findNonexistentDimension() throws IOException, URISyntaxException {
        DEF def = TestUtils.parseDEF("/tabwin/SIMPLE.DEF");

        def.getDimension("Idade detalhada");
    }

    @Test(expected = NoSuchElementException.class)
    public void findNonexistentIncrement() throws IOException, URISyntaxException {
        DEF def = TestUtils.parseDEF("/tabwin/SIMPLE.DEF");

        def.getIncrement("Qtde.Leitos");
    }

    @Test
    public void testListFiles() throws IOException, URISyntaxException {
        DEF def = TestUtils.parseDEF("/tabwin/SIMPLE.DEF");

        List<File> files = def.listDataFiles();
        assertEquals(1, files.size());
        assertEquals("RDGO1801.DBC", files.get(0).getName());

        assertEquals(0, def.listDataFiles("DN*dbc").size());
        assertEquals(1, def.listDataFiles("RDGO*DBC").size());
        assertTrue(def.listDataFiles().get(0).exists());
    }

}
