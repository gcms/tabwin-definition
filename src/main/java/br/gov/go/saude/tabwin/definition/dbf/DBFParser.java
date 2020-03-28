package br.gov.go.saude.tabwin.definition.dbf;

import br.gov.go.saude.tabwin.definition.ConversionParser;

import java.io.File;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DBFParser implements ConversionParser {
  private static final Pattern FILENAME_PATTERN = Pattern.compile(".*\\.dbf", Pattern.CASE_INSENSITIVE);
  private static final String DEFAULT_CHARSET = "Cp850";

  @Override
  public boolean supports(File filename) {
    return FILENAME_PATTERN.matcher(filename.getName()).matches();
  }

  public DBF parse(File file) {
    return new DBF(file, Charset.forName(DEFAULT_CHARSET));
  }

}