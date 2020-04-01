package br.gov.go.saude.tabwin.definition;

import br.gov.go.saude.tabwin.definition.cnv.CNVParser;
import br.gov.go.saude.tabwin.definition.dbf.DBFParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DirectoryConversionLoader implements ConversionLoader {
    private static final Logger log = LoggerFactory.getLogger(DirectoryConversionLoader.class);

    private static final Charset DEFAULT_CHARSET = Charset.forName("Windows-1252");

    private final Map<String, ConversionFile> cnvs = new ConcurrentHashMap<>();
    private final File baseDir;

    private static final ConversionParser[] DEFAULT_PARSERS = new ConversionParser[]{
            new CNVParser(), new DBFParser()
    };


    private List<ConversionParser> parsers;

    public DirectoryConversionLoader(final File baseDir) {
        this(baseDir, DEFAULT_CHARSET);
    }

    public DirectoryConversionLoader(final File baseDir, final Charset charset) {
        this(baseDir, Arrays.asList(DEFAULT_PARSERS), charset);
    }

    public DirectoryConversionLoader(final File baseDir, final Collection<ConversionParser> parsers, final Charset charset) {
        this.baseDir = baseDir;
        this.parsers = new ArrayList<>(parsers);
    }

    public ConversionFile load(final String name) {
        return cnvs.computeIfAbsent(name.toUpperCase(), this::loadFromFile);
    }

    public ConversionFile loadFromFile(final String name) {
        log.info("Loading conversion file {}", name);
        try {
            final File file = Utils.resolveCaseInsensitivePath(baseDir, name);

            for (ConversionParser parser : parsers)
                if (parser.supports(file))
                    return parser.parse(file);

        } catch (IOException ex) {
            throw TabWinDefinitionException.ioException(name, ex);
        }

        throw TabWinDefinitionException.unsupportedFormat(name);
    }

    public ConversionFile loadFile(String name) {
        return load(name);
    }
}