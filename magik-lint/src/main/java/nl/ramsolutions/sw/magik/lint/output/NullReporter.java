package nl.ramsolutions.sw.magik.lint.output;

import java.util.Collections;
import java.util.Set;
import nl.ramsolutions.sw.magik.checks.MagikIssue;

/**
 * Null reporter.
 */
public class NullReporter implements Reporter {

    @Override
    public void reportIssue(MagikIssue magikIssue) {
    }

    @Override
    public Set<String> reportedSeverities() {
        return Collections.emptySet();
    }

}
