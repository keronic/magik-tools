package nl.ramsolutions.sw.magik.analysis.definitions;

import com.sonar.sslr.api.AstNode;
import java.util.Collections;
import java.util.List;
import nl.ramsolutions.sw.magik.analysis.typing.types.GlobalReference;

/**
 * Indexed exemplar definition.
 */
public class IndexedExemplarDefinition extends Definition {

    private final List<String> parents;

    public IndexedExemplarDefinition(
        final AstNode node, final String pakkage, final String name, final List<String> parents) {
        super(node, pakkage, name);
        this.parents = List.copyOf(parents);
    }

    public List<String> getParents() {
        return Collections.unmodifiableList(this.parents);
    }

    public GlobalReference getGlobalReference() {
        return GlobalReference.of(this.getPackage(), this.getName());
    }

}
