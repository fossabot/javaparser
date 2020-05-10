package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.PatternExpr;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;

import java.util.List;
import java.util.Optional;

public class BinaryExprContext extends AbstractJavaParserContext<BinaryExpr> {

    public BinaryExprContext(BinaryExpr wrappedNode, TypeSolver typeSolver) {
        super(wrappedNode, typeSolver);
    }

    // TODO: Add in mechanism where any PatternExpr on the left branch becomes available within the right branch


    @Override
    public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name) {
        BinaryExpr binaryExpr = wrappedNode;

        // Not applicable for || -- Pattern will only be created and used on the right when instanceof evaluates to true
        if (binaryExpr.getOperator() == BinaryExpr.Operator.AND) {
            List<PatternExpr> patternExprs = binaryExpr.getLeft().findAll(PatternExpr.class);
            for (PatternExpr patternExpr : patternExprs) {
                if (patternExpr.getName().getIdentifier().equals(name)) {
                    return SymbolReference.solved(JavaParserSymbolDeclaration.patternVar(patternExpr, typeSolver));
                }
            }
        }

        return SymbolReference.unsolved(ResolvedValueDeclaration.class);
    }

    @Override
    public Optional<Value> solveSymbolAsValue(String name) {
        BinaryExpr binaryExpr = wrappedNode;

        // Not applicable for || -- Pattern will only be created and used on the right when instanceof evaluates to true
        if (binaryExpr.getOperator() == BinaryExpr.Operator.EQUALS) {
            List<PatternExpr> patternExprs = binaryExpr.getLeft().findAll(PatternExpr.class);
            for (PatternExpr patternExpr : patternExprs) {
                if (patternExpr.getName().getIdentifier().equals(name)) {
                    JavaParserSymbolDeclaration decl = JavaParserSymbolDeclaration.patternVar(patternExpr, typeSolver);
                    return Optional.of(Value.from(decl));
                }
            }
        }

        return Optional.empty();
    }
}
