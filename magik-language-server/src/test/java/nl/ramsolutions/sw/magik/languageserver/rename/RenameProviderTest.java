package nl.ramsolutions.sw.magik.languageserver.rename;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.List;
import java.util.Map;
import nl.ramsolutions.sw.magik.MagikTypedFile;
import nl.ramsolutions.sw.magik.analysis.definitions.DefinitionKeeper;
import nl.ramsolutions.sw.magik.analysis.definitions.IDefinitionKeeper;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.PrepareRenameDefaultBehavior;
import org.eclipse.lsp4j.PrepareRenameResult;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either3;
import org.junit.jupiter.api.Test;

/** Test RenameProvider. */
@SuppressWarnings("checkstyle:MagicNumber")
class RenameProviderTest {

  private static final URI DEFAULT_URI = URI.create("memory://source.magik");

  private Either3<Range, PrepareRenameResult, PrepareRenameDefaultBehavior> getPrepareRename(
      final String code, final Position position) {
    final IDefinitionKeeper definitionKeeper = new DefinitionKeeper();
    final MagikTypedFile magikFile = new MagikTypedFile(DEFAULT_URI, code, definitionKeeper);
    final RenameProvider provider = new RenameProvider();
    return provider.providePrepareRename(magikFile, position);
  }

  private WorkspaceEdit getRename(
      final String code, final Position position, final String newName) {
    final IDefinitionKeeper definitionKeeper = new DefinitionKeeper();
    final MagikTypedFile magikFile = new MagikTypedFile(DEFAULT_URI, code, definitionKeeper);
    final RenameProvider provider = new RenameProvider();
    return provider.provideRename(magikFile, position, newName);
  }

  @Test
  void testPrepareRenameLocal() {
    final String code =
        """
        _block
            _local var
            show(var)
        _endblock
        """;
    final Position position = new Position(1, 12); // On `var`.

    final Either3<Range, PrepareRenameResult, PrepareRenameDefaultBehavior> either =
        this.getPrepareRename(code, position);
    assertThat(either).isNotNull();

    final PrepareRenameResult prepareRenameResult = either.getSecond();
    assertThat(prepareRenameResult.getRange())
        .isEqualTo(new Range(new Position(1, 11), new Position(1, 14)));
    assertThat(prepareRenameResult.getPlaceholder()).isEqualTo("var");
  }

  @Test
  void testRenameLocal() {
    final String code =
        """
        _block
            _local var
            show(var)
        _endblock
        """;
    final Position position = new Position(1, 12); // On `var`.

    final WorkspaceEdit workspaceEdit = this.getRename(code, position, "new");
    assertThat(workspaceEdit).isNotNull();

    assertThat(workspaceEdit.getChanges())
        .isEqualTo(
            Map.of(
                "memory://source.magik",
                List.of(
                    new TextEdit(new Range(new Position(1, 11), new Position(1, 14)), "new"),
                    new TextEdit(new Range(new Position(2, 9), new Position(2, 12)), "new"))));
  }

  @Test
  void testRenameLocalFromUsage() {
    final String code =
        """
        _block
            _local var
            show(var)
        _endblock
        """;
    final Position position = new Position(2, 12); // On `var`.

    final WorkspaceEdit workspaceEdit = this.getRename(code, position, "new");
    assertThat(workspaceEdit).isNotNull();

    assertThat(workspaceEdit.getChanges())
        .isEqualTo(
            Map.of(
                "memory://source.magik",
                List.of(
                    new TextEdit(new Range(new Position(1, 11), new Position(1, 14)), "new"),
                    new TextEdit(new Range(new Position(2, 9), new Position(2, 12)), "new"))));
  }

  @Test
  void testPrepareRenameForVariable() {
    final String code =
        """
        _block
            _for iter_var _over 1.upto(10)
            _loop
                show(iter_var)
            _endloop
        _endblock
        """;
    final Position position = new Position(1, 10); // on `iter_var`.

    final Either3<Range, PrepareRenameResult, PrepareRenameDefaultBehavior> either =
        this.getPrepareRename(code, position);
    assertThat(either).isNotNull();

    final PrepareRenameResult prepareRenameResult = either.getSecond();
    assertThat(prepareRenameResult.getRange())
        .isEqualTo(new Range(new Position(1, 9), new Position(1, 17)));
    assertThat(prepareRenameResult.getPlaceholder()).isEqualTo("iter_var");
  }

  @Test
  void testRenameForVariable() {
    final String code =
        """
        _block
            _for iter_var _over 1.upto(10)
            _loop
                show(iter_var)
            _endloop
        _endblock
        """;
    final Position position = new Position(1, 12); // On `iter_var`.

    final WorkspaceEdit workspaceEdit = this.getRename(code, position, "new");
    assertThat(workspaceEdit).isNotNull();

    assertThat(workspaceEdit.getChanges())
        .isEqualTo(
            Map.of(
                "memory://source.magik",
                List.of(
                    new TextEdit(new Range(new Position(1, 9), new Position(1, 17)), "new"),
                    new TextEdit(new Range(new Position(3, 13), new Position(3, 21)), "new"))));
  }

  @Test
  void testRenameOptionalParameter() {
    final String code =
        """
        _method a.b(_optional param1)
            write(param1)
        _endmethod
        """;
    final Position position = new Position(1, 12); // On `param1`.

    final WorkspaceEdit workspaceEdit = this.getRename(code, position, "new");
    assertThat(workspaceEdit).isNotNull();

    assertThat(workspaceEdit.getChanges())
        .isEqualTo(
            Map.of(
                "memory://source.magik",
                List.of(
                    new TextEdit(new Range(new Position(0, 22), new Position(0, 28)), "new"),
                    new TextEdit(new Range(new Position(1, 10), new Position(1, 16)), "new"))));
  }
}
