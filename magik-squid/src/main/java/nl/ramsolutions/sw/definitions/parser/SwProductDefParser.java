package nl.ramsolutions.sw.definitions.parser;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.impl.Parser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import nl.ramsolutions.sw.FileCharsetDeterminer;
import nl.ramsolutions.sw.definitions.api.SwProductDefGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.parser.ParserAdapter;

/**
 * Smallworld product.def parser.
 */
public class SwProductDefParser {

    /**
     * Parse a file and return the AstNode.
     *
     * @param path Path to file
     * @return Tree
     * @throws IOException -
     */
    public AstNode parse(Path path) throws IOException {
        final Charset charset = FileCharsetDeterminer.determineCharset(path);
        final Parser<LexerlessGrammar> parser = new ParserAdapter<>(charset, SwProductDefGrammar.create());
        final File file = path.toFile();
        return parser.parse(file);
    }

}
