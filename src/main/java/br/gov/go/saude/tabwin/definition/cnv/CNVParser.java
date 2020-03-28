package br.gov.go.saude.tabwin.definition.cnv;

import br.gov.go.saude.tabwin.definition.ConversionParser;
import br.gov.go.saude.tabwin.definition.TabWinDefinitionException;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilter;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilterRange;
import br.gov.go.saude.tabwin.definition.cnv.filter.CNVFilterValue;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static br.gov.go.saude.tabwin.definition.Utils.stripComments;

public class CNVParser implements ConversionParser {
    private static final Logger log = LoggerFactory.getLogger(CNVParser.class);


    private static final String DEFAULT_CHARSET = "Windows-1252";

    private static final Pattern FILENAME_PATTERN = Pattern.compile(".*\\.cnv$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean supports(File filename) {
        return FILENAME_PATTERN.matcher(filename.getName()).matches();
    }

    @Override
    public CNV parse(File file) throws IOException {
        return parse(file.getName(), new FileInputStream(file));
    }

    public CNV parse(final String name, InputStream ano) throws IOException {
        return parse(name, new InputStreamReader(ano, DEFAULT_CHARSET));
    }

    public CNV parse(final String name, final Reader reader) throws IOException {
        return parse(name, new BufferedReader(reader));
    }

    public CNV parse(final String name, final BufferedReader reader) throws IOException {
        log.info("Parsing CNV from {}", name);

        final CNVParsingContext context = new CNVParsingContext();

        context.setName(name);
        parseHeader(context, reader);
        parseContent(context, reader);

        log.info("Finished parsing {}", name);
        return context.build();
    }


    private static final Pattern HEADER_PATTERN = Pattern.compile("[A-Z]*\\s*([0-9]+)\\s+([0-9]+)\\s*([A-Z]*)");

    private String skipToHeader(final CNVParsingContext context, final BufferedReader reader) throws IOException {
        String comment = null;

        String line;
        while ((line = reader.readLine()) != null) {
            context.countLine();

            line = line.trim();

            if (!line.startsWith(";") && !line.isEmpty())
                break;

            if (comment == null && line.startsWith(";") && line.length() > 1)
                comment = line.substring(1).trim();
        }

        context.setComment(comment);

        return line;
    }

    private void parseHeader(final CNVParsingContext context, final BufferedReader reader) throws IOException {
        String line = stripComments(skipToHeader(context, reader)).trim();

        try {
            Matcher m = HEADER_PATTERN.matcher(line);
            m.matches();
            MatchResult mr = m.toMatchResult();

            int lines = Integer.parseInt(mr.group(1));
            int length = Integer.parseInt(mr.group(2));
            String type = mr.group(3);

            context.setNumLines(lines);
            context.setFieldLength(length);
            context.setType(type);

        } catch (IllegalStateException ex) {
            throw TabWinDefinitionException.parseException(String.format("Error parsing CNV header in line '%s'", line), ex);
        }
    }


    private void parseContent(CNVParsingContext context, BufferedReader reader) {
        reader.lines().forEach(line -> parseLine(context, line));
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("^([\\s\\d]{3})([\\s\\d]{4})\\s(.{52})([0-9A-Za-z,\\.\\s\\-]+)[\\s;]*");

    static void parseLine(final CNVParsingContext context, String line) {
        context.countLine();

        if (line == null)
            return;

        line = stripComments(line);
        if (line.isEmpty())
            return;

        if (line.replaceAll("[^\\w]", "").isEmpty())
            return;


        Matcher m = LINE_PATTERN.matcher(line);
        if (!m.matches())
            throw TabWinDefinitionException.parseException(String.format("Invalid CNV line in %s: %d %s", context.getName(), context.getCurrentLine(), line));

        String subtotal = m.group(1).trim();
        String order = m.group(2).trim();
        String description = m.group(3).trim();
        String values = m.group(4).trim();

        List<CNVFilter> filter = parseValues(context, values);
        context.addLine(subtotal, order, description, filter);
    }

    static List<CNVFilter> parseValues(CNVParsingContext context, final String values) {
        return Splitter.on(",").omitEmptyStrings().splitToStream(values.trim())
                .map(it -> parseValue(context, it))
                .collect(Collectors.toList());
    }

    static CNVFilter parseValue(CNVParsingContext context, final String value) {
        checkChars(context, value);

        if (value.matches("[0-9A-Za-z][0-9A-Za-z ]*\\-[0-9A-Za-z][0-9A-Za-z ]*"))
            return parseRange(context, value);

        return new CNVFilterValue(value);
    }

    static CNVFilter parseRange(CNVParsingContext context, final String range) {
        context.setHasRange(true);

        final List<String> parts = Splitter.on("-").splitToList(range);
        return new CNVFilterRange(parts.get(0), parts.get(1));
    }

    private static void checkChars(CNVParsingContext context, String value) {
        if (value.matches(".*[^0-9\\-].*"))
            context.setHasCharOnFilter(true);
    }

}