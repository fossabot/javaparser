package com.github.javaparser.issues;

import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue2627Test {

    private void assertMethodInExpectedLines(MethodDeclaration md, int expectedStartLine, int expectedEndLine) {
        Range range = md.getRange().get();
        System.out.println("range = " + range);

        assertEquals(796, range.begin.line);
        assertEquals(799, range.end.line);
    }

    private Optional<MethodDeclaration> getFirstMethodDeclarationByName(CompilationUnit cu, String name) {
        return cu.findAll(MethodDeclaration.class).stream()
                .filter(n -> name.equals(n.getNameAsString()))
                .findFirst();
    }

    @Test
    public void method_minimal_batchToSpace() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops_minimal.java");
        Optional<MethodDeclaration> node = getFirstMethodDeclarationByName(cu, "batchToSpace");
        assertMethodInExpectedLines(node.get(), 796, 799);
    }

    @Test
    public void method_minimal_batchToSpaceNd() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops_minimal.java");
        Optional<MethodDeclaration> node = getFirstMethodDeclarationByName(cu, "batchToSpaceNd");
        assertMethodInExpectedLines(node.get(), 911, 914);
    }

    @Test
    public void method_minimal_placeHolder() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops_minimal.java");
        Optional<MethodDeclaration> node = getFirstMethodDeclarationByName(cu, "placeholder");
        assertMethodInExpectedLines(node.get(), 3686, 3689);
    }

    @Test
    public void method_original_batchToSpace() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops.java");
        Optional<MethodDeclaration> node = getFirstMethodDeclarationByName(cu, "batchToSpace");
        assertMethodInExpectedLines(node.get(), 786, 799);
    }

    @Test
    public void method_original_batchToSpaceNd() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops.java");
        Optional<MethodDeclaration> node = getFirstMethodDeclarationByName(cu, "batchToSpaceNd");
        assertMethodInExpectedLines(node.get(), 911, 914);
    }

    @Test
    public void method_original_placeHolder() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops.java");
        Optional<MethodDeclaration> node = getFirstMethodDeclarationByName(cu, "placeholder");
        assertMethodInExpectedLines(node.get(), 3686, 3689);
    }

}
