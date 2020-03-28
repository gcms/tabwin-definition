package br.gov.go.saude.tabwin.definition;

import java.io.File;
import java.io.IOException;

public interface ConversionParser {
    boolean supports(File filename);

    ConversionFile parse(File file) throws IOException;
}
