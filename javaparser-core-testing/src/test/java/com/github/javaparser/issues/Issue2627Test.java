package com.github.javaparser.issues;

import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Issue2627Test {

    private Optional<MethodDeclaration> getFirstMethodDeclarationByName(CompilationUnit cu, String name) {
        return cu.findAll(MethodDeclaration.class).stream()
                .filter(n -> name.equals(n.getNameAsString()))
                .findFirst();
    }

    @Test
    public void method_original_batchToSpace() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops.java");

        Optional<MethodDeclaration> node = getFirstMethodDeclarationByName(cu, "batchToSpace");

        assertTrue(node.isPresent());
        Range range = node.get().getRange().get();

        System.out.println("range = " + range);

        assertEquals(796, range.begin.line);
        assertEquals(799, range.end.line);

    }

    @Test
    public void method_original_batchToSpaceNd() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops.java");

        Optional<MethodDeclaration> node = getFirstMethodDeclarationByName(cu, "batchToSpaceNd");

        assertTrue(node.isPresent());
        Range range = node.get().getRange().get();

        System.out.println("range = " + range);

        assertEquals(911, range.begin.line);
        assertEquals(914, range.end.line);

    }

    @Test
    public void method_original_placeHolder() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops.java");

        Optional<MethodDeclaration> node = getFirstMethodDeclarationByName(cu, "placeholder");

        assertTrue(node.isPresent());
        Range range = node.get().getRange().get();

        System.out.println("range = " + range);

        assertEquals(3686, range.begin.line);
        assertEquals(3689, range.end.line);

    }

    @Test
    public void method_minimal_batchToSpace() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops_minimal.java");

        Optional<MethodDeclaration> node = getFirstMethodDeclarationByName(cu, "batchToSpace");

        assertTrue(node.isPresent());
        Range range = node.get().getRange().get();

        System.out.println("range = " + range);

        assertEquals(796, range.begin.line);
        assertEquals(799, range.end.line);

    }

    @Test
    public void method_minimal_batchToSpaceNd() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops_minimal.java");

        Optional<MethodDeclaration> node = getFirstMethodDeclarationByName(cu, "batchToSpaceNd");

        assertTrue(node.isPresent());
        Range range = node.get().getRange().get();

        System.out.println("range = " + range);

        assertEquals(911, range.begin.line);
        assertEquals(914, range.end.line);

    }

    @Test
    public void method_minimal_placeHolder() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops_minimal.java");

        Optional<MethodDeclaration> node = getFirstMethodDeclarationByName(cu, "placeholder");

        assertTrue(node.isPresent());
        Range range = node.get().getRange().get();

        System.out.println("range = " + range);

        assertEquals(3686, range.begin.line);
        assertEquals(3689, range.end.line);

    }

}
