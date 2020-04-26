package com.github.javaparser.issues;

import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Issue2627Test {

    @Test
    public void test() throws IOException {
        CompilationUnit cu = StaticJavaParser.parseResource("com/github/javaparser/issue_samples/issue_2627/Ops.java");

        Optional<MethodDeclaration> node = cu.findAll(MethodDeclaration.class).stream()
                .filter(n -> "placeholder".equals(n.getNameAsString()))
                .findFirst();

        assertTrue(node.isPresent());
        Range range = node.get().getRange().get();

        System.out.println("range = " + range);

        assertEquals(3686, range.begin.line);
        assertEquals(3689, range.end.line);

    }

}
