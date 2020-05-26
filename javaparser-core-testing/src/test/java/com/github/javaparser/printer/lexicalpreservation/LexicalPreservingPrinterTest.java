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

package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.GeneratedJavaParserConstants;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Generated;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.javaparser.StaticJavaParser.parse;
import static com.github.javaparser.StaticJavaParser.parseClassOrInterfaceType;
import static com.github.javaparser.ast.Modifier.Keyword.PUBLIC;
import static com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter.NODE_TEXT_DATA;
import static com.github.javaparser.utils.TestUtils.assertEqualsNoEol;
import static com.github.javaparser.utils.Utils.EOL;
import static org.junit.jupiter.api.Assertions.*;

class LexicalPreservingPrinterTest extends AbstractLexicalPreservingTest {

    private NodeText getTextForNode(Node node) {
        return node.getData(NODE_TEXT_DATA);
    }

    //
    // Tests on TextNode definition
    //

    @Test
    void checkNodeTextCreatedForSimplestClass() {
        considerCode("class A {}");

        // CU
        assertEquals(1, getTextForNode(cu).numberOfElements());
        assertTrue(getTextForNode(cu).getTextElement(0) instanceof ChildTextElement);
        assertEquals(cu.getClassByName("A").get(),
                ((ChildTextElement) getTextForNode(cu).getTextElement(0)).getChild());

        // Class
        ClassOrInterfaceDeclaration classA = cu.getClassByName("A").get();
        assertEquals(7, getTextForNode(classA).numberOfElements());
        assertEquals("class", getTextForNode(classA).getTextElement(0).expand());
        assertEquals(" ", getTextForNode(classA).getTextElement(1).expand());
        assertEquals("A", getTextForNode(classA).getTextElement(2).expand());
        assertEquals(" ", getTextForNode(classA).getTextElement(3).expand());
        assertEquals("{", getTextForNode(classA).getTextElement(4).expand());
        assertEquals("}", getTextForNode(classA).getTextElement(5).expand());
        assertEquals("", getTextForNode(classA).getTextElement(6).expand());
        assertTrue(getTextForNode(classA).getTextElement(6) instanceof TokenTextElement);
        assertEquals(GeneratedJavaParserConstants.EOF,
                ((TokenTextElement) getTextForNode(classA).getTextElement(6)).getTokenKind());
    }

    @Test
    void checkNodeTextCreatedForField() {
        String code = "class A {int i;}";
        considerCode(code);

        ClassOrInterfaceDeclaration classA = cu.getClassByName("A").get();
        FieldDeclaration fd = classA.getFieldByName("i").get();
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(fd);
        assertEquals(Arrays.asList("int", " ", "i", ";"),
                nodeText.getElements().stream().map(TextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void checkNodeTextCreatedForVariableDeclarator() {
        String code = "class A {int i;}";
        considerCode(code);

        ClassOrInterfaceDeclaration classA = cu.getClassByName("A").get();
        FieldDeclaration fd = classA.getFieldByName("i").get();
        VariableDeclarator vd = fd.getVariables().get(0);
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(vd);
        assertEquals(Arrays.asList("i"),
                nodeText.getElements().stream().map(TextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void checkNodeTextCreatedForMethod() {
        String code = "class A {void foo(int p1, float p2) { }}";
        considerCode(code);

        ClassOrInterfaceDeclaration classA = cu.getClassByName("A").get();
        MethodDeclaration md = classA.getMethodsByName("foo").get(0);
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(md);
        assertEquals(Arrays.asList("void", " ", "foo", "(", "int p1", ",", " ", "float p2", ")", " ", "{ }"),
                nodeText.getElements().stream().map(TextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void checkNodeTextCreatedForMethodParameter() {
        String code = "class A {void foo(int p1, float p2) { }}";
        considerCode(code);

        ClassOrInterfaceDeclaration classA = cu.getClassByName("A").get();
        MethodDeclaration md = classA.getMethodsByName("foo").get(0);
        Parameter p1 = md.getParameterByName("p1").get();
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(p1);
        assertEquals(Arrays.asList("int", " ", "p1"),
                nodeText.getElements().stream().map(TextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void checkNodeTextCreatedForPrimitiveType() {
        String code = "class A {void foo(int p1, float p2) { }}";
        considerCode(code);

        ClassOrInterfaceDeclaration classA = cu.getClassByName("A").get();
        MethodDeclaration md = classA.getMethodsByName("foo").get(0);
        Parameter p1 = md.getParameterByName("p1").get();
        Type t = p1.getType();
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(t);
        assertEquals(Arrays.asList("int"),
                nodeText.getElements().stream().map(TextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void checkNodeTextCreatedForSimpleImport() {
        String code = "import a.b.c.D;";
        considerCode(code);

        ImportDeclaration imp = (ImportDeclaration) cu.getChildNodes().get(0);
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(imp);
        assertEquals(Arrays.asList("import", " ", "a.b.c.D", ";", ""),
                nodeText.getElements().stream().map(TextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void addedImportShouldBePrependedWithEOL() {
        considerCode("import a.A;" + EOL + "class X{}");

        cu.addImport("a.B");

        assertEqualsNoEol("import a.A;\nimport a.B;\nclass X{}", LexicalPreservingPrinter.print(cu));
    }

    @Test
    void checkNodeTextCreatedGenericType() {
        String code = "class A {ParseResult<T> result;}";
        considerCode(code);

        FieldDeclaration field = cu.getClassByName("A").get().getFieldByName("result").get();
        Node t = field.getCommonType();
        Node t2 = field.getVariable(0).getType();
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(field);
        assertEquals(Arrays.asList("ParseResult", "<", "T", ">", " ", "result", ";"),
                nodeText.getElements().stream().map(TextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void checkNodeTextCreatedAnnotationDeclaration() {
        String code = "public @interface ClassPreamble { String author(); }";
        considerCode(code);

        AnnotationDeclaration ad = cu.getAnnotationDeclarationByName("ClassPreamble").get();
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(ad);
        assertEquals(
                Arrays.asList("public", " ", "@", "interface", " ", "ClassPreamble", " ", "{", " ", "String author();",
                        " ", "}", ""),
                nodeText.getElements().stream().map(TextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void checkNodeTextCreatedAnnotationMemberDeclaration() {
        String code = "public @interface ClassPreamble { String author(); }";
        considerCode(code);

        AnnotationDeclaration ad = cu.getAnnotationDeclarationByName("ClassPreamble").get();
        AnnotationMemberDeclaration md = (AnnotationMemberDeclaration) ad.getMember(0);
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(md);
        assertEquals(Arrays.asList("String", " ", "author", "(", ")", ";"),
                nodeText.getElements().stream().map(TextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void checkNodeTextCreatedAnnotationMemberDeclarationWithArrayType() {
        String code = "public @interface ClassPreamble { String[] author(); }";
        considerCode(code);

        AnnotationDeclaration ad = cu.getAnnotationDeclarationByName("ClassPreamble").get();
        AnnotationMemberDeclaration md = (AnnotationMemberDeclaration) ad.getMember(0);
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(md);
        assertEquals(Arrays.asList("String[]", " ", "author", "(", ")", ";"),
                nodeText.getElements().stream().map(TextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void checkNodeTextCreatedAnnotationMemberDeclarationArrayType() {
        String code = "public @interface ClassPreamble { String[] author(); }";
        considerCode(code);

        AnnotationDeclaration ad = cu.getAnnotationDeclarationByName("ClassPreamble").get();
        AnnotationMemberDeclaration md = ad.getMember(0).asAnnotationMemberDeclaration();
        Type type = md.getType();
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(type);
        assertEquals(Arrays.asList("String", "[", "]"),
                nodeText.getElements().stream().map(TextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void checkNodeTextCreatedAnnotationMemberDeclarationWithComment() throws IOException {
        considerExample("AnnotationDeclaration_Example3_original");

        AnnotationMemberDeclaration md = cu.getAnnotationDeclarationByName("ClassPreamble").get().getMember(5)
                .asAnnotationMemberDeclaration();
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(md);
        assertEquals(Arrays.asList("String[]", " ", "reviewers", "(", ")", ";"),
                nodeText.getElements().stream().map(TextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void checkNodeTextCreatedArrayCreationLevelWithoutExpression() {
        considerExpression("new int[]");

        ArrayCreationExpr arrayCreationExpr = expression.asArrayCreationExpr();
        ArrayCreationLevel arrayCreationLevel = arrayCreationExpr.getLevels().get(0);
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(arrayCreationLevel);
        assertEquals(Arrays.asList("[", "]"),
                nodeText.getElements().stream().map(TextElement::expand).filter(e -> !e.isEmpty())
                        .collect(Collectors.toList()));
    }

    @Test
    void checkNodeTextCreatedArrayCreationLevelWith() {
        considerExpression("new int[123]");

        ArrayCreationExpr arrayCreationExpr = expression.asArrayCreationExpr();
        ArrayCreationLevel arrayCreationLevel = arrayCreationExpr.getLevels().get(0);
        NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(arrayCreationLevel);
        assertEquals(Arrays.asList("[", "123", "]"),
                nodeText.getElements().stream().map(TextElement::expand).filter(e -> !e.isEmpty())
                        .collect(Collectors.toList()));
    }

    //
    // Tests on findIndentation
    //

    @Test
    void findIndentationForAnnotationMemberDeclarationWithoutComment() throws IOException {
        considerExample("AnnotationDeclaration_Example3_original");
        Node node = cu.getAnnotationDeclarationByName("ClassPreamble").get().getMember(4);
        List<TokenTextElement> indentation = LexicalPreservingPrinter.findIndentation(node);
        assertEquals(Arrays.asList(" ", " ", " "),
                indentation.stream().map(TokenTextElement::expand).collect(Collectors.toList()));
    }

    @Test
    void findIndentationForAnnotationMemberDeclarationWithComment() throws IOException {
        considerExample("AnnotationDeclaration_Example3_original");
        Node node = cu.getAnnotationDeclarationByName("ClassPreamble").get().getMember(5);
        List<TokenTextElement> indentation = LexicalPreservingPrinter.findIndentation(node);
        assertEquals(Arrays.asList(" ", " ", " "),
                indentation.stream().map(TokenTextElement::expand).collect(Collectors.toList()));
    }

    //
    // Tests on printing
    //

    @Test
    void printASuperSimpleCUWithoutChanges() {
        String code = "class A {}";
        considerCode(code);

        assertEquals(code, LexicalPreservingPrinter.print(cu));
    }

    @Test
    void printASuperSimpleClassWithAFieldAdded() {
        String code = "class A {}";
        considerCode(code);

        ClassOrInterfaceDeclaration classA = cu.getClassByName("A").get();
        classA.addField("int", "myField");
        assertEquals("class A {" + EOL + "    int myField;" + EOL + "}", LexicalPreservingPrinter.print(classA));
    }

    @Test
    void printASuperSimpleClassWithoutChanges() {
        String code = "class A {}";
        considerCode(code);

        assertEquals(code, LexicalPreservingPrinter.print(cu.getClassByName("A").get()));
    }

    @Test
    void printASimpleCUWithoutChanges() {
        String code = "class /*a comment*/ A {\t\t" + EOL + " int f;" + EOL + EOL + EOL
                + "         void foo(int p  ) { return  'z'  \t; }}";
        considerCode(code);

        assertEquals(code, LexicalPreservingPrinter.print(cu));
        assertEquals(code, LexicalPreservingPrinter.print(cu.getClassByName("A").get()));
        assertEquals("void foo(int p  ) { return  'z'  \t; }",
                LexicalPreservingPrinter.print(cu.getClassByName("A").get().getMethodsByName("foo").get(0)));
    }

    @Test
    void printASimpleClassRemovingAField() {
        String code = "class /*a comment*/ A {\t\t" + EOL +
                " int f;" + EOL + EOL + EOL +
                "         void foo(int p  ) { return  'z'  \t; }}";
        considerCode(code);

        ClassOrInterfaceDeclaration c = cu.getClassByName("A").get();
        c.getMembers().remove(0);
        assertEquals("class /*a comment*/ A {\t\t" + EOL +
                EOL +
                "         void foo(int p  ) { return  'z'  \t; }}", LexicalPreservingPrinter.print(c));
    }

    @Test
    void printASimpleClassRemovingAMethod() {
        String code = "class /*a comment*/ A {\t\t" + EOL +
                " int f;" + EOL + EOL + EOL +
                "         void foo(int p  ) { return  'z'  \t; }" + EOL +
                " int g;}";
        considerCode(code);

        ClassOrInterfaceDeclaration c = cu.getClassByName("A").get();
        c.getMembers().remove(1);
        assertEquals("class /*a comment*/ A {\t\t" + EOL +
                " int f;" + EOL + EOL + EOL +
                " int g;}", LexicalPreservingPrinter.print(c));
    }

    @Test
    void printASimpleMethodAddingAParameterToAMethodWithZeroParameters() {
        String code = "class A { void foo() {} }";
        considerCode(code);

        MethodDeclaration m = cu.getClassByName("A").get().getMethodsByName("foo").get(0);
        m.addParameter("float", "p1");
        assertEquals("void foo(float p1) {}", LexicalPreservingPrinter.print(m));
    }

    @Test
    void printASimpleMethodAddingAParameterToAMethodWithOneParameter() {
        String code = "class A { void foo(char p1) {} }";
        considerCode(code);

        MethodDeclaration m = cu.getClassByName("A").get().getMethodsByName("foo").get(0);
        m.addParameter("float", "p2");
        assertEquals("void foo(char p1, float p2) {}", LexicalPreservingPrinter.print(m));
    }

    @Test
    void printASimpleMethodRemovingAParameterToAMethodWithOneParameter() {
        String code = "class A { void foo(float p1) {} }";
        considerCode(code);

        MethodDeclaration m = cu.getClassByName("A").get().getMethodsByName("foo").get(0);
        m.getParameters().remove(0);
        assertEquals("void foo() {}", LexicalPreservingPrinter.print(m));
    }

    @Test
    void printASimpleMethodRemovingParameterOneFromMethodWithTwoParameters() {
        String code = "class A { void foo(char p1, int p2) {} }";
        considerCode(code);

        MethodDeclaration m = cu.getClassByName("A").get().getMethodsByName("foo").get(0);
        m.getParameters().remove(0);
        assertEquals("void foo(int p2) {}", LexicalPreservingPrinter.print(m));
    }

    @Test
    void printASimpleMethodRemovingParameterTwoFromMethodWithTwoParameters() {
        String code = "class A { void foo(char p1, int p2) {} }";
        considerCode(code);

        MethodDeclaration m = cu.getClassByName("A").get().getMethodsByName("foo").get(0);
        m.getParameters().remove(1);
        assertEquals("void foo(char p1) {}", LexicalPreservingPrinter.print(m));
    }

    @Test
    void printASimpleMethodAddingAStatement() {
        String code = "class A { void foo(char p1, int p2) {} }";
        considerCode(code);

        Statement s = new ExpressionStmt(new BinaryExpr(
                new IntegerLiteralExpr("10"), new IntegerLiteralExpr("2"), BinaryExpr.Operator.PLUS));
        NodeList<Statement> stmts = cu.getClassByName("A").get().getMethodsByName("foo").get(0).getBody().get()
                .getStatements();
        stmts.add(s);
        MethodDeclaration m = cu.getClassByName("A").get().getMethodsByName("foo").get(0);
        assertEquals("void foo(char p1, int p2) {" + EOL +
                "    10 + 2;" + EOL +
                "}", LexicalPreservingPrinter.print(m));
    }

    @Test
    void printASimpleMethodRemovingAStatementCRLF() {
        printASimpleMethodRemovingAStatement("\r\n");
    }

    @Test
    void printASimpleMethodRemovingAStatementLF() {
        printASimpleMethodRemovingAStatement("\n");
    }

    @Test
    void printASimpleMethodRemovingAStatementCR() {
        printASimpleMethodRemovingAStatement("\r");
    }

    private void printASimpleMethodRemovingAStatement(String eol) {
        String code = "class A {" + eol
                + "\t" + "foo(int a, int b) {" + eol
                + "\t\t" + "int result = a * b;" + eol
                + "\t\t" + "return a * b;" + eol
                + "\t" + "}" + eol
                + "}";

        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);
        ExpressionStmt stmt = cu.findAll(ExpressionStmt.class).get(0);
        stmt.remove();

        assertEquals("class A {" + eol
                + "\t" + "foo(int a, int b) {" + eol
                + "\t\t" + "return a * b;" + eol
                + "\t" + "}" + eol
                + "}", LexicalPreservingPrinter.print(cu));
    }

    @Test
    void printASimpleImport() {
        String code = "import a.b.c.D;";
        considerCode(code);

        ImportDeclaration imp = (ImportDeclaration) cu.getChildNodes().get(0);
        assertEquals("import a.b.c.D;", LexicalPreservingPrinter.print(imp));
    }

    @Test
    void printAnotherImport() {
        String code = "import com.github.javaparser.ast.CompilationUnit;";
        considerCode(code);

        ImportDeclaration imp = (ImportDeclaration) cu.getChildNodes().get(0);
        assertEquals("import com.github.javaparser.ast.CompilationUnit;", LexicalPreservingPrinter.print(imp));
    }

    @Test
    void printAStaticImport() {
        String code = "import static com.github.javaparser.ParseStart.*;";
        considerCode(code);

        ImportDeclaration imp = (ImportDeclaration) cu.getChildNodes().get(0);
        assertEquals("import static com.github.javaparser.ParseStart.*;", LexicalPreservingPrinter.print(imp));
    }

    @Test
    void checkAnnidatedTypeParametersPrinting() {
        String code = "class A { private final Stack<Iterator<Triple>> its = new Stack<Iterator<Triple>>(); }";
        considerCode(code);
        assertEquals("class A { private final Stack<Iterator<Triple>> its = new Stack<Iterator<Triple>>(); }",
                LexicalPreservingPrinter.print(cu));
    }

    @Test
    void printASingleCatch() {
        String code = "class A {{try { doit(); } catch (Exception e) {}}}";
        considerCode(code);

        assertEquals("class A {{try { doit(); } catch (Exception e) {}}}", LexicalPreservingPrinter.print(cu));
    }

    @Test
    void printAMultiCatch() {
        String code = "class A {{try { doit(); } catch (Exception | AssertionError e) {}}}";
        considerCode(code);

        assertEquals("class A {{try { doit(); } catch (Exception | AssertionError e) {}}}",
                LexicalPreservingPrinter.print(cu));
    }

    @Test
    void printASingleCatchType() {
        String code = "class A {{try { doit(); } catch (Exception e) {}}}";
        considerCode(code);
        InitializerDeclaration initializerDeclaration = (InitializerDeclaration) cu.getType(0).getMembers().get(0);
        TryStmt tryStmt = (TryStmt) initializerDeclaration.getBody().getStatements().get(0);
        CatchClause catchClause = tryStmt.getCatchClauses().get(0);
        Type catchType = catchClause.getParameter().getType();

        assertEquals("Exception", LexicalPreservingPrinter.print(catchType));
    }

    @Test
    void printUnionType() {
        String code = "class A {{try { doit(); } catch (Exception | AssertionError e) {}}}";
        considerCode(code);
        InitializerDeclaration initializerDeclaration = (InitializerDeclaration) cu.getType(0).getMembers().get(0);
        TryStmt tryStmt = (TryStmt) initializerDeclaration.getBody().getStatements().get(0);
        CatchClause catchClause = tryStmt.getCatchClauses().get(0);
        UnionType unionType = (UnionType) catchClause.getParameter().getType();

        assertEquals("Exception | AssertionError", LexicalPreservingPrinter.print(unionType));
    }

    @Test
    void printParameterHavingUnionType() {
        String code = "class A {{try { doit(); } catch (Exception | AssertionError e) {}}}";
        considerCode(code);
        InitializerDeclaration initializerDeclaration = (InitializerDeclaration) cu.getType(0).getMembers().get(0);
        TryStmt tryStmt = (TryStmt) initializerDeclaration.getBody().getStatements().get(0);
        CatchClause catchClause = tryStmt.getCatchClauses().get(0);
        Parameter parameter = catchClause.getParameter();

        assertEquals("Exception | AssertionError e", LexicalPreservingPrinter.print(parameter));
    }

    @Test
    void printLambaWithUntypedParams() {
        String code = "class A {Function<String,String> f = a -> a;}";
        considerCode(code);

        assertEquals("class A {Function<String,String> f = a -> a;}", LexicalPreservingPrinter.print(cu));
    }

    @Test
    void printAModuleInfoSpecificKeywordUsedAsIdentifier1() {
        considerCode("class module { }");

        cu.getClassByName("module").get().setName("xyz");

        assertEquals("class xyz { }", LexicalPreservingPrinter.print(cu));
    }

    @Test
    void printAModuleInfoSpecificKeywordUsedAsIdentifier2() {
        considerCode("class xyz { }");

        cu.getClassByName("xyz").get().setName("module");

        assertEquals("class module { }", LexicalPreservingPrinter.print(cu));
    }

    // Issue 823: setPackageDeclaration on CU starting with a comment
    @Test
    void reactToSetPackageDeclarationOnCuStartingWithComment() {
        considerCode("// Hey, this is a comment\n" +
                "\n" +
                "\n" +
                "// Another one\n" +
                "\n" +
                "class A {}");
        cu.setPackageDeclaration("org.javaparser.lexicalpreservation.examples");
    }

    @Test
    void printLambdaIntersectionTypeAssignment() {
        String code = "class A {" + EOL +
                "  void f() {" + EOL +
                "    Runnable r = (Runnable & Serializable) (() -> {});" + EOL +
                "    r = (Runnable & Serializable)() -> {};" + EOL +
                "    r = (Runnable & I)() -> {};" + EOL +
                "  }}";
        considerCode(code);

        assertEquals(code, LexicalPreservingPrinter.print(cu));
    }

    @Test
    void printLambdaIntersectionTypeReturn() {
        String code = "class A {" + EOL
                + "  Object f() {" + EOL
                + "    return (Comparator<Map.Entry<K, V>> & Serializable)(c1, c2) -> c1.getKey().compareTo(c2.getKey()); "
                + EOL
                + "}}";
        considerCode(code);

        assertEquals(code, LexicalPreservingPrinter.print(cu));
    }

    // See issue #855
    @Test
    void handleOverrideAnnotation() {
        String code = "public class TestPage extends Page {" + EOL +
                EOL +
                "   protected void test() {}" + EOL +
                EOL +
                "   @Override" + EOL +
                "   protected void initializePage() {}" + EOL +
                "}";

        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);

        cu.getTypes()
                .forEach(type -> type.getMembers()
                        .forEach(member -> {
                            if (member instanceof MethodDeclaration) {
                                MethodDeclaration methodDeclaration = (MethodDeclaration) member;
                                if (!methodDeclaration.getAnnotationByName("Override").isPresent()) {
                                    methodDeclaration.addAnnotation("Override");
                                }
                            }
                        }));
        assertEquals("public class TestPage extends Page {" + EOL +
                EOL +
                "   @Override()" + EOL +
                "   protected void test() {}" + EOL +
                EOL +
                "   @Override" + EOL +
                "   protected void initializePage() {}" + EOL +
                "}", LexicalPreservingPrinter.print(cu));
    }

    @Test
    void preserveSpaceAsIsForASimpleClassWithMoreFormatting() throws IOException {
        considerExample("ASimpleClassWithMoreFormatting");
        assertEquals(readExample("ASimpleClassWithMoreFormatting"), LexicalPreservingPrinter.print(cu));
    }

    @Test
    void renameASimpleClassWithMoreFormatting() throws IOException {
        considerExample("ASimpleClassWithMoreFormatting");

        cu.getClassByName("ASimpleClass").get()
                .setName("MyRenamedClass");
        assertEquals(readExample("ASimpleClassWithMoreFormatting_step1"), LexicalPreservingPrinter.print(cu));
    }

    @Test
    void theLexicalPreservationStringForAnAddedMethodShouldBeIndented() throws IOException {
        considerExample("ASimpleClassWithMoreFormatting");

        cu.getClassByName("ASimpleClass").get()
                .setName("MyRenamedClass");
        MethodDeclaration setter = cu
                .getClassByName("MyRenamedClass").get()
                .addMethod("setAField", PUBLIC);
        assertEquals("public void setAField() {" + EOL +
                "    }", LexicalPreservingPrinter.print(setter));
    }

    @Test
    void addMethodToASimpleClassWithMoreFormatting() throws IOException {
        considerExample("ASimpleClassWithMoreFormatting");

        cu.getClassByName("ASimpleClass").get()
                .setName("MyRenamedClass");
        MethodDeclaration setter = cu
                .getClassByName("MyRenamedClass").get()
                .addMethod("setAField", PUBLIC);
        assertEquals(readExample("ASimpleClassWithMoreFormatting_step2"), LexicalPreservingPrinter.print(cu));
    }

    @Test
    void addingParameterToAnAddedMethodInASimpleClassWithMoreFormatting() throws IOException {
        considerExample("ASimpleClassWithMoreFormatting");

        cu.getClassByName("ASimpleClass").get()
                .setName("MyRenamedClass");
        MethodDeclaration setter = cu
                .getClassByName("MyRenamedClass").get()
                .addMethod("setAField", PUBLIC);
        setter.addParameter("boolean", "aField");
        assertEquals(readExample("ASimpleClassWithMoreFormatting_step3"), LexicalPreservingPrinter.print(cu));
    }

    @Test
    void findIndentationOfEmptyMethod() throws IOException {
        considerExample("ASimpleClassWithMoreFormatting_step3");

        MethodDeclaration setter = cu.getClassByName("MyRenamedClass").get()
                .getMethodsByName("setAField").get(0);
        assertEquals(4, LexicalPreservingPrinter.findIndentation(setter).size());
        assertEquals(4, LexicalPreservingPrinter.findIndentation(setter.getBody().get()).size());
    }

    @Test
    void findIndentationOfMethodWithStatements() throws IOException {
        considerExample("ASimpleClassWithMoreFormatting_step4");

        MethodDeclaration setter = cu.getClassByName("MyRenamedClass").get()
                .getMethodsByName("setAField").get(0);
        assertEquals(4, LexicalPreservingPrinter.findIndentation(setter).size());
        assertEquals(4, LexicalPreservingPrinter.findIndentation(setter.getBody().get()).size());
        assertEquals(8, LexicalPreservingPrinter.findIndentation(setter.getBody().get().getStatement(0)).size());
    }

    @Test
    void addingStatementToAnAddedMethodInASimpleClassWithMoreFormatting() throws IOException {
        considerExample("ASimpleClassWithMoreFormatting");

        cu.getClassByName("ASimpleClass").get()
                .setName("MyRenamedClass");
        MethodDeclaration setter = cu
                .getClassByName("MyRenamedClass").get()
                .addMethod("setAField", PUBLIC);
        setter.addParameter("boolean", "aField");
        setter.getBody().get().getStatements().add(new ExpressionStmt(
                new AssignExpr(
                        new FieldAccessExpr(new ThisExpr(), "aField"),
                        new NameExpr("aField"),
                        AssignExpr.Operator.ASSIGN)));
        assertEquals(readExample("ASimpleClassWithMoreFormatting_step4"), LexicalPreservingPrinter.print(cu));
    }

    @Test
    void addingStatementToAnAddedMethodInASimpleClassWithMoreFormattingFromStep3() throws IOException {
        considerExample("ASimpleClassWithMoreFormatting_step3");

        MethodDeclaration setter = cu.getClassByName("MyRenamedClass").get()
                .getMethodsByName("setAField").get(0);
        setter.getBody().get().getStatements().add(new ExpressionStmt(
                new AssignExpr(
                        new FieldAccessExpr(new ThisExpr(), "aField"),
                        new NameExpr("aField"),
                        AssignExpr.Operator.ASSIGN)));
        assertEquals(readExample("ASimpleClassWithMoreFormatting_step4"), LexicalPreservingPrinter.print(cu));
    }

    @Test
    void nodeTextForMethod() throws IOException {
        considerExample("ASimpleClassWithMoreFormatting_step4");

        MethodDeclaration setter = cu.getClassByName("MyRenamedClass").get()
                .getMethodsByName("setAField").get(0);
        NodeText nodeText;

        nodeText = getTextForNode(setter);
        int index = 0;
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(Modifier.class));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(VoidType.class));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(SimpleName.class));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.LPAREN));
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(Parameter.class));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.RPAREN));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(BlockStmt.class));
        assertEquals(index, nodeText.getElements().size());

        nodeText = getTextForNode(setter.getBody().get());
        index = 0;
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.LBRACE));
        assertTrue(nodeText.getElements().get(index++).isNewline());
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(ExpressionStmt.class));
        assertTrue(nodeText.getElements().get(index++).isNewline());
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.RBRACE));
        assertEquals(index, nodeText.getElements().size());

        nodeText = getTextForNode(setter.getBody().get().getStatement(0));
        index = 0;
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(AssignExpr.class));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SEMICOLON));
        assertEquals(index, nodeText.getElements().size());
    }

    @Test
    void nodeTextForModifiedMethod() throws IOException {
        considerExample("ASimpleClassWithMoreFormatting_step3");

        MethodDeclaration setter = cu.getClassByName("MyRenamedClass").get()
                .getMethodsByName("setAField").get(0);
        setter.getBody().get().getStatements().add(new ExpressionStmt(
                new AssignExpr(
                        new FieldAccessExpr(new ThisExpr(), "aField"),
                        new NameExpr("aField"),
                        AssignExpr.Operator.ASSIGN)));
        NodeText nodeText;

        nodeText = getTextForNode(setter);
        int index = 0;
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(Modifier.class));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(VoidType.class));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(SimpleName.class));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.LPAREN));
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(Parameter.class));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.RPAREN));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(BlockStmt.class));
        assertEquals(index, nodeText.getElements().size());

        nodeText = getTextForNode(setter.getBody().get());
        index = 0;
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.LBRACE));
        assertTrue(nodeText.getElements().get(index++).isNewline());
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(ExpressionStmt.class));
        assertTrue(nodeText.getElements().get(index++).isNewline());
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SPACE));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.RBRACE));
        assertEquals(index, nodeText.getElements().size());

        nodeText = LexicalPreservingPrinter.getOrCreateNodeText(setter.getBody().get().getStatement(0));
        index = 0;
        assertTrue(nodeText.getElements().get(index++).isChildOfClass(AssignExpr.class));
        assertTrue(nodeText.getElements().get(index++).isToken(GeneratedJavaParserConstants.SEMICOLON));
        assertEquals(index, nodeText.getElements().size());
    }

    // See issue #926
    @Test
    void addASecondStatementToExistingMethod() throws IOException {
        considerExample("MethodWithOneStatement");

        MethodDeclaration methodDeclaration = cu.getType(0).getMethodsByName("someMethod").get(0);
        methodDeclaration.getBody().get().getStatements().add(new ExpressionStmt(
                new VariableDeclarationExpr(
                        new VariableDeclarator(
                                parseClassOrInterfaceType("String"),
                                "test2",
                                new StringLiteralExpr("")))));
        assertEquals("public void someMethod() {" + EOL
                + "        String test = \"\";" + EOL
                + "        String test2 = \"\";" + EOL
                // HACK: The right closing brace should not have indentation
                // because the original method did not introduce indentation,
                // however due to necessity this test was left with indentation,
                // in a later version it should be revised.
                + "    }", LexicalPreservingPrinter.print(methodDeclaration));
    }

    // See issue #866
    @Test
    void moveOverrideAnnotations() {
        String code = "public class TestPage extends Page {" + EOL +
                EOL +
                "   protected void test() {}" + EOL +
                EOL +
                "   protected @Override void initializePage() {}" + EOL +
                "}";

        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);

        cu.getTypes()
                .forEach(type -> type.getMembers()
                        .forEach(member -> member.ifMethodDeclaration(methodDeclaration -> {
                            if (methodDeclaration.getAnnotationByName("Override").isPresent()) {

                                while (methodDeclaration.getAnnotations().isNonEmpty()) {
                                    AnnotationExpr annotationExpr = methodDeclaration.getAnnotations().get(0);
                                    annotationExpr.remove();
                                }

                                methodDeclaration.addMarkerAnnotation("Override");
                            }
                        })));
        assertEquals("public class TestPage extends Page {" + EOL +
                EOL +
                "   protected void test() {}" + EOL +
                EOL +
                "   @Override" + EOL +
                "   protected void initializePage() {}" + EOL +
                "}", LexicalPreservingPrinter.print(cu));
    }

    // See issue #866
    @Test
    void moveOrAddOverrideAnnotations() {
        String code = "public class TestPage extends Page {" + EOL +
                EOL +
                "   protected void test() {}" + EOL +
                EOL +
                "   protected @Override void initializePage() {}" + EOL +
                "}";

        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);

        cu.getTypes()
                .forEach(type -> type.getMembers()
                        .forEach(member -> {
                            if (member instanceof MethodDeclaration) {
                                MethodDeclaration methodDeclaration = (MethodDeclaration) member;
                                if (methodDeclaration.getAnnotationByName("Override").isPresent()) {

                                    while (methodDeclaration.getAnnotations().isNonEmpty()) {
                                        AnnotationExpr annotationExpr = methodDeclaration.getAnnotations().get(0);
                                        annotationExpr.remove();
                                    }
                                }
                                methodDeclaration.addMarkerAnnotation("Override");
                            }
                        }));
        assertEquals("public class TestPage extends Page {" + EOL +
                EOL +
                "   @Override" + EOL +
                "   protected void test() {}" + EOL +
                EOL +
                "   @Override" + EOL +
                "   protected void initializePage() {}" + EOL +
                "}", LexicalPreservingPrinter.print(cu));
    }

    // See issue #865
    @Test
    void handleAddingMarkerAnnotation() {
        String code = "public class TestPage extends Page {" + EOL +
                EOL +
                "   protected void test() {}" + EOL +
                EOL +
                "   @Override" + EOL +
                "   protected void initializePage() {}" + EOL +
                "}";

        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);

        cu.getTypes()
                .forEach(type -> type.getMembers()
                        .forEach(member -> {
                            if (member instanceof MethodDeclaration) {
                                MethodDeclaration methodDeclaration = (MethodDeclaration) member;
                                if (!methodDeclaration.getAnnotationByName("Override").isPresent()) {
                                    methodDeclaration.addMarkerAnnotation("Override");
                                }
                            }
                        }));
        assertEquals("public class TestPage extends Page {" + EOL +
                EOL +
                "   @Override" + EOL +
                "   protected void test() {}" + EOL +
                EOL +
                "   @Override" + EOL +
                "   protected void initializePage() {}" + EOL +
                "}", LexicalPreservingPrinter.print(cu));
    }

    // See issue #865
    @Test
    void handleOverrideMarkerAnnotation() {
        String code = "public class TestPage extends Page {" + EOL +
                EOL +
                "   protected void test() {}" + EOL +
                EOL +
                "   protected void initializePage() {}" + EOL +
                "}";

        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);

        cu.getTypes()
                .forEach(type -> type.getMembers()
                        .forEach(member -> member.ifMethodDeclaration(
                                methodDeclaration -> methodDeclaration.addMarkerAnnotation("Override"))));
        assertEquals("public class TestPage extends Page {" + EOL +
                EOL +
                "   @Override" + EOL +
                "   protected void test() {}" + EOL +
                EOL +
                "   @Override" + EOL +
                "   protected void initializePage() {}" + EOL +
                "}", LexicalPreservingPrinter.print(cu));
    }

    // See issue #865
    @Test
    void handleOverrideAnnotationAlternative() {
        String code = "public class TestPage extends Page {" + EOL +
                EOL +
                "   protected void test() {}" + EOL +
                EOL +
                "   protected void initializePage() {}" + EOL +
                "}";

        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);

        cu.getTypes()
                .forEach(type -> type.getMembers()
                        .forEach(member -> member.ifMethodDeclaration(
                                methodDeclaration -> methodDeclaration.addAnnotation("Override"))));
        assertEquals("public class TestPage extends Page {" + EOL +
                EOL +
                "   @Override()" + EOL +
                "   protected void test() {}" + EOL +
                EOL +
                "   @Override()" + EOL +
                "   protected void initializePage() {}" + EOL +
                "}", LexicalPreservingPrinter.print(cu));
    }

    @Test
    void invokeModifierVisitor() {
        String code = "class A {" + EOL
                + "  Object f() {" + EOL
                + "    return (Comparator<Map.Entry<K, V>> & Serializable)(c1, c2) -> c1.getKey().compareTo(c2.getKey()); "
                + EOL
                + "}}";
        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);
        cu.accept(new ModifierVisitor<>(), null);
    }

    @Test
    void handleDeprecatedAnnotationFinalClass() {
        String code = "public final class A {}";

        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);

        cu.getTypes().forEach(type -> type.addAndGetAnnotation(Deprecated.class));

        assertEquals("@Deprecated()" + EOL +
                "public final class A {}", LexicalPreservingPrinter.print(cu));

    }

    @Test
    void handleDeprecatedAnnotationAbstractClass() {
        String code = "public abstract class A {}";

        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);

        cu.getTypes().forEach(type -> type.addAndGetAnnotation(Deprecated.class));

        assertEquals("@Deprecated()" + EOL +
                "public abstract class A {}", LexicalPreservingPrinter.print(cu));
    }

    @Test
    void issue1244() {
        String code = "public class Foo {" + EOL + EOL
                + "// Some comment" + EOL + EOL // does work with only one \n
                + "public void writeExternal() {}" + EOL + "}";
        CompilationUnit originalCu = parse(code);
        CompilationUnit cu = LexicalPreservingPrinter.setup(originalCu);

        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(c -> {
            List<MethodDeclaration> methods = c.getMethodsByName("writeExternal");
            for (MethodDeclaration method : methods) {
                c.remove(method);
            }
        });
        assertEqualsNoEol("public class Foo {\n" +
                "// Some comment\n\n" +
                "}", LexicalPreservingPrinter.print(cu));
    }

    static class AddFooCallModifierVisitor extends ModifierVisitor<Void> {

        @Override
        public Visitable visit(MethodCallExpr n, Void arg) {
            // Add a call to foo() on every found method call
            return new MethodCallExpr(n, "foo");
        }
    }

    // See issue 1277
    @Test
    void testInvokeModifierVisitor() {
        String code = "class A {" + EOL +
                "  public String message = \"hello\";" + EOL +
                "   void bar() {" + EOL +
                "     System.out.println(\"hello\");" + EOL +
                "   }" + EOL +
                "}";

        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);
        cu.accept(new AddFooCallModifierVisitor(), null);
    }

    static class CallModifierVisitor extends ModifierVisitor<Void> {

        @Override
        public Visitable visit(MethodCallExpr n, Void arg) {
            // Add a call to foo() on every found method call
            return new MethodCallExpr(n.clone(), "foo");
        }
    }

    @Test
    void invokeModifierVisitorIssue1297() {
        String code = "class A {" + EOL +
                "   public void bar() {" + EOL +
                "     System.out.println(\"hello\");" + EOL +
                "     System.out.println(\"hello\");" + EOL +
                "     // comment" + EOL +
                "   }" + EOL +
                "}";

        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);
        cu.accept(new CallModifierVisitor(), null);
    }

    @Test
    void addedBlockCommentsPrinted() {
        String code = "public class Foo { }";
        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);

        cu.getClassByName("Foo").get()
                .addMethod("mymethod")
                .setBlockComment("block");
        assertEqualsNoEol("public class Foo {" + EOL +
                "    /*block*/" + EOL +
                "    void mymethod() {" + EOL +
                "    }" + EOL +
                "}", LexicalPreservingPrinter.print(cu));
    }

    @Test
    void addedLineCommentsPrinted() {
        String code = "public class Foo { }";
        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);

        cu.getClassByName("Foo").get()
                .addMethod("mymethod")
                .setLineComment("line");
        assertEqualsNoEol("public class Foo {" + EOL +
                "    //line" + EOL +
                "    void mymethod() {" + EOL +
                "    }" + EOL +
                "}", LexicalPreservingPrinter.print(cu));
    }

    @Test
    void removedLineCommentsPrinted() {
        String code = "public class Foo {" + EOL +
                "//line" + EOL +
                "void mymethod() {" + EOL +
                "}" + EOL +
                "}";
        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);
        cu.getAllContainedComments().get(0).remove();

        assertEqualsNoEol("public class Foo {" + EOL +
                "void mymethod() {" + EOL +
                "}" + EOL +
                "}", LexicalPreservingPrinter.print(cu));
    }

    // Checks if comments get removed properly with Unix style line endings
    @Test
    void removedLineCommentsPrintedUnix() {
        String code = "public class Foo {" + "\n" +
                "//line" + "\n" +
                "void mymethod() {" + "\n" +
                "}" + "\n" +
                "}";
        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);
        cu.getAllContainedComments().get(0).remove();

        assertEquals("public class Foo {" + "\n" +
                "void mymethod() {" + "\n" +
                "}" + "\n" +
                "}", LexicalPreservingPrinter.print(cu));
    }

    @Test
    void removedBlockCommentsPrinted() {
        String code = "public class Foo {" + EOL +
                "/*" + EOL +
                "Block comment coming through" + EOL +
                "*/" + EOL +
                "void mymethod() {" + EOL +
                "}" + EOL +
                "}";
        CompilationUnit cu = parse(code);
        LexicalPreservingPrinter.setup(cu);
        cu.getAllContainedComments().get(0).remove();

        assertEqualsNoEol("public class Foo {" + EOL +
                "void mymethod() {" + EOL +
                "}" + EOL +
                "}", LexicalPreservingPrinter.print(cu));
    }

    @Test
    void testFixIndentOfMovedNode() {
        try {
            CompilationUnit compilationUnit = parse(readExample("FixIndentOfMovedNode"));
            LexicalPreservingPrinter.setup(compilationUnit);

            compilationUnit.getClassByName("ThisIsASampleClass").get()
                    .getMethodsByName("longerMethod")
                    .get(0)
                    .setBlockComment("Lorem ipsum dolor sit amet, consetetur sadipscing elitr.");

            compilationUnit.getClassByName("Foo").get()
                    .getFieldByName("myFoo")
                    .get()
                    .setLineComment("sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat");

            String expectedCode = readExample("FixIndentOfMovedNodeExpected");
            assertEquals(expectedCode, LexicalPreservingPrinter.print(compilationUnit));
        } catch (IOException ex) {
            fail("Could not read test code", ex);
        }
    }

    @Test
    void issue1321() {
        CompilationUnit compilationUnit = parse("class X { X() {} private void testme() {} }");
        LexicalPreservingPrinter.setup(compilationUnit);

        ClassOrInterfaceDeclaration type = compilationUnit.getClassByName("X").get();
        type.getConstructors().get(0).setBody(new BlockStmt().addStatement("testme();"));

        assertEqualsNoEol("class X { X() {\n    testme();\n} private void testme() {} }",
                LexicalPreservingPrinter.print(compilationUnit));
    }

    @Test
    void issue2001() {
        CompilationUnit compilationUnit = parse("class X {void blubb(){X.p(\"blaubb04\");}}");
        LexicalPreservingPrinter.setup(compilationUnit);

        compilationUnit
                .findAll(MethodCallExpr.class)
                .forEach(Node::removeForced);

        assertEqualsNoEol("class X {void blubb(){}}", LexicalPreservingPrinter.print(compilationUnit));
    }

    @Test
    void testIndentOfCodeBlocks() throws IOException {
        CompilationUnit compilationUnit = parse(considerExample("IndentOfInsertedCodeBlocks"));
        LexicalPreservingPrinter.setup(compilationUnit);

        IfStmt ifStmt = new IfStmt();
        ifStmt.setCondition(StaticJavaParser.parseExpression("name.equals(\"foo\")"));
        BlockStmt blockStmt = new BlockStmt();
        blockStmt.addStatement(StaticJavaParser.parseStatement("int i = 0;"));
        blockStmt.addStatement(StaticJavaParser.parseStatement("System.out.println(i);"));
        blockStmt.addStatement(
                new IfStmt().setCondition(StaticJavaParser.parseExpression("i < 0"))
                        .setThenStmt(new BlockStmt().addStatement(StaticJavaParser.parseStatement("i = 0;"))));
        blockStmt.addStatement(StaticJavaParser.parseStatement("new Object(){};"));
        ifStmt.setThenStmt(blockStmt);
        ifStmt.setElseStmt(new BlockStmt());

        compilationUnit.findFirst(BlockStmt.class).get().addStatement(ifStmt);
        String expected = considerExample("IndentOfInsertedCodeBlocksExpected");
        assertEquals(expected, LexicalPreservingPrinter.print(compilationUnit));
    }

    @Test
    void commentAddedAtTopLevel() {
        JavaParser javaParser = new JavaParser(new ParserConfiguration().setLexicalPreservationEnabled(true));
        CompilationUnit cu = javaParser.parse("package x;class X{}").getResult().get();

        cu.setComment(new LineComment("Bla"));
        assertEqualsNoEol("//Bla\npackage x;class X{}", LexicalPreservingPrinter.print(cu));

        cu.setComment(new LineComment("BlaBla"));
        assertEqualsNoEol("//BlaBla\npackage x;class X{}", LexicalPreservingPrinter.print(cu));

        cu.removeComment();
        assertEqualsNoEol("package x;class X{}", LexicalPreservingPrinter.print(cu));
    }

    @Test
    public void testReplaceStringLiteral() {
        final JavaParser javaParser = new JavaParser(new ParserConfiguration().setLexicalPreservationEnabled(true));

        final String code = "\"asd\"";
        final String expected = "\"REPLACEMENT\"";

        final Expression b = javaParser.parseExpression(code).getResult().orElseThrow(AssertionError::new);

        assertTrue(b.isStringLiteralExpr());
        StringLiteralExpr sle = (StringLiteralExpr) b;
        sle.setValue("REPLACEMENT");

        final String actual = LexicalPreservingPrinter.print(b);
        assertEquals(expected, actual);
    }

    @Test
    public void testReplaceStringLiteralWithinStatement() {
        final JavaParser javaParser = new JavaParser(new ParserConfiguration().setLexicalPreservationEnabled(true));

        String code = "String str = \"aaa\";";
        String expected = "String str = \"REPLACEMENT\";";

        Statement b = javaParser.parseStatement(code).getResult().orElseThrow(AssertionError::new);
        b.findAll(StringLiteralExpr.class).forEach(stringLiteralExpr -> {
            stringLiteralExpr.setValue("REPLACEMENT");
        });

        assertEquals(expected, LexicalPreservingPrinter.print(b));
        assertEquals(expected, b.toString());
    }

    @Test
    public void testReplaceClassName() {
        final JavaParser javaParser = new JavaParser(new ParserConfiguration().setLexicalPreservationEnabled(true));

        final String code = "class A {}";
        final CompilationUnit b = javaParser.parse(code).getResult().orElseThrow(AssertionError::new);
        LexicalPreservingPrinter.setup(b);

        assertEquals(1, b.findAll(ClassOrInterfaceDeclaration.class).size());
        b.findAll(ClassOrInterfaceDeclaration.class).forEach(coid -> coid.setName("B"));

        final String expected = "class B {}";

        final String actual = LexicalPreservingPrinter.print(b);
        assertEquals(expected, actual);
    }

    @Test
    public void testReplaceIntLiteral() {
        final JavaParser javaParser = new JavaParser(new ParserConfiguration().setLexicalPreservationEnabled(true));

        final String code = "5";
        final String expected = "10";

        final Expression b = javaParser.parseExpression(code).getResult().orElseThrow(AssertionError::new);

        assertTrue(b.isIntegerLiteralExpr());
        ((IntegerLiteralExpr) b).setValue("10");

        final String actual = LexicalPreservingPrinter.print(b);
        assertEquals(expected, actual);
    }

    @Test
    public void testReplaceLongLiteral() {
        final JavaParser javaParser = new JavaParser(new ParserConfiguration().setLexicalPreservationEnabled(true));

        String code = "long x = 5L;";
        String expected = "long x = 10L;";

        final Statement b = javaParser.parseStatement(code).getResult().orElseThrow(AssertionError::new);
        b.findAll(LongLiteralExpr.class).forEach(longLiteralExpr -> {
            longLiteralExpr.setValue("10L");
        });

        final String actual = LexicalPreservingPrinter.print(b);
        assertEquals(expected, actual);
    }

    @Test
    public void testReplaceBooleanLiteral() {
        final JavaParser javaParser = new JavaParser(new ParserConfiguration().setLexicalPreservationEnabled(true));

        String code = "boolean x = true;";
        String expected = "boolean x = false;";

        final Statement b = javaParser.parseStatement(code).getResult().orElseThrow(AssertionError::new);
        b.findAll(BooleanLiteralExpr.class).forEach(booleanLiteralExpr -> {
            booleanLiteralExpr.setValue(false);
        });

        final String actual = LexicalPreservingPrinter.print(b);
        assertEquals(expected, actual);
    }

    @Test
    public void testReplaceDoubleLiteral() {
        final JavaParser javaParser = new JavaParser(new ParserConfiguration().setLexicalPreservationEnabled(true));

        String code = "double x = 5.0D;";
        String expected = "double x = 10.0D;";

        final Statement b = javaParser.parseStatement(code).getResult().orElseThrow(AssertionError::new);
        b.findAll(DoubleLiteralExpr.class).forEach(doubleLiteralExpr -> {
            doubleLiteralExpr.setValue("10.0D");
        });

        final String actual = LexicalPreservingPrinter.print(b);
        assertEquals(expected, actual);
    }

    @Test
    public void testReplaceCharLiteral() {
        final JavaParser javaParser = new JavaParser(new ParserConfiguration().setLexicalPreservationEnabled(true));

        String code = "char x = 'a';";
        String expected = "char x = 'b';";

        final Statement b = javaParser.parseStatement(code).getResult().orElseThrow(AssertionError::new);
        b.findAll(CharLiteralExpr.class).forEach(charLiteralExpr -> {
            charLiteralExpr.setValue("b");
        });

        final String actual = LexicalPreservingPrinter.print(b);
        assertEquals(expected, actual);
    }

    @Test
    public void testReplaceCharLiteralUnicode() {
        final JavaParser javaParser = new JavaParser(new ParserConfiguration().setLexicalPreservationEnabled(true));

        String code = "char x = 'a';";
        String expected = "char x = '\\u0000';";

        final Statement b = javaParser.parseStatement(code).getResult().orElseThrow(AssertionError::new);
        b.findAll(CharLiteralExpr.class).forEach(charLiteralExpr -> {
            charLiteralExpr.setValue("\\u0000");
        });

        final String actual = LexicalPreservingPrinter.print(b);
        assertEquals(expected, actual);
    }

    @Test
    public void testReplaceTextBlockLiteral() {
        final JavaParser javaParser = new JavaParser(
                new ParserConfiguration()
                        .setLexicalPreservationEnabled(true)
                        .setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_14)
        );

        String code = "String x = \"\"\"a\"\"\";";
        String expected = "String x = \"\"\"\n" +
                "     REPLACEMENT\n" +
                "     \"\"\";";

        final Statement b = javaParser.parseStatement(code).getResult().orElseThrow(AssertionError::new);
        b.findAll(TextBlockLiteralExpr.class).forEach(textblockLiteralExpr -> {
            textblockLiteralExpr.setValue("\n     REPLACEMENT\n     ");
        });

        final String actual = LexicalPreservingPrinter.print(b);
        assertEquals(expected, actual);
    }

    @Test
    public void test() {
        final JavaParser javaParser = new JavaParser(
                new ParserConfiguration()
                        .setLexicalPreservationEnabled(true)
        );

//        String eol = EOL;
        String eol = "\n"; // Fails (sometimes???) due to equalsNoEol not normalising both inputs

        String code = "" +
                "/*" + EOL + 
                " * Copyright (C) 2007-2010 Júlio Vilmar Gesser." + EOL + 
                " * Copyright (C) 2011, 2013-2020 The JavaParser Team." + EOL + 
                " *" + EOL + 
                " * This file is part of JavaParser." + EOL + 
                " *" + EOL + 
                " * JavaParser can be used either under the terms of" + EOL + 
                " * a) the GNU Lesser General Public License as published by" + EOL + 
                " *     the Free Software Foundation, either version 3 of the License, or" + EOL + 
                " *     (at your option) any later version." + EOL + 
                " * b) the terms of the Apache License" + EOL + 
                " *" + EOL + 
                " * You should have received a copy of both licenses in LICENCE.LGPL and" + EOL + 
                " * LICENCE.APACHE. Please refer to those files for details." + EOL + 
                " *" + EOL + 
                " * JavaParser is distributed in the hope that it will be useful," + EOL + 
                " * but WITHOUT ANY WARRANTY; without even the implied warranty of" + EOL + 
                " * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the" + EOL + 
                " * GNU Lesser General Public License for more details." + EOL + 
                " */" + EOL + 
                "package com.github.javaparser.ast.stmt;" + EOL + 
                "" + EOL + 
                "import com.github.javaparser.ast.AllFieldsConstructor;" + EOL + 
                "import com.github.javaparser.ast.expr.BooleanLiteralExpr;" + EOL + 
                "import com.github.javaparser.ast.expr.Expression;" + EOL + 
                "import com.github.javaparser.ast.observer.ObservableProperty;" + EOL + 
                "import com.github.javaparser.ast.visitor.GenericVisitor;" + EOL + 
                "import com.github.javaparser.ast.visitor.VoidVisitor;" + EOL + 
                "import java.util.Optional;" + EOL + 
                "import static com.github.javaparser.utils.Utils.assertNotNull;" + EOL + 
                "import com.github.javaparser.ast.Node;" + EOL + 
                "import com.github.javaparser.ast.visitor.CloneVisitor;" + EOL + 
                "import com.github.javaparser.metamodel.AssertStmtMetaModel;" + EOL + 
                "import com.github.javaparser.metamodel.JavaParserMetaModel;" + EOL + 
                "import com.github.javaparser.TokenRange;" + EOL + 
                "import com.github.javaparser.metamodel.OptionalProperty;" + EOL + 
                "import java.util.function.Consumer;" + EOL + 
                "import com.github.javaparser.ast.Generated;" + EOL + 
                "" + EOL + 
                "/**" + EOL + 
                " * A usage of the keyword \"assert\"" + EOL + 
                " * <br>In {@code assert dead : \"Wasn't expecting to be dead here\";} the check is \"dead\" and the message is the string." + EOL + 
                " * @author Julio Vilmar Gesser" + EOL + 
                " */" + EOL + 
                "public class AssertStmt extends Statement {" + EOL + 
                "" + EOL + 
                "    private Expression check;" + EOL + 
                "" + EOL + 
                "    @OptionalProperty" + EOL + 
                "    private Expression message;" + EOL + 
                "" + EOL + 
                "    public AssertStmt() {" + EOL + 
                "        this(null, new BooleanLiteralExpr(), null);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    public AssertStmt(final Expression check) {" + EOL + 
                "        this(null, check, null);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @AllFieldsConstructor" + EOL + 
                "    public AssertStmt(final Expression check, final Expression message) {" + EOL + 
                "        this(null, check, message);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    /**" + EOL + 
                "     * This constructor is used by the parser and is considered private." + EOL + 
                "     */" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.MainConstructorGenerator\")" + EOL + 
                "    public AssertStmt(TokenRange tokenRange, Expression check, Expression message) {" + EOL + 
                "        super(tokenRange);" + EOL + 
                "        setCheck(check);" + EOL + 
                "        setMessage(message);" + EOL + 
                "        customInitialization();" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.AcceptGenerator\")" + EOL + 
                "    public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {" + EOL + 
                "        return v.visit(this, arg);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.AcceptGenerator\")" + EOL + 
                "    public <A> void accept(final VoidVisitor<A> v, final A arg) {" + EOL + 
                "        v.visit(this, arg);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.PropertyGenerator\")" + EOL + 
                "    public Expression getCheck() {" + EOL + 
                "        return check;" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.PropertyGenerator\")" + EOL + 
                "    public Optional<Expression> getMessage() {" + EOL + 
                "        return Optional.ofNullable(message);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.PropertyGenerator\")" + EOL + 
                "    public AssertStmt setCheck(final Expression check) {" + EOL + 
                "        assertNotNull(check);" + EOL + 
                "        if (check == this.check) {" + EOL + 
                "            return (AssertStmt) this;" + EOL + 
                "        }" + EOL + 
                "        notifyPropertyChange(ObservableProperty.CHECK, this.check, check);" + EOL + 
                "        if (this.check != null)" + EOL + 
                "            this.check.setParentNode(null);" + EOL + 
                "        this.check = check;" + EOL + 
                "        setAsParentNodeOf(check);" + EOL + 
                "        return this;" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    /**" + EOL + 
                "     * Sets the message" + EOL + 
                "     *" + EOL + 
                "     * @param message the message, can be null" + EOL + 
                "     * @return this, the AssertStmt" + EOL + 
                "     */" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.PropertyGenerator\")" + EOL + 
                "    public AssertStmt setMessage(final Expression message) {" + EOL + 
                "        if (message == this.message) {" + EOL + 
                "            return (AssertStmt) this;" + EOL + 
                "        }" + EOL + 
                "        notifyPropertyChange(ObservableProperty.MESSAGE, this.message, message);" + EOL + 
                "        if (this.message != null)" + EOL + 
                "            this.message.setParentNode(null);" + EOL + 
                "        this.message = message;" + EOL + 
                "        setAsParentNodeOf(message);" + EOL + 
                "        return this;" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.RemoveMethodGenerator\")" + EOL + 
                "    public boolean remove(Node node) {" + EOL + 
                "        if (node == null)" + EOL + 
                "            return false;" + EOL + 
                "        if (message != null) {" + EOL + 
                "            if (node == message) {" + EOL + 
                "                removeMessage();" + EOL + 
                "                return true;" + EOL + 
                "            }" + EOL + 
                "        }" + EOL + 
                "        return super.remove(node);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.RemoveMethodGenerator\")" + EOL + 
                "    public AssertStmt removeMessage() {" + EOL + 
                "        return setMessage((Expression) null);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.CloneGenerator\")" + EOL + 
                "    public AssertStmt clone() {" + EOL + 
                "        return (AssertStmt) accept(new CloneVisitor(), null);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.GetMetaModelGenerator\")" + EOL + 
                "    public AssertStmtMetaModel getMetaModel() {" + EOL + 
                "        return JavaParserMetaModel.assertStmtMetaModel;" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.ReplaceMethodGenerator\")" + EOL + 
                "    public boolean replace(Node node, Node replacementNode) {" + EOL + 
                "        if (node == null)" + EOL + 
                "            return false;" + EOL + 
                "        if (node == check) {" + EOL + 
                "            setCheck((Expression) replacementNode);" + EOL + 
                "            return true;" + EOL + 
                "        }" + EOL + 
                "        if (message != null) {" + EOL + 
                "            if (node == message) {" + EOL + 
                "                setMessage((Expression) replacementNode);" + EOL + 
                "                return true;" + EOL + 
                "            }" + EOL + 
                "        }" + EOL + 
                "        return super.replace(node, replacementNode);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.TypeCastingGenerator\")" + EOL + 
                "    public boolean isAssertStmt() {" + EOL + 
                "        return true;" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.TypeCastingGenerator\")" + EOL + 
                "    public AssertStmt asAssertStmt() {" + EOL + 
                "        return this;" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.TypeCastingGenerator\")" + EOL + 
                "    public void ifAssertStmt(Consumer<AssertStmt> action) {" + EOL + 
                "        action.accept(this);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    @Generated(\"com.github.javaparser.generator.core.node.TypeCastingGenerator\")" + EOL + 
                "    public Optional<AssertStmt> toAssertStmt() {" + EOL + 
                "        return Optional.of(this);" + EOL + 
                "    }" + EOL + 
                "}" + EOL + 

                "";

        String expected =                 "/*" + EOL + 
                " * Copyright (C) 2007-2010 Júlio Vilmar Gesser." + EOL + 
                " * Copyright (C) 2011, 2013-2020 The JavaParser Team." + EOL + 
                " *" + EOL + 
                " * This file is part of JavaParser." + EOL + 
                " *" + EOL + 
                " * JavaParser can be used either under the terms of" + EOL + 
                " * a) the GNU Lesser General Public License as published by" + EOL + 
                " *     the Free Software Foundation, either version 3 of the License, or" + EOL + 
                " *     (at your option) any later version." + EOL + 
                " * b) the terms of the Apache License" + EOL + 
                " *" + EOL + 
                " * You should have received a copy of both licenses in LICENCE.LGPL and" + EOL + 
                " * LICENCE.APACHE. Please refer to those files for details." + EOL + 
                " *" + EOL + 
                " * JavaParser is distributed in the hope that it will be useful," + EOL + 
                " * but WITHOUT ANY WARRANTY; without even the implied warranty of" + EOL + 
                " * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the" + EOL + 
                " * GNU Lesser General Public License for more details." + EOL + 
                " */" + EOL + 
                "package com.github.javaparser.ast.stmt;" + EOL + 
                "" + EOL + 
                "import com.github.javaparser.ast.AllFieldsConstructor;" + EOL + 
                "import com.github.javaparser.ast.expr.BooleanLiteralExpr;" + EOL + 
                "import com.github.javaparser.ast.expr.Expression;" + EOL + 
                "import com.github.javaparser.ast.observer.ObservableProperty;" + EOL + 
                "import com.github.javaparser.ast.visitor.GenericVisitor;" + EOL + 
                "import com.github.javaparser.ast.visitor.VoidVisitor;" + EOL + 
                "import java.util.Optional;" + EOL + 
                "import static com.github.javaparser.utils.Utils.assertNotNull;" + EOL + 
                "import com.github.javaparser.ast.Node;" + EOL + 
                "import com.github.javaparser.ast.visitor.CloneVisitor;" + EOL + 
                "import com.github.javaparser.metamodel.AssertStmtMetaModel;" + EOL + 
                "import com.github.javaparser.metamodel.JavaParserMetaModel;" + EOL + 
                "import com.github.javaparser.TokenRange;" + EOL + 
                "import com.github.javaparser.metamodel.OptionalProperty;" + EOL + 
                "import java.util.function.Consumer;" + EOL + 
                "import com.github.javaparser.ast.Generated;" + EOL + 
                "" + EOL + 
                "/**" + EOL + 
                " * A usage of the keyword \"assert\"" + EOL + 
                " * <br>In {@code assert dead : \"Wasn't expecting to be dead here\";} the check is \"dead\" and the message is the string." + EOL + 
                " * @author Julio Vilmar Gesser" + EOL + 
                " */" + EOL + 
                "public class AssertStmt extends Statement {" + EOL + 
                "" + EOL + 
                "    private Expression check;" + EOL + 
                "" + EOL + 
                "    @OptionalProperty" + EOL + 
                "    private Expression message;" + EOL + 
                "" + EOL + 
                "    public AssertStmt() {" + EOL + 
                "        this(null, new BooleanLiteralExpr(), null);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    public AssertStmt(final Expression check) {" + EOL + 
                "        this(null, check, null);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @AllFieldsConstructor" + EOL + 
                "    public AssertStmt(final Expression check, final Expression message) {" + EOL + 
                "        this(null, check, message);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    /**" + EOL + 
                "     * This constructor is used by the parser and is considered private." + EOL + 
                "     */" + EOL + 
                "    public AssertStmt(TokenRange tokenRange, Expression check, Expression message) {" + EOL +
                "        super(tokenRange);" + EOL + 
                "        setCheck(check);" + EOL + 
                "        setMessage(message);" + EOL + 
                "        customInitialization();" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {" + EOL +
                "        return v.visit(this, arg);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    public <A> void accept(final VoidVisitor<A> v, final A arg) {" + EOL +
                "        v.visit(this, arg);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    public Expression getCheck() {" + EOL +
                "        return check;" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    public Optional<Expression> getMessage() {" + EOL +
                "        return Optional.ofNullable(message);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    public AssertStmt setCheck(final Expression check) {" + EOL +
                "        assertNotNull(check);" + EOL + 
                "        if (check == this.check) {" + EOL + 
                "            return (AssertStmt) this;" + EOL + 
                "        }" + EOL + 
                "        notifyPropertyChange(ObservableProperty.CHECK, this.check, check);" + EOL + 
                "        if (this.check != null)" + EOL + 
                "            this.check.setParentNode(null);" + EOL + 
                "        this.check = check;" + EOL + 
                "        setAsParentNodeOf(check);" + EOL + 
                "        return this;" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    /**" + EOL + 
                "     * Sets the message" + EOL + 
                "     *" + EOL + 
                "     * @param message the message, can be null" + EOL + 
                "     * @return this, the AssertStmt" + EOL + 
                "     */" + EOL + 
                "    public AssertStmt setMessage(final Expression message) {" + EOL +
                "        if (message == this.message) {" + EOL + 
                "            return (AssertStmt) this;" + EOL + 
                "        }" + EOL + 
                "        notifyPropertyChange(ObservableProperty.MESSAGE, this.message, message);" + EOL + 
                "        if (this.message != null)" + EOL + 
                "            this.message.setParentNode(null);" + EOL + 
                "        this.message = message;" + EOL + 
                "        setAsParentNodeOf(message);" + EOL + 
                "        return this;" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    public boolean remove(Node node) {" + EOL +
                "        if (node == null)" + EOL + 
                "            return false;" + EOL + 
                "        if (message != null) {" + EOL + 
                "            if (node == message) {" + EOL + 
                "                removeMessage();" + EOL + 
                "                return true;" + EOL + 
                "            }" + EOL + 
                "        }" + EOL + 
                "        return super.remove(node);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    public AssertStmt removeMessage() {" + EOL +
                "        return setMessage((Expression) null);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    public AssertStmt clone() {" + EOL +
                "        return (AssertStmt) accept(new CloneVisitor(), null);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    public AssertStmtMetaModel getMetaModel() {" + EOL +
                "        return JavaParserMetaModel.assertStmtMetaModel;" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    public boolean replace(Node node, Node replacementNode) {" + EOL +
                "        if (node == null)" + EOL + 
                "            return false;" + EOL + 
                "        if (node == check) {" + EOL + 
                "            setCheck((Expression) replacementNode);" + EOL + 
                "            return true;" + EOL + 
                "        }" + EOL + 
                "        if (message != null) {" + EOL + 
                "            if (node == message) {" + EOL + 
                "                setMessage((Expression) replacementNode);" + EOL + 
                "                return true;" + EOL + 
                "            }" + EOL + 
                "        }" + EOL + 
                "        return super.replace(node, replacementNode);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    public boolean isAssertStmt() {" + EOL +
                "        return true;" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    public AssertStmt asAssertStmt() {" + EOL +
                "        return this;" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    public void ifAssertStmt(Consumer<AssertStmt> action) {" + EOL +
                "        action.accept(this);" + EOL + 
                "    }" + EOL + 
                "" + EOL + 
                "    @Override" + EOL + 
                "    public Optional<AssertStmt> toAssertStmt() {" + EOL +
                "        return Optional.of(this);" + EOL + 
                "    }" + EOL + 
                "}" + EOL + 
                "";

        final Node b = javaParser.parse(code)
                .getResult()
                .orElseThrow(AssertionError::new);

        List<AnnotationExpr> allAnnotations = b.findAll(AnnotationExpr.class);

        List<Node> annotatedNodes = allAnnotations.stream()
                .filter(annotationExpr -> annotationExpr.getParentNode().isPresent())
                .map(annotationExpr -> annotationExpr.getParentNode().get())
                .collect(Collectors.toList());

        annotatedNodes.stream()
                .filter(node -> node instanceof NodeWithAnnotations)
                .map(node -> (NodeWithAnnotations<?>) node)
                .forEach(nodeWithAnnotations -> {
                    NodeList<AnnotationExpr> annotations = nodeWithAnnotations.getAnnotations();

                    NodeList<AnnotationExpr> newAnnotations = annotations.stream()
//                            .filter(annotationExpr -> !annotationExpr.getName().asString().equals(Override.class.getSimpleName()))
                            .filter(annotationExpr -> !annotationExpr.getName().asString().equals(Generated.class.getSimpleName()))
                            .collect(Collectors.toCollection(NodeList::new));

//                    nodeWithAnnotations.setAnnotations(new NodeList<>());
                    nodeWithAnnotations.setAnnotations(newAnnotations);
                });


        final String actual = LexicalPreservingPrinter.print(b);
        System.out.println(actual);
        assertEqualsNoEol(expected, actual);

//        final String actual2 = b.toString();
//        System.out.println(actual2);
//        assertEqualsNoEol(expected, actual2);
    }

}
