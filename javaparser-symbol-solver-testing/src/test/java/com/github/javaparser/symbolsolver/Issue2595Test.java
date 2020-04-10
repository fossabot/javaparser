package com.github.javaparser.symbolsolver;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.javaparser.Providers.provider;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class Issue2595Test {

    @BeforeEach
    void setAdapter() {
//        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
    }

    @AfterEach
    void resetAdapter() {
        Log.setAdapter(new Log.SilentAdapter());
    }

    @Test
    public void test_definingLambdaAsField() {
        String sourceCode = "" +
                "\n" +
                "import java.util.function.Function;\n" +
                "\n" +
                "public class Test {\n" +
                "\n" +
                "    ClassMetric fieldName_ClassMetric = lambdaParam -> {\n" +
                "        return 0;\n" +
                "    };\n" +
                "\n" +
                "    @FunctionalInterface\n" +
                "    public interface ClassMetric extends Function<String, Integer> {\n" +
                "        @Override\n" +
                "        Integer apply(String c);\n" +
                "    }\n" +
                "\n" +
                "}\n";

        CompilationUnit cu = getCu(sourceCode);
//        parse(sourceCode);

        List<LambdaExpr> lambdas = cu.findAll(LambdaExpr.class);
        System.out.println("lambdas = " + lambdas);
        System.out.println(lambdas.get(0).calculateResolvedType());
    }


    @Test
    public void test_definingLambdaAsField_withIntermediateGeneric() {
        String sourceCode = "" +
                "\n" +
                "import java.util.function.Function;\n" +
                "\n" +
                "public class Test {\n" +
                "\n" +
                "    ClassMetric<Integer> fieldName_ClassMetric = lambdaParam -> {\n" +
                "        return 0;\n" +
                "    };\n" +
                "\n" +
                "    @FunctionalInterface\n" +
                "    public interface ClassMetric<T> extends Function<String, T> {\n" +
                "        @Override\n" +
                "        T apply(String c);\n" +
                "    }\n" +
                "\n" +
                "}\n";

        CompilationUnit cu = getCu(sourceCode);
//        parse(sourceCode);

        List<LambdaExpr> lambdas = cu.findAll(LambdaExpr.class);
        System.out.println("lambdas = " + lambdas);
        System.out.println(lambdas.get(0).calculateResolvedType());
    }


    @Test
    public void test_definingLambdaAsLocalVariable() {
        String sourceCode = "" +
                "\n" +
                "import java.util.function.Function;\n" +
                "\n" +
                "public class Test {\n" +
                "\n" +
                "    public void x() {\n" +
                "        ClassMetric fdp = c -> {\n" +
                "            return 0;\n" +
                "        };\n" +
                "    }\n" +
                "\n" +
                "    @FunctionalInterface\n" +
                "    public interface ClassMetric extends Function<String, Integer> {\n" +
                "        @Override\n" +
                "        Integer apply(String c);\n" +
                "    }\n" +
                "\n" +
                "}\n";

        CompilationUnit cu = getCu(sourceCode);
//        parse(sourceCode);

        List<LambdaExpr> lambdas = cu.findAll(LambdaExpr.class);
        System.out.println("lambdas = " + lambdas);
        System.out.println(lambdas.get(0).calculateResolvedType());
    }

    @Test
    public void issue2595ImplicitTypeLambdaTest() {
        String sourceCode = "" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.function.Function;\n" +
                "\n" +
                "public class Test {\n" +
                "\n" +
                "    ClassMetric<Integer> fdp = c -> {\n" +
                "        List<String> classFieldNames = getAllClassFieldNames(c);\n" +
                "        return classFieldNames.size();\n" +
                "    };\n" +
                "\n" +
                "\n" +
                "    private List<String> getAllClassFieldNames(final String c) {\n" +
                "        return new ArrayList<>();\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    @FunctionalInterface\n" +
                "    public interface ClassMetric<T> extends Function<String, T> {\n" +
                "        @Override\n" +
                "        T apply(String c);\n" +
                "    }\n" +
                "\n" +
                "}\n";

        parse(sourceCode);
    }

    @Test
    public void issue2595ExplicitTypeLambdaTest() {
        String sourceCode = "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.function.Function;\n" +
                "\n" +
                "public class TestIssue2595 {\n" +
                "    ClassMetric fdp = (String c) -> {\n" +
                "        List<String> classFieldNames = getAllClassFieldNames(c);\n" +
                "        return classFieldNames.size();\n" +
                "    };\n" +
                "    \n" +
                "\n" +
                "    private List<String> getAllClassFieldNames(final String c) {\n" +
                "        return new ArrayList<>();\n" +
                "    }\n" +
                "\n" +
                "    @FunctionalInterface\n" +
                "    public interface ClassMetric extends Function<String, Integer> {\n" +
                "        @Override\n" +
                "        Integer apply(String c);\n" +
                "    }\n" +
                "}";

        parse(sourceCode);
    }

    @Test
    public void issue2595NoParameterLambdaTest() {
        String sourceCode = "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "\n" +
                "public class TestIssue2595 {\n" +
                "    ClassMetric fdp = () -> {\n" +
                "        List<String> classFieldNames = getAllClassFieldNames();\n" +
                "        return classFieldNames.size();\n" +
                "    };\n" +
                "\n" +
                "\n" +
                "    private List<String> getAllClassFieldNames() {\n" +
                "        return new ArrayList<>();\n" +
                "    }\n" +
                "\n" +
                "    @FunctionalInterface\n" +
                "    public interface ClassMetric {\n" +
                "        Integer apply();\n" +
                "    }\n" +
                "}";

        parse(sourceCode);
    }

    @Test
    public void issue2595AnonymousInnerClassTest() {
        String sourceCode = "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.function.Function;\n" +
                "\n" +
                "public class TestIssue2595 {\n" +
                "    ClassMetric fdp = new ClassMetric() {\n" +
                "        @Override\n" +
                "        public Integer apply(String c) {\n" +
                "            List<String> classFieldNames = getAllClassFieldNames(c);\n" +
                "            return classFieldNames.size();\n" +
                "        }\n" +
                "    };\n" +
                "\n" +
                "    private List<String> getAllClassFieldNames(final String c) {\n" +
                "        return new ArrayList<>();\n" +
                "    }\n" +
                "\n" +
                "    @FunctionalInterface\n" +
                "    public interface ClassMetric extends Function<String, Integer> {\n" +
                "        @Override\n" +
                "        Integer apply(String c);\n" +
                "    }\n" +
                "}";

        parse(sourceCode);
    }

    private CompilationUnit getCu(String sourceCode) {
        TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());
        ParserConfiguration configuration = new ParserConfiguration().setSymbolResolver(new JavaSymbolSolver(typeSolver));
        JavaParser javaParser = new JavaParser(configuration);

        ParseResult<CompilationUnit> result = javaParser.parse(ParseStart.COMPILATION_UNIT, provider(sourceCode));
        assumeTrue(result.isSuccessful());
        assumeTrue(result.getResult().isPresent());

        CompilationUnit cu = result.getResult().get();
//        System.out.println(cu);

        return cu;
    }

    private void parse(String sourceCode) {
        CompilationUnit cu = getCu(sourceCode);

        List<MethodCallExpr> methodCalls = cu.findAll(MethodCallExpr.class);
        assumeFalse(methodCalls.isEmpty());
        for (int i = methodCalls.size() - 1; i >= 0; i--) {
            MethodCallExpr methodCallExpr = methodCalls.get(i);
            System.out.println();
            System.out.println("methodCallExpr = " + methodCallExpr);
            System.out.println("methodCallExpr.resolve() = " + methodCallExpr.resolve());
            System.out.println("methodCallExpr.calculateResolvedType() = " + methodCallExpr.calculateResolvedType());
        }
    }

}
