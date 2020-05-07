package com.github.javaparser.symbolsolver.resolution;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StringProvider;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InstanceOfTest {


    private final String x = "" +
            "class X {\n" +
            "  public X() {\n" +
            "    boolean result;;\n" +
            "    String obj = \"abc\";\n" +
            "    if (!(obj instanceof String s)) {\n" +
            "        result = s.contains(\"b\");\n" +
            "    } else {\n" +
            "        result = s.contains(\"b\");\n" +
            "    }\n" +
            "  }\n" +
            " }\n";


    @Test
    public void givenInstanceOfPattern_thenCorrectNumberOfMethodCalls() {
        final CompilationUnit cu = parseWithTypeSolver(x);
        final List<MethodCallExpr> methodCalls = cu.findAll(MethodCallExpr.class);

        System.out.println(methodCalls);
        assertEquals(2, methodCalls.size());
    }

    @Test
    public void givenInstanceOfPattern_whenSolvingInvalidNotInScope_thenFails() {
        final CompilationUnit cu = parseWithTypeSolver(x);
        final List<MethodCallExpr> methodCalls = cu.findAll(MethodCallExpr.class);
        assertEquals(2, methodCalls.size());

        // FIXME: Should NOT be able to resolve this -- not in scope...
        final ResolvedMethodDeclaration resolve = methodCalls.get(0).resolve();
        System.out.println("resolve.getQualifiedSignature() = " + resolve.getQualifiedSignature());

        assertTrue(false, "FIXME....");

    }

    @Test
    public void givenInstanceOfPattern_whenSolvingValidInScope_thenSuccessful() {
        final CompilationUnit cu = parseWithTypeSolver(x);
        final List<MethodCallExpr> methodCalls = cu.findAll(MethodCallExpr.class);

        assertEquals(2, methodCalls.size());

        // FIXME: Should be able to resolve this.
        final ResolvedMethodDeclaration resolve = methodCalls.get(0).resolve();
        System.out.println("resolve.getQualifiedSignature() = " + resolve.getQualifiedSignature());
        assertEquals("java.lang.String", resolve.getQualifiedSignature());
    }


    private CompilationUnit parseWithTypeSolver(String code) {
        TypeSolver typeSolver = new ReflectionTypeSolver();
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setSymbolResolver(new JavaSymbolSolver(typeSolver));
        JavaParser javaParser = new JavaParser(parserConfiguration);
        return javaParser.parse(ParseStart.COMPILATION_UNIT, new StringProvider(code)).getResult().get();
    }

}
