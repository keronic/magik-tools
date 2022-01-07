package nl.ramsolutions.sw.magik.checks.checks;

import java.util.List;
import nl.ramsolutions.sw.magik.checks.MagikCheck;
import nl.ramsolutions.sw.magik.checks.MagikIssue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test SwMethodDocCheck.
 */
class SwMethodDocCheckTest extends MagikCheckTestBase {

    @Test
    void testSwMethodDoc() {
        final MagikCheck check = new SwMethodDocCheck();
        final String code = ""
            + "_method a.b(param1, param2?)\n"
            + "    ## This is an example method. PARAM1 and PARAM2? are used.\n"
            + "    ## Some more doc.\n"
            + "_endmethod";
        final List<MagikIssue> issues = runCheck(code, check);
        assertThat(issues).isEmpty();
    }

    @Test
    void testSwMethodDocMssing() {
        final MagikCheck check = new SwMethodDocCheck();
        final String code = ""
            + "_method a.b(param1, param2)\n"
            + "    ## This is an example method.\n"
            + "    ## Some more doc.\n"
            + "_endmethod";
        final List<MagikIssue> issues = runCheck(code, check);
        assertThat(issues).hasSize(2);
    }

}