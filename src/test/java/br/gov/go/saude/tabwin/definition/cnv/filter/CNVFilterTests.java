package br.gov.go.saude.tabwin.definition.cnv.filter;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CNVFilterTests {
    @Test
    public void testFilterValue() {
        CNVFilter f = new CNVFilterValue("10");

        assertEquals("10", f.getMin());
        assertEquals("10", f.getMax());

        assertTrue(f.checkValue("10"));
        assertFalse(f.checkValue("1"));
        assertFalse(f.checkValue("0"));
        assertFalse(f.checkValue("101"));
        assertFalse(f.checkValue("100"));

        assertEquals("10", f.toString());
    }

    @Test
    public void testFilterRange() {
        CNVFilter f = new CNVFilterRange("07", "12");

        assertEquals("07", f.getMin());
        assertEquals("12", f.getMax());

        assertTrue(f.checkValue("07"));
        assertTrue(f.checkValue("09"));
        assertTrue(f.checkValue("10"));
        assertTrue(f.checkValue("12"));

        assertFalse(f.checkValue("121"));
        assertFalse(f.checkValue(""));
        assertFalse(f.checkValue("9"));

        assertEquals("07-12", f.toString());
    }

    @Test
    public void testFilterRangeAlpha() {
        CNVFilter f = new CNVFilterRange("A07", "B12");

        assertEquals("A07", f.getMin());
        assertEquals("B12", f.getMax());

        assertTrue(f.checkValue("B07"));
        assertTrue(f.checkValue("A99"));
        assertTrue(f.checkValue("B00"));

        assertFalse(f.checkValue("B13"));
        assertFalse(f.checkValue("A06"));
        assertFalse(f.checkValue("A060"));

        assertEquals("A07-B12", f.toString());
    }

    @Test
    public void testProperties() {
        assertEquals("A00-A20", new CNVFilterRange("A00", "A20").toString());
        assertEquals(new CNVFilterRange("A00", "A20"), new CNVFilterRange("A00", "A20"));

    }
}
