package br.gov.go.saude.tabwin.definition.cnv.finder;

import br.gov.go.saude.tabwin.definition.TabWinDefinitionException;
import br.gov.go.saude.tabwin.definition.TestUtils;
import br.gov.go.saude.tabwin.definition.cnv.CNV;
import br.gov.go.saude.tabwin.definition.cnv.CNVCategory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CNVTreeRangeRangeFinderTests {
    @Test
    public void testSimple() throws IOException {
//        12 4 F
//         1  0 dias                                             0000
//         2  1 dia                                              0001
//         3  2 dias                                             0002
//         4  3 dias                                             0003
//         5  4 dias                                             0004
//         6  5 dias                                             0005
//         7  6 dias                                             0006
//         8  7 dias                                             0007
//         9  8-14 dias                                          0014
//        10  15-21 dias                                         0021
//        11  22-28 dias                                         0028
//        12  29 dias e +                                        9999

        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/PERM.CNV");

        CNVCategoryFinder finder = new CNVTreeRangeRangeFinder(cnv.getCategories().toArray(CNVCategory[]::new));

        assertEquals("0 dias", finder.findEntry("0000").getDescription());
        assertEquals("1 dia", finder.findEntry("0001").getDescription());

        assertEquals("5 dias", finder.findEntry("0005").getDescription());
        assertEquals("2 dias", finder.findEntry("0002").getDescription());


        for (int i = 8; i <= 14; i++)
            assertEquals("8-14 dias", finder.findEntry(String.format("%04d", i)).getDescription());
        for (int i = 15; i <= 21; i++)
            assertEquals("15-21 dias", finder.findEntry(String.format("%04d", i)).getDescription());
        for (int i = 22; i <= 28; i++)
            assertEquals("22-28 dias", finder.findEntry(String.format("%04d", i)).getDescription());

        for (int i = 29; i < 10000; i++)
            assertEquals("29 dias e +", finder.findEntry(String.format("%04d", i)).getDescription());
    }

    @Test
    public void testDecimalValues() throws IOException {
//        11 14 F
//         1   Sem Beneficiários                                              0
//         2   1 a 100 beneficiários                                      99.99
//         3   101 a 1.000 beneficiários                                 999.99
//         4   1.001 a 2.000 beneficiários                              1999.99
//         5   2.001 a 5.000 beneficiários                              4999.99
//         6   5.001 a 10.000 beneficiários                             9999.99
//         7   10.001 a 20.000 beneficiários                           19999.99
//         8   20.001 a 50.000 beneficiários                           49999.99
//         9   50.001 a 100.000 beneficiários                          99999.99
//        10   100.001 a 500.000 beneficiários                        499999.99
//        11   Acima de 500.000 beneficiários                    99999999999.99

        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/porte_operadora3.cnv");

        CNVCategoryFinder finder = new CNVTreeRangeRangeFinder(cnv.getCategories().toArray(CNVCategory[]::new));

        assertEquals("20.001 a 50.000 beneficiários", finder.findEntry("22000.00").getDescription());
        assertEquals("20.001 a 50.000 beneficiários", finder.findEntry("000000022000.00").getDescription());

        assertEquals("1 a 100 beneficiários", finder.findEntry("99.99").getDescription());
    }

    @Test(expected = TabWinDefinitionException.class)
    public void testInvalidInput() throws IOException {
        CNV cnv = TestUtils.parseCNV("/tabwin/CNV/ANO.CNV");

        new CNVTreeRangeRangeFinder(cnv.getCategories().toArray(CNVCategory[]::new));
    }
}
