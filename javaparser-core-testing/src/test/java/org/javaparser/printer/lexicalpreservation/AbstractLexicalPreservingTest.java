/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2019 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */

package org.javaparser.printer.lexicalpreservation;

import org.javaparser.ast.CompilationUnit;
import org.javaparser.ast.Node;
import org.javaparser.ast.expr.Expression;

import java.io.IOException;

import static org.javaparser.StaticJavaParser.parse;
import static org.javaparser.StaticJavaParser.parseExpression;
import static org.javaparser.StaticJavaParser.parseVariableDeclarationExpr;
import static org.javaparser.utils.TestUtils.readResource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractLexicalPreservingTest {

    protected CompilationUnit cu;
    protected Expression expression;

    protected void considerCode(String code) {
        cu = LexicalPreservingPrinter.setup(parse(code));
    }

    protected void considerExpression(String code) {
        expression = LexicalPreservingPrinter.setup(parseExpression(code));
    }
    
    protected void considerVariableDeclaration(String code) {
        expression = LexicalPreservingPrinter.setup(parseVariableDeclarationExpr(code));
    }

    protected String considerExample(String resourceName) throws IOException {
        String code = readExample(resourceName);
        considerCode(code);
        return code;
    }

    protected String readExample(String resourceName) throws IOException {
        return readResource("org/javaparser/lexical_preservation_samples/" + resourceName + ".java.txt");
    }

    protected void assertTransformed(String exampleName, Node node) throws IOException {
        String expectedCode = readExample(exampleName + "_expected");
        String actualCode = LexicalPreservingPrinter.print(node);
        assertEquals(expectedCode, actualCode);
    }

    protected void assertUnchanged(String exampleName) throws IOException {
        String code = considerExample(exampleName + "_original");
        assertEquals(code, LexicalPreservingPrinter.print(cu != null ? cu : expression));
    }

    protected void assertTransformedToString(String expectedPartialCode, Node node) {
        String actualCode = LexicalPreservingPrinter.print(node);
        assertEquals(expectedPartialCode, actualCode);
    }

}
