package br.gov.go.saude.tabwin.definition;

import br.gov.go.saude.tabwin.definition.cnv.CNV;
import br.gov.go.saude.tabwin.definition.cnv.CNVParser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;

public class TestUtils {

    public static DEF loadRD2008() throws URISyntaxException, IOException {
        File baseDir = TestUtils.getTabWinDir();

        DEFParser parser = new DEFParser();
        return parser.parse(new File(baseDir, "RD2008.DEF"), Charset.forName("Windows-1252"));
    }

    public static File getTabWinDir() throws URISyntaxException {
        URL url = DEFParserTests.class.getResource("/tabwin");
        return new File(url.toURI());
    }

    public static ConversionLoader getLoader() throws URISyntaxException {
        return new DirectoryConversionLoader(getTabWinDir());
    }

    public static DEF parseDEF(String path) throws URISyntaxException, IOException {
        URI uri = TestUtils.class.getResource(path).toURI();
        return new DEFParser().parse(new File(uri), Charset.forName("Windows-1252"));
    }

    public static CNV parseCNV(String path) throws IOException {
        URL url = TestUtils.class.getResource(path);
        String name = Paths.get(path).getFileName().toString();
        return new CNVParser().parse(name, url.openStream());
    }

    public static File getFile(String path) throws URISyntaxException {
        URL url = TestUtils.class.getResource(path);
        return new File(url.toURI());
    }
}
