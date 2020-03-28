package br.gov.go.saude.tabwin.definition;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

public class DEFParser {
    private static final Logger log = LoggerFactory.getLogger(DEFParser.class);


    interface DEFLineParser {
        boolean parse(DEFParsingContext ctx, String line);
    }

    static class DEFCommentLineParser implements DEFLineParser {
        public boolean parse(DEFParsingContext ctx, String line) {
            if (!line.trim().startsWith(";"))
                return false;

            if (ctx.getDescription() == null) {
                line = CharMatcher.anyOf(" ;").trimFrom(line);
                if (!line.isEmpty())
                    ctx.setDescription(line);
            }

            return true;
        }
    }

    static class DEFDatabaseFilterParsers implements DEFLineParser {
        public boolean parse(DEFParsingContext ctx, String line) {
            if (!line.trim().startsWith("A"))
                return false;

            ctx.setFilePattern(line.substring(1).trim());
            return true;
        }
    }

    static class DEFVariableLineParser implements DEFLineParser {
        static Variable.Type findType(String line) {
            try {
                return Variable.Type.valueOf(line.substring(0, 1));
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        @Override
        public boolean parse(DEFParsingContext ctx, String line) {
            line = Utils.stripComments(line).trim();

            if (line.isEmpty())
                return false;


            Variable.Type type = findType(line);
            if (type == null)
                return false;

            List<String> parts = Splitter.on(",").trimResults().splitToList(line.substring(1));

            switch (type) {
                case I:
                    ctx.addIncrement(type, parts.get(0), parts.get(1));
                    break;
                case G:
                    ctx.addIncrement(Variable.Type.I, parts.get(0), parts.get(0));
                    break;
                case E:
                    ctx.addIncrement(Variable.Type.I, parts.get(0), parts.get(1).substring(0, parts.get(1).length() - 1));
                    break;
                case L:
                case C:
                case X:
                    if (parts.get(0).startsWith("*"))
                        break;
                case S:
                case D:
                case T:
                    ctx.addDimension(type, parts.get(0), parts.get(1), parts.get(2), parts.get(3));
                    break;
                default:
                    break;
            }

            return true;
        }

    }

    protected List<DEFLineParser> lineParsers;

    public DEFParser() {
        lineParsers = Lists.newArrayList(
                new DEFCommentLineParser(),
                new DEFDatabaseFilterParsers(),
                new DEFVariableLineParser()
        );
    }


    public static final String DEFAULT_CHARSET = "Windows-1252";

    public DEF parse(File file, Charset charset) throws IOException {
        return parse(file, new FileInputStream(file), charset);
    }

    public DEF parse(File file, InputStream is, Charset charset) {
        return parse(file, new InputStreamReader(is, charset));
    }

    public DEF parse(File file, Reader reader) {
        return parse(file, new BufferedReader(reader));
    }

    public DEF parse(File file, BufferedReader reader) {
        log.info("Parsing DEF from {}", file);

        DEFParsingContext ctx = new DEFParsingContext(file, new DirectoryConversionLoader(file.getParentFile()));
        reader.lines().forEach(it -> parseLine(ctx, it));

        log.info("Finished parsing {}", file);

        return ctx.build();
    }

    private void parseLine(DEFParsingContext ctx, String line) {
        for (DEFLineParser parser : lineParsers) {
            if (parser.parse(ctx, line))
                break;
        }
    }

}