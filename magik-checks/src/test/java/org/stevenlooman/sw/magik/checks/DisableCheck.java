package org.stevenlooman.sw.magik.checks;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.stevenlooman.sw.magik.MagikCheck;
import org.stevenlooman.sw.magik.MagikIssue;

import java.util.List;

public class DisableCheck extends MagikCheckTestBase {

  @Test
  public void testNoMLint() {
    MagikCheck check = new SizeZeroEmptyCheck();

    String code =
        "a.size = 0  # mlint: disable=size-zero-empty";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isEmpty();
  }

  @Test
  public void testDoFail() {
    MagikCheck check = new SizeZeroEmptyCheck();

    String code =
        "a.size = 0";
    List<MagikIssue> issues = runCheck(code, check);
    assertThat(issues).isNotEmpty();
  }

}
