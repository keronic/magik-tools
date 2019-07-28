package org.stevenlooman.sw.magik.checks;

import com.sonar.sslr.api.AstNode;
import org.stevenlooman.sw.magik.MagikCheck;
import org.stevenlooman.sw.magik.MagikIssue;
import org.stevenlooman.sw.magik.MagikVisitorContext;
import org.stevenlooman.sw.magik.parser.MagikParser;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

public class MagikCheckTestBase {
  protected static MagikVisitorContext createContext(String code) throws IllegalArgumentException {
    MagikParser parser = new MagikParser(Charset.forName("UTF-8"));
    AstNode root = parser.parse(code);
    return new MagikVisitorContext(code, root);
  }

  protected static MagikVisitorContext createFileContext(Path path)
      throws IllegalArgumentException {
    MagikParser parser = new MagikParser(Charset.forName("UTF-8"));
    AstNode root = parser.parse(path);
    if (root.getChildren().isEmpty()) {
      throw new IllegalArgumentException("Unable to parse code");
    }
    return new MagikVisitorContext(path, root);
  }

  protected List<MagikIssue> runCheck(String code, MagikCheck check) {
    MagikVisitorContext context = createContext(code);
    List<MagikIssue> issues = check.scanFileForIssues(context);
    return issues;
  }

  protected List<MagikIssue> runFileCheck(Path path, MagikCheck check) {
    MagikVisitorContext context = createFileContext(path);
    List<MagikIssue> issues = check.scanFileForIssues(context);
    return issues;
  }
}
