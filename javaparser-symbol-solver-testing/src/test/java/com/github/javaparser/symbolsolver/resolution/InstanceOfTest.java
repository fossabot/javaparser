package com.github.javaparser.symbolsolver.resolution;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StringProvider;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InstanceOfTest {


    private static final String CODE_INSTANCEOF_PATTERN_IF_ELSE = "" +
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

    private static final String CODE_INSTANCEOF_PATTERN_IF = "" +
            "class X {\n" +
            "  public X() {\n" +
            "    boolean result;;\n" +
            "    String obj = \"abc\";\n" +
            "    if (!(obj instanceof String s)) {\n" +
            "        result = s.contains(\"b\");\n" +
            "    }\n" +
            "  }\n" +
            " }\n";

    private TypeSolver typeSolver = new ReflectionTypeSolver();


    @Test
    public void givenInstanceOfPattern_thenCorrectNumberOfMethodCalls() {
        final CompilationUnit cu = parseWithTypeSolver(CODE_INSTANCEOF_PATTERN_IF_ELSE);
        final List<MethodCallExpr> methodCalls = cu.findAll(MethodCallExpr.class);

        System.out.println(methodCalls);
        assertEquals(2, methodCalls.size());
    }

    @Disabled
    @Test
    public void givenInstanceOfPattern_whenSolvingInvalidNotInScope_thenFails() {
        final CompilationUnit cu = parseWithTypeSolver(ParserConfiguration.LanguageLevel.JAVA_14, CODE_INSTANCEOF_PATTERN_IF_ELSE);
        final List<MethodCallExpr> methodCalls = cu.findAll(MethodCallExpr.class);
        assertEquals(2, methodCalls.size());

        MethodCallExpr inScopeMethodCall = methodCalls.get(0);
        MethodCallExpr outOfScopeMethodCall = methodCalls.get(1);

        // Expected to not be able to resolve s, as out of scope within an else block.
        assertThrows(UnsolvedSymbolException.class, () -> {
            final ResolvedMethodDeclaration resolve = outOfScopeMethodCall.resolve();
            System.out.println("resolve = " + resolve);
        });
    }

    @Test
    public void givenInstanceOfPattern_whenSolvingValidInScope_thenSuccessful() {
        final CompilationUnit cu = parseWithTypeSolver(ParserConfiguration.LanguageLevel.JAVA_14, CODE_INSTANCEOF_PATTERN_IF_ELSE);
        final List<MethodCallExpr> methodCalls = cu.findAll(MethodCallExpr.class);
        assertEquals(2, methodCalls.size());

        MethodCallExpr inScopeMethodCall = methodCalls.get(0);
        MethodCallExpr outOfScopeMethodCall = methodCalls.get(1);


        // Resolving the method call .contains()
        final ResolvedMethodDeclaration resolve = inScopeMethodCall.resolve();
        System.out.println("resolve.getQualifiedSignature() = " + resolve.getQualifiedSignature());

        assertEquals("java.lang.String.contains(java.lang.CharSequence)", resolve.getQualifiedSignature());
        assertEquals("boolean", resolve.getReturnType().describe());
        assertEquals("contains", resolve.getName());
        assertEquals(1, resolve.getNumberOfParams());
        assertEquals("contains(java.lang.CharSequence)", resolve.getSignature());


        // Resolving the variable `s`
        assertTrue(inScopeMethodCall.getScope().isPresent());
        final Expression expression = inScopeMethodCall.getScope().get();

        final ResolvedType resolvedType = expression.calculateResolvedType();
        assertEquals("java.lang.String", resolvedType.describe());


    }


    private CompilationUnit parseWithTypeSolver(String code) {
        return parseWithTypeSolver(null, code);
    }

    private CompilationUnit parseWithTypeSolver(ParserConfiguration.LanguageLevel languageLevel, String code) {
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setSymbolResolver(new JavaSymbolSolver(typeSolver));

        if (languageLevel != null) {
            parserConfiguration.setLanguageLevel(languageLevel);
        }

        return new JavaParser(parserConfiguration)
                .parse(ParseStart.COMPILATION_UNIT, new StringProvider(code))
                .getResult().get();
    }

}
