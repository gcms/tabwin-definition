package br.gov.go.saude.tabwin.definition.cnv;

import br.gov.go.saude.tabwin.definition.Category;
import br.gov.go.saude.tabwin.definition.TabWinDefinitionException;
import br.gov.go.saude.tabwin.definition.TestUtils;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilter;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilterValue;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;


public class CNVParserTests {

    @Test
    public void testParseSingleValue() {
        CNVParsingContext ctx = Mockito.mock(CNVParsingContext.class);

        List<CNVFilter> filters = CNVParser.parseValues(ctx, "G009");

        assertEquals(1, filters.size());
        assertEquals("G009", filters.get(0).getMin());
        assertEquals("G009", filters.get(0).getMax());
    }

    @Test
    public void testParseMultValue() {
        CNVParsingContext ctx = Mockito.mock(CNVParsingContext.class);

        List<CNVFilter> filters = CNVParser.parseValues(ctx, "G009,G00");

        assertEquals(2, filters.size());
        assertEquals("G009", filters.get(0).getMin());
        assertEquals("G009", filters.get(0).getMax());

        assertEquals("G00", filters.get(1).getMin());
        assertEquals("G00", filters.get(1).getMax());
    }

    @Test
    public void testParseEmptyValues1() {
        CNVParsingContext ctx = Mockito.mock(CNVParsingContext.class);

        List<CNVFilter> filters = CNVParser.parseValues(ctx, "G009,,G00");

        assertEquals(2, filters.size());
        assertEquals("G009", filters.get(0).getMin());
        assertEquals("G009", filters.get(0).getMax());

        assertEquals("G00", filters.get(1).getMin());
        assertEquals("G00", filters.get(1).getMax());
    }

    @Test
    public void testParseEmptyValues2() {
        CNVParsingContext ctx = Mockito.mock(CNVParsingContext.class);

        List<CNVFilter> filters = CNVParser.parseValues(ctx, "G009,G00,");

        assertEquals(2, filters.size());
        assertEquals("G009", filters.get(0).getMin());
        assertEquals("G009", filters.get(0).getMax());

        assertEquals("G00", filters.get(1).getMin());
        assertEquals("G00", filters.get(1).getMax());
    }


    @Test
    public void testParseEmptyValues3() {
        CNVParsingContext ctx = Mockito.mock(CNVParsingContext.class);

        List<CNVFilter> filters = CNVParser.parseValues(ctx, "G009,G00 ,   ");

        assertEquals(2, filters.size());
        assertEquals("G009", filters.get(0).getMin());
        assertEquals("G009", filters.get(0).getMax());

        assertEquals("G00 ", filters.get(1).getMin());
        assertEquals("G00 ", filters.get(1).getMax());
    }

    @Test
    public void testParseRange() {
        CNVParsingContext ctx = Mockito.mock(CNVParsingContext.class);

        List<CNVFilter> filters = CNVParser.parseValues(ctx, "G009-G020");

        assertEquals(1, filters.size());
        assertEquals("G009", filters.get(0).getMin());
        assertEquals("G020", filters.get(0).getMax());
    }

    @Test
    public void testParseRangeAndValues() {
        CNVParsingContext ctx = Mockito.mock(CNVParsingContext.class);

        List<CNVFilter> filters = CNVParser.parseValues(ctx, "G009-G020,G024,G032,");

        assertEquals(3, filters.size());
        assertEquals("G009", filters.get(0).getMin());
        assertEquals("G020", filters.get(0).getMax());

        assertEquals("G024", filters.get(1).getMin());
        assertEquals("G024", filters.get(1).getMax());

        assertEquals("G032", filters.get(2).getMin());
        assertEquals("G032", filters.get(2).getMax());
    }

    @Test
    public void testRangeWithSpacesOnMin() {
        List<CNVFilter> filter = CNVParser.parseValues(new CNVParsingContext(), "L26 -L269,");
        assertEquals(1, filter.size());

        CNVFilter first = filter.get(0);
        assertEquals("L26 ", first.getMin());
        assertEquals("L269", first.getMax());
    }

    @Test
    public void testRangeWithSpacesOnMax() {
        List<CNVFilter> filter = CNVParser.parseValues(new CNVParsingContext(), "L250-L26 ,");
        assertEquals(1, filter.size());

        CNVFilter first = filter.get(0);
        assertEquals("L250", first.getMin());
        assertEquals("L26 ", first.getMax());
    }


    @Test
    public void testParseLineWithEmptyValues() throws CNVParseException {
        CNVParsingContext ctx = Mockito.mock(CNVParsingContext.class);

        String line = "    167  G00.9 Meningite bacteriana não especificada        G009,G00 ,";
        CNVParser.parseLine(ctx, line);


        Mockito.verify(ctx).addLine("", "167", "G00.9 Meningite bacteriana não especificada",
                Lists.newArrayList(new CNVFilterValue("G009"), new CNVFilterValue("G00 ")));
    }

    @Test
    public void testParseLineWithAlphaValues() throws CNVParseException {
        CNVParsingContext ctx = Mockito.mock(CNVParsingContext.class);

        String line = "    167  G00.9 Meningite bacteriana não especificada        G009,G00 ,";
        CNVParser.parseLine(ctx, line);


        Mockito.verify(ctx, Mockito.atLeastOnce()).setHasCharOnFilter(true);
    }

    @Test
    public void testParseLineWithComments() throws CNVParseException {
        CNVParsingContext ctx = Mockito.mock(CNVParsingContext.class);

        String line = "    167  G00.9 Meningite bacteriana não especificada        G009,G00 ; Comentário";
        CNVParser.parseLine(ctx, line);

        Mockito.verify(ctx).addLine("", "167", "G00.9 Meningite bacteriana não especificada",
                Lists.newArrayList(new CNVFilterValue("G009"), new CNVFilterValue("G00")));
        Mockito.verify(ctx, Mockito.atLeastOnce()).setHasCharOnFilter(true);
    }

    @Test
    public void testParseEmptyLine() throws CNVParseException {
        CNVParsingContext ctx = Mockito.mock(CNVParsingContext.class);

        String line = "  ; Teste linha com comentário";
        CNVParser.parseLine(ctx, line);

        Mockito.verify(ctx).countLine();
        Mockito.verifyNoMoreInteractions(ctx);
    }

    @Test
    public void testParseFileProperties() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/ANO.CNV");

        assertEquals("ANO.CNV", cnv.getHeader().getName());
        assertEquals(cnv.getHeader().getCategoriesCount(), cnv.getEntries().count());
        assertEquals(2, cnv.getHeader().getLength());
        assertTrue(cnv.getHeader().isLong());
        assertTrue(cnv.getHeader().hasRange());
        assertTrue(cnv.getHeader().hasOnlyInts());
    }


    @Test
    public void testParseFileWithDescription() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/AgravoBloqS.CNV");

        assertEquals("PT SAS conjunta numero 20 public.25-05-2005 - RELAÇÃO DE DOENÇAS NOTIFICAVEIS E AGRAVOS COM BLOQUEIO AUTOMATICO DE PAGTO AIH", cnv.getHeader().getDescription());
        assertEquals(16, cnv.getCategories().count());
    }

    @Test
    public void testParseFileWithEmptyLinesAndComments() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/ANO2.CNV");

        assertEquals(cnv.getHeader().getCategoriesCount(), cnv.getEntries().count());
        assertEquals(2, cnv.getHeader().getLength());
        assertTrue(cnv.getHeader().isLong());
        assertTrue(cnv.getHeader().hasRange());
        assertTrue(cnv.getHeader().hasOnlyInts());
    }

    @Test(expected = CNVParseException.class)
    public void testParseInvalidDEF() throws IOException {
        InputStream is = getClass().getResourceAsStream("/tabwin/RD2008.DEF");

        new CNVParser().parse("RD2008.DEF", is);
    }

    @Test(expected = CNVParseException.class)
    public void testParseInvalidCNV() throws IOException {
        InputStream ano = getClass().getResourceAsStream("/tabwin/CNV/MALFORMED.CNV");

        new CNVParser().parse("MALFORMED.CNV", ano);
    }


    @Test(expected = CNVParseException.class)
    public void testParseInvalidCNVExtraCategory() throws IOException {
        InputStream is = getClass().getResourceAsStream("/tabwin/CNV/outofrange.cnv");

        new CNVParser().parse("outofrange.cnv", is);
    }


    @Test
    public void testParseRepeatedLines() throws IOException {
        InputStream ano = getClass().getResourceAsStream("/tabwin/CNV/REPEATED.CNV");

        CNV cnv = new CNVParser().parse("REPEATED.CNV", ano);

        assertNotNull(cnv.findEntryByValue("81"));
        Category entry = cnv.findEntryByValue("88");
        assertNotNull(entry);

        assertEquals("1980", entry.getDescription());
    }

    @Test
    public void testLoadWithSubtotals() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/CIDX12.CNV");

        assertTrue(cnv.getHeader().hasRange());
        assertFalse(cnv.getHeader().hasOnlyInts());
        assertTrue(cnv.getHeader().isLong());
    }

    @Test
    public void testDescription() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/AgravoBloq.CNV");

        assertEquals("PT SAS conjunta numero 20 public.25-05-2005 - RELAÇÃO DE DOENÇAS NOTIFICAVEIS E AGRAVOS COM BLOQUEIO AUTOMATICO DE PAGTO AIH", cnv.getHeader().getDescription());
    }
}
