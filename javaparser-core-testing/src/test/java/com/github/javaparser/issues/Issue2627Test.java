package com.github.javaparser.issues;

import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue2627Test {

    private static final String RESOURCE_PATH_STRING_MINIMAL = "com/github/javaparser/issue_samples/issue_2627/Ops_minimal.java";
    private static final String RESOURCE_PATH_STRING_ORIGINAL = "com/github/javaparser/issue_samples/issue_2627/Ops.java";

    private void assertMethodInExpectedLines(CompilationUnit cu, String name, int expectedStartLine, int expectedEndLine) {
        MethodDeclaration node = getFirstMethodDeclarationByName(cu, name);

        Range range = node.getRange().get();
        System.out.println("range = " + range);

        assertEquals(796, range.begin.line);
        assertEquals(799, range.end.line);
    }

    private MethodDeclaration getFirstMethodDeclarationByName(CompilationUnit cu, String name) {
        return cu.findAll(MethodDeclaration.class).stream()
                .filter(n -> name.equals(n.getNameAsString()))
                .findFirst()
                .get();
    }

    @Test
    public void method_minimal_batchToSpace() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource(RESOURCE_PATH_STRING_MINIMAL);
        assertMethodInExpectedLines(cu, "batchToSpace", 796, 799);
    }

    @Test
    public void method_minimal_batchToSpaceNd() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource(RESOURCE_PATH_STRING_MINIMAL);
        assertMethodInExpectedLines(cu, "batchToSpaceNd", 911, 914);
    }

    @Test
    public void method_minimal_placeHolder() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource(RESOURCE_PATH_STRING_MINIMAL);
        assertMethodInExpectedLines(cu, "placeholder", 3686, 3689);
    }

    @Test
    public void method_original_batchToSpace() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource(RESOURCE_PATH_STRING_ORIGINAL);
        assertMethodInExpectedLines(cu, "batchToSpace", 786, 799);
    }

    @Test
    public void method_original_batchToSpaceNd() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource(RESOURCE_PATH_STRING_ORIGINAL);
        assertMethodInExpectedLines(cu, "batchToSpaceNd", 911, 914);
    }

    @Test
    public void method_original_placeHolder() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource(RESOURCE_PATH_STRING_ORIGINAL);
        assertMethodInExpectedLines(cu, "placeholder", 3686, 3689);
    }

}
