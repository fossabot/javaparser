package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

public class BinaryExprContext extends AbstractJavaParserContext<BinaryExpr> {

    public BinaryExprContext(BinaryExpr wrappedNode, TypeSolver typeSolver) {
        super(wrappedNode, typeSolver);
    }

    // TODO: Add in mechanism where any PatternExpr on the left branch becomes available within the right branch

}
