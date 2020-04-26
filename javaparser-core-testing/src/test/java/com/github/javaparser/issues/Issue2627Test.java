package com.github.javaparser.issues;

import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue2627Test {

    private void assertMethodInExpectedLines(MethodDeclaration md, int expectedStartLine, int expectedEndLine) {
        Range range = md.getRange().get();
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
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops_minimal.java");
        assertMethodInExpectedLines(
                getFirstMethodDeclarationByName(cu, "batchToSpace"),
                796,
                799
        );
    }

    @Test
    public void method_minimal_batchToSpaceNd() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops_minimal.java");
        MethodDeclaration node = getFirstMethodDeclarationByName(cu, "batchToSpaceNd");
        assertMethodInExpectedLines(node, 911, 914);
    }

    @Test
    public void method_minimal_placeHolder() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops_minimal.java");
        MethodDeclaration node = getFirstMethodDeclarationByName(cu, "placeholder");
        assertMethodInExpectedLines(node, 3686, 3689);
    }

    @Test
    public void method_original_batchToSpace() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops.java");
        MethodDeclaration node = getFirstMethodDeclarationByName(cu, "batchToSpace");
        assertMethodInExpectedLines(node, 786, 799);
    }

    @Test
    public void method_original_batchToSpaceNd() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops.java");
        MethodDeclaration node = getFirstMethodDeclarationByName(cu, "batchToSpaceNd");
        assertMethodInExpectedLines(node, 911, 914);
    }

    @Test
    public void method_original_placeHolder() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops.java");
        MethodDeclaration node = getFirstMethodDeclarationByName(cu, "placeholder");
        assertMethodInExpectedLines(node, 3686, 3689);
    }

}
