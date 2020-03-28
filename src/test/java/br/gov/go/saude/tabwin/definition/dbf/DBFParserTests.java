package br.gov.go.saude.tabwin.definition.dbf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;

import br.gov.go.saude.tabwin.definition.TestUtils;

public class DBFParserTests {
    @Test
    public void testParseSimple() throws URISyntaxException {
        //LGrupo de Procedimentos    ,PROC_REA  ,NO_GRUPO  ,DBF\TB_GRUPO.DBF
        File tabWinDir = TestUtils.getTabWinDir();
        File dbfFile = new File(tabWinDir, "DBF/TB_GRUPO.DBF");
        assertTrue(dbfFile.exists());

        DBF dbf = new DBFParser().parse(dbfFile);
        assertNotNull(dbf);

//        VariableConversor conversor = dbf.loadAndUseConversor("NO_GRUPO", );

//        conversor.findByDescription("0408050837");
    }
}