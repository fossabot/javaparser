package com.github.javaparser.symbolsolver;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
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

    private Log.Adapter testAdapter = new Log.StandardOutStandardErrorAdapter();


    @BeforeEach
    void setAdapter() {
        Log.setAdapter(testAdapter);
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
        Log.info("lambdas = " + lambdas);
        Log.info(lambdas.get(0).calculateResolvedType().toString());
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
                "        ClassMetric<Double> fieldName_ClassMetric2 = lambdaParam2 -> {\n" +
                "            return 0.0;\n" +
                "        };\n" +
                "        return 0;\n" +
                "    };\n" +
                "\n" +
                "}\n" +
                "\n" +
                "@FunctionalInterface\n" +
                "interface ClassMetric<T> extends Function<String, T> {\n" +
                "    @Override\n" +
                "    T apply(String c);\n" +
                "}\n";

        CompilationUnit cu = getCu(sourceCode);
//        parse(sourceCode);

//        List<VariableDeclarator> fds = cu.findAll(VariableDeclarator.class);
//        fds.forEach(fd -> {
//            Log.info("");
//            Log.info("----VARIABLE DECLARATOR----");
//            Log.info("");
//            Log.info("[VAR.DEC] fd = " + fd);
//            Log.info("[VAR.DEC] fd.getCommonType().asString() = " + fd.getType().asString());
//            Log.info("[VAR.DEC] fd.getCommonType().resolve() = " + fd.getType().resolve());
//
//            Log.info("");
//            ResolvedType resolvedFdType = fd.getType().resolve();
//            Log.info("[VAR.DEC] resolvedFdType.describe() = " + resolvedFdType.describe());
//            List<ResolvedReferenceType> directAncestors = resolvedFdType.asReferenceType().getDirectAncestors();
//            Log.info("[VAR.DEC] directAncestors = " + directAncestors);
//            directAncestors.forEach(resolvedReferenceType -> Log.info(resolvedReferenceType.describe()));
//        });


        List<LambdaExpr> lambdas = cu.findAll(LambdaExpr.class);
        lambdas.forEach(lambdaExpr -> {

            Log.info("");
            Log.info("----LAMBDA----");
            Log.info("");
            Log.info("[LAMBDA] lambdaExpr = " + lambdaExpr);
            Log.info("[LAMBDA] lambdaExpr.calculateResolvedType() = " + lambdaExpr.calculateResolvedType());
            Log.info("[LAMBDA] lambdaExpr.calculateResolvedType().describe() = " + lambdaExpr.calculateResolvedType().describe());
        });
    }

    @Test
    public void test_definingLambdaAsField_withIntermediateGeneric_NotNestedInterface() {
        String sourceCode = "" +
                "\n" +
                "import java.util.function.Function;\n" +
                "\n" +
                "public class Test {\n" +
                "\n" +
                "    ClassMetric<Integer> fieldName_ClassMetric;\n" +
                "\n" +
                "}\n" +
                "\n" +
//                "@FunctionalInterface\n" +
                "interface ClassMetric<T> extends Function<String, T> {\n" +
                "    @Override\n" +
                "    T apply(String c);\n" +
                "}\n";

        CompilationUnit cu = getCu(sourceCode);
//        parse(sourceCode);

//
//        List<ClassOrInterfaceDeclaration> coids = cu.findAll(ClassOrInterfaceDeclaration.class);
//        List<ClassOrInterfaceDeclaration> interfaces = coids.stream()
//                .filter(ClassOrInterfaceDeclaration::isInterface)
//                .collect(Collectors.toList());
//        Log.info("interfaces = " + interfaces);
//
//        interfaces.forEach(interfaceDeclaration -> {
//            Log.info("interfaceDeclaration.getTypeParameters() = " + interfaceDeclaration.getTypeParameters());
//        });

        List<FieldDeclaration> fds = cu.findAll(FieldDeclaration.class);
        fds.forEach(fd -> {
            Log.info("fd = " + fd);
            Log.info("fd.getCommonType().asString() = " + fd.getCommonType().asString());
            Log.info("fd.getCommonType().resolve() = " + fd.getCommonType().resolve());

            Log.info("");
            ResolvedType resolvedFdType = fd.getCommonType().resolve();
            Log.info("resolvedFdType.describe() = " + resolvedFdType.describe());

            Log.info("");
            List<ResolvedReferenceType> directAncestors = resolvedFdType.asReferenceType().getDirectAncestors();
            Log.info("directAncestors = " + directAncestors);
            directAncestors.forEach(resolvedReferenceType -> Log.info(resolvedReferenceType.describe()));
        });

//        List<LambdaExpr> lambdas = cu.findAll(LambdaExpr.class);
//        Log.info("lambdas = " + lambdas);
//        Log.info(lambdas.get(0).calculateResolvedType());
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
        Log.info("lambdas = " + lambdas);
        Log.info(lambdas.get(0).calculateResolvedType().toString());
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
//        Log.info(cu);

        return cu;
    }

    private void parse(String sourceCode) {
        CompilationUnit cu = getCu(sourceCode);

        List<MethodCallExpr> methodCalls = cu.findAll(MethodCallExpr.class);
        assumeFalse(methodCalls.isEmpty());
        for (int i = methodCalls.size() - 1; i >= 0; i--) {
            MethodCallExpr methodCallExpr = methodCalls.get(i);
            Log.info("");
            Log.info("[PARSE RESULT] methodCallExpr = " + methodCallExpr);
            Log.info("[PARSE RESULT] methodCallExpr.resolve() = " + methodCallExpr.resolve());
            Log.info("[PARSE RESULT] methodCallExpr.calculateResolvedType() = " + methodCallExpr.calculateResolvedType());
        }
    }

}
