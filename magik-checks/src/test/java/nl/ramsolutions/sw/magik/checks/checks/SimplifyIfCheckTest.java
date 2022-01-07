package nl.ramsolutions.sw.magik.checks.checks;

import java.util.List;
import nl.ramsolutions.sw.magik.checks.MagikCheck;
import nl.ramsolutions.sw.magik.checks.MagikIssue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test SimplifyIfCheck.
 */
class SimplifyIfCheckTest extends MagikCheckTestBase {

    @Test
    void testSimplifyIfIf() {
        final MagikCheck check = new SimplifyIfCheck();
        final String code = ""
            + "_if a\n"
            + "_then\n"
            + "    _if b\n"
            + "    _then\n"
            + "    _endif\n"
            + "_endif";
        final List<MagikIssue> issues = this.runCheck(code, check);
        assertThat(issues).isNotEmpty();
    }

    @Test
    void testSimplifyIfElseIf() {
        final MagikCheck check = new SimplifyIfCheck();
        final String code = ""
            + "_if a\n"
            + "_then\n"
            + "_else\n"
            + "    _if b\n"
            + "    _then\n"
            + "    _endif\n"
            + "_endif";
        final List<MagikIssue> issues = this.runCheck(code, check);
        assertThat(issues).isNotEmpty();
    }

    @Test
    void testNoSimplifyIfIf() {
        final MagikCheck check = new SimplifyIfCheck();
        final String code = ""
            + "_if a\n"
            + "_then\n"
            + "    _if b\n"
            + "    _then\n"
            + "    _endif\n"
            + "    c()\n"
            + "_endif";
        final List<MagikIssue> issues = this.runCheck(code, check);
        assertThat(issues).isEmpty();
    }

    @Test
    void testNoSimplifyIfIfElif() {
        final MagikCheck check = new SimplifyIfCheck();
        final String code = ""
            + "_if a\n"
            + "_then\n"
            + "    _if b\n"
            + "    _then\n"
            + "    _elif c\n"
            + "    _then\n"
            + "    _endif\n"
            + "_endif";
        final List<MagikIssue> issues = this.runCheck(code, check);
        assertThat(issues).isEmpty();
    }

    @Test
    void testNoSimplifyIfIfElse() {
        final MagikCheck check = new SimplifyIfCheck();
        final String code = ""
            + "_if a\n"
            + "_then\n"
            + "    _if b\n"
            + "    _then\n"
            + "    _else\n"
            + "    _endif\n"
            + "_endif";
        final List<MagikIssue> issues = this.runCheck(code, check);
        assertThat(issues).isEmpty();
    }

    @Test
    void testNoSimplifyIfElseIf() {
        final MagikCheck check = new SimplifyIfCheck();
        final String code = ""
            + "_if a\n"
            + "_then\n"
            + "_else\n"
            + "    _if b\n"
            + "    _then\n"
            + "    _endif\n"
            + "    c()\n"
            + "_endif";
        final List<MagikIssue> issues = this.runCheck(code, check);
        assertThat(issues).isEmpty();
    }

}
