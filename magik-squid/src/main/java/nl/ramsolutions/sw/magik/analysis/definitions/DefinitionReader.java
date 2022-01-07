package nl.ramsolutions.sw.magik.analysis.definitions;

import com.sonar.sslr.api.AstNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.ramsolutions.sw.magik.analysis.AstWalker;
import nl.ramsolutions.sw.magik.api.MagikGrammar;

/**
 * Class to easily read/parse AST for. Supported constructs:
 * - def_package (only takes symbols for name)
 * - def_enumeration
 * - def_slotted_exemplar (only takes symbols for name + slot names)
 * - def_indexed_exemplar
 * - def_mixin
 * - method definition
 * - define_slot_access
 * - define_shared_variable
 * - define_shared_constant
 */
public class DefinitionReader extends AstWalker {

    private final List<Definition> definitions = new ArrayList<>();

    public List<Definition> getDefinitions() {
        return Collections.unmodifiableList(this.definitions);
    }

    @Override
    protected void walkPostMethodDefinition(final AstNode node) {
        final MethodDefinitionParser parser = new MethodDefinitionParser(node);
        final List<Definition> parsedDefinitions = parser.parseDefinitions();
        this.definitions.addAll(parsedDefinitions);
    }

    @Override
    protected void walkPostVariableDefinitionStatement(final AstNode node) {
        if (GlobalDefinitionParser.isGlobalDefinition(node)) {
            this.handleGlobalDefinition(node);
        }
    }

    private void handleGlobalDefinition(final AstNode node) {
        final GlobalDefinitionParser parser = new GlobalDefinitionParser(node);
        final List<Definition> parsedDefinitions = parser.parseDefinitions();
        this.definitions.addAll(parsedDefinitions);
    }

    @Override
    protected void walkPostProcedureInvocation(final AstNode node) {
        if (DefPackageParser.isDefPackage(node)) {
            this.handleDefPackage(node);
        } else if (DefEnumerationParser.isDefEnumeration(node)) {
            this.handleDefEnumeration(node);
        } else if (DefSlottedExemplarParser.isDefSlottedExemplar(node)) {
            this.handleDefSlottedExemplar(node);
        } else if (DefIndexedExemplarParser.isDefIndexedExemplar(node)) {
            this.handleDefIndexedExemplar(node);
        } else if (DefMixinParser.isDefMixin(node)) {
            this.handleDefMixin(node);
        } else if (DefineBinaryOperatorCaseParser.isBinaryOperatorCase(node)) {
            this.handleDefineBinaryOperatorCase(node);
        }
    }

    @Override
    protected void walkPostMethodInvocation(final AstNode node) {
        // Some more sanity: Directly under top level.
        final AstNode statementNode = node.getFirstAncestor(MagikGrammar.STATEMENT);
        if (statementNode != null
            && statementNode.getParent() != null
            && statementNode.getParent().isNot(MagikGrammar.MAGIK)) {
            return;
        }

        if (DefineSlotAccessParser.isDefineSlotAccess(node)) {
            this.handleDefineSlotAccess(node);
        } else if (DefineSharedVariableParser.isDefineSharedVariable(node)) {
            this.handleDefineSharedVariable(node);
        } else if (DefineSharedConstantParser.isDefineSharedConstant(node)) {
            this.handleDefineSharedConstant(node);
        }
    }

    private void handleDefPackage(final AstNode node) {
        final DefPackageParser parser = new DefPackageParser(node);
        final List<Definition> parsedDefinitions = parser.parseDefinitions();
        this.definitions.addAll(parsedDefinitions);
    }

    private void handleDefEnumeration(final AstNode node) {
        final DefEnumerationParser parser = new DefEnumerationParser(node);
        final List<Definition> parsedDefinitions = parser.parseDefinitions();
        this.definitions.addAll(parsedDefinitions);
    }

    private void handleDefSlottedExemplar(final AstNode node) {
        final DefSlottedExemplarParser parser = new DefSlottedExemplarParser(node);
        final List<Definition> parsedDefinitions = parser.parseDefinitions();
        this.definitions.addAll(parsedDefinitions);
    }

    private void handleDefIndexedExemplar(final AstNode node) {
        final DefIndexedExemplarParser parser = new DefIndexedExemplarParser(node);
        final List<Definition> parsedDefinitions = parser.parseDefinitions();
        this.definitions.addAll(parsedDefinitions);
    }

    private void handleDefMixin(final AstNode node) {
        final DefMixinParser parser = new DefMixinParser(node);
        final List<Definition> parsedDefinitions = parser.parseDefinitions();
        this.definitions.addAll(parsedDefinitions);
    }

    private void handleDefineBinaryOperatorCase(final AstNode node) {
        final DefineBinaryOperatorCaseParser parser = new DefineBinaryOperatorCaseParser(node);
        final List<Definition> parsedDefinitions = parser.parseDefinitions();
        this.definitions.addAll(parsedDefinitions);
    }

    private void handleDefineSlotAccess(final AstNode node) {
        final DefineSlotAccessParser parser = new DefineSlotAccessParser(node);
        final List<Definition> parsedDefinitions = parser.parseDefinitions();
        this.definitions.addAll(parsedDefinitions);
    }

    private void handleDefineSharedVariable(final AstNode node) {
        final DefineSharedVariableParser parser = new DefineSharedVariableParser(node);
        final List<Definition> parsedDefinitions = parser.parseDefinitions();
        this.definitions.addAll(parsedDefinitions);
    }

    private void handleDefineSharedConstant(final AstNode node) {
        final DefineSharedConstantParser parser = new DefineSharedConstantParser(node);
        final List<Definition> parsedDefinitions = parser.parseDefinitions();
        this.definitions.addAll(parsedDefinitions);
    }

}