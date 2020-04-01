package br.gov.go.saude.tabwin.definition.dbf;

import br.gov.go.saude.tabwin.definition.ConversionParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DBFParser implements ConversionParser {
    private static final Pattern FILENAME_PATTERN = Pattern.compile(".*\\.dbf", Pattern.CASE_INSENSITIVE);
    private static final Charset DEFAULT_CHARSET = Charset.forName("Windows-1252");

    @Override
    public boolean supports(File filename) {
        return FILENAME_PATTERN.matcher(filename.getName()).matches();
    }

    public DBF parse(File file) throws FileNotFoundException {
        return parse(file, DEFAULT_CHARSET);
    }

    public DBF parse(File file, Charset charset) throws FileNotFoundException {
        return parse(new FileInputStream(file), charset);
    }

    public DBF parse(InputStream is, Charset charset) {
        return new DBF(is, charset);
    }
}