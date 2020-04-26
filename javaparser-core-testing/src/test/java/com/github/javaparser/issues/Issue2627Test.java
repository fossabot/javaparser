package com.github.javaparser.issues;

import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue2627Test {

    private static final String RESOURCE_PATH_STRING_CR = "com/github/javaparser/issue_samples/issue_2627/Ops_cr.java";
    private static final String RESOURCE_PATH_STRING_LF = "com/github/javaparser/issue_samples/issue_2627/Ops_lf.java";
    private static final String RESOURCE_PATH_STRING_CRLF = "com/github/javaparser/issue_samples/issue_2627/Ops_crlf.java";
    private static final String RESOURCE_PATH_STRING_MINIMAL = "com/github/javaparser/issue_samples/issue_2627/Ops_minimal.java";
    private static final String RESOURCE_PATH_STRING_ORIGINAL = "com/github/javaparser/issue_samples/issue_2627/Ops.java";

    private static Stream<Arguments> arguments_minimal() {
        return Stream.of(
                Arguments.of("methodA", 193, 194),
                Arguments.of("methodB", 98, 99),
                Arguments.of("methodC", 3, 4)
        );
    }

    private static Stream<Arguments> arguments_original() {
        return Stream.of(
                Arguments.of("batchToSpace", 796, 799),
                Arguments.of("batchToSpaceNd", 911, 914),
                Arguments.of("placeholder", 3686, 3689)
        );
    }

    private void assertMethodInExpectedLines(CompilationUnit cu, String name, int expectedStartLine, int expectedEndLine) {
        final Range cuRange = cu.getRange().get();
        System.out.println("cu.getRange().get() = " + cuRange);

        int lineCount = cuRange.end.line - cuRange.begin.line;
        System.out.println("lineCount = " + lineCount);
//        assertEquals(145, lineCount);

        MethodDeclaration node = getFirstMethodDeclarationByName(cu, name);
        Range range = node.getRange().get();
        System.out.println("range (" + name + ") = " + range);

        assertEquals(expectedStartLine, range.begin.line);
        assertEquals(expectedEndLine, range.end.line);
    }

    private MethodDeclaration getFirstMethodDeclarationByName(CompilationUnit cu, String name) {
        return cu.findAll(MethodDeclaration.class).stream()
                .filter(n -> name.equals(n.getNameAsString()))
                .findFirst()
                .get();
    }

    @ParameterizedTest
    @MethodSource("arguments_minimal")
    public void method_minimal(String name, int expectedStart, int expectedEnd) throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource(RESOURCE_PATH_STRING_MINIMAL);
        assertMethodInExpectedLines(cu, name, expectedStart, expectedEnd);
    }


    @ParameterizedTest
    @MethodSource("arguments_original")
    public void method_original(String name, int expectedStart, int expectedEnd) throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource(RESOURCE_PATH_STRING_ORIGINAL);
        assertMethodInExpectedLines(cu, name, expectedStart, expectedEnd);
    }

    @ParameterizedTest
    @MethodSource("arguments_original")
    public void method_original_cr(String name, int expectedStart, int expectedEnd) throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource(RESOURCE_PATH_STRING_CR);
        assertMethodInExpectedLines(cu, name, expectedStart, expectedEnd);
    }

    @ParameterizedTest
    @MethodSource("arguments_original")
    public void method_original_crlf(String name, int expectedStart, int expectedEnd) throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource(RESOURCE_PATH_STRING_CRLF);
        assertMethodInExpectedLines(cu, name, expectedStart, expectedEnd);
    }

    @ParameterizedTest
    @MethodSource("arguments_original")
    public void method_original_lf(String name, int expectedStart, int expectedEnd) throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource(RESOURCE_PATH_STRING_LF);
        assertMethodInExpectedLines(cu, name, expectedStart, expectedEnd);
    }

}
