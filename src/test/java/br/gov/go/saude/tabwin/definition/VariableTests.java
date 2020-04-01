package br.gov.go.saude.tabwin.definition;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VariableTests {
    @Test
    public void increment() {
        Increment inc = new Increment(Variable.Type.I, "Valor Total", "VAL_TOT");

        assertTrue(inc.toLineString().matches("IValor Total,\\s+VAL_TOT"));
        assertEquals("Valor Total (VAL_TOT)", inc.toString());
    }

    @Test
    public void dimension() {
        Dimension dimension = new Dimension(Variable.Type.L, "Sexo", "SEXO", "1", "SEXO.CNV");

        assertEquals("LSexo, SEXO, 1, SEXO.CNV", dimension.toLineString());
    }
}
