package br.gov.go.saude.tabwin.definition.dbf;

import br.gov.go.saude.tabwin.definition.*;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class DBFTests {
    //        LGrupo de Procedimentos    ,PROC_REA  ,NO_GRUPO  ,DBF\TB_GRUPO.DBF
    //        CGrupo de Procedimentos    ,PROC_REA  ,CO_GRUPO  ,DBF\TB_GRUPO.DBF
    //        SGrupo de Procedimentos    ,PROC_REA  ,NO_GRUPO  ,DBF\TB_GRUPO.DBF
    @Test
    public void simpleProperties() throws URISyntaxException {

        File tabWinDir = TestUtils.getTabWinDir();
        File dbfFile = new File(tabWinDir, "DBF/TB_GRUPO.DBF");
        assertTrue(dbfFile.exists());

        DBF dbf = new DBFParser().parse(dbfFile);
        DBFConversor conversor = dbf.createConversor("PROC_REA", "NO_GRUPO");
        assertEquals(2, conversor.getValueLength());
    }

    @Test
    public void keyFieldNotFound() throws URISyntaxException {

        File tabWinDir = TestUtils.getTabWinDir();
        File dbfFile = new File(tabWinDir, "DBF/TB_GRUPO.DBF");
        assertTrue(dbfFile.exists());

        DBF dbf = new DBFParser().parse(dbfFile);

        DBFConversor c1 = dbf.createConversor("PROC_REA", "NO_GRUPO");
        DBFConversor c2 = dbf.createConversor(0, "NO_GRUPO");
        assertEquals(c1.getEntries().collect(Collectors.toList()), c2.getEntries().collect(Collectors.toList()));

        DBFConversor c3 = dbf.createConversor("CO_GRUPO", "NO_GRUPO");
        assertEquals(c2.getEntries().collect(Collectors.toList()), c3.getEntries().collect(Collectors.toList()));
    }

    @Test
    public void loadAndUseConversor() throws URISyntaxException {
        DBF dbf = new DBF(TestUtils.getFile("/tabwin/DBF/TB_GRUPO.DBF"), Charset.forName("Cp850"));
        Dimension var = new Dimension(Variable.Type.L, "Grupo de Procedimentos", "PROC_REA", "NO_GRUPO", "TB_GRUPO.DBF");

        DBFConversor conversor = dbf.createMapping(var);


        assertTrue(conversor.findByValue("03").isPresent());
        assertEquals("Procedimentos clínicos", conversor.findByValue("03").get().getDescription());

        assertTrue(conversor.findByDescription("Procedimentos clínicos").isPresent());
        assertEquals("03", conversor.findByDescription("Procedimentos clínicos").get().getKeyValue());
    }
}
