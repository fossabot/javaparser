/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2020 The JavaParser Team.
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

package com.github.javaparser.generator.core.node;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.generator.NodeGenerator;
import com.github.javaparser.metamodel.BaseNodeMetaModel;
import com.github.javaparser.metamodel.PropertyMetaModel;
import com.github.javaparser.utils.SourceRoot;

import static com.github.javaparser.StaticJavaParser.parseBodyDeclaration;
import static com.github.javaparser.utils.CodeGenerationUtils.f;
import static com.github.javaparser.utils.Utils.capitalize;


public class RemoveMethodGenerator extends NodeGenerator {

    public RemoveMethodGenerator(SourceRoot sourceRoot) {
        super(sourceRoot);
    }

    @Override
    protected void generateNode(BaseNodeMetaModel nodeMetaModel, CompilationUnit nodeCu, ClassOrInterfaceDeclaration nodeCoid) {
        MethodDeclaration removeNodeMethod = (MethodDeclaration) parseBodyDeclaration("public boolean remove(Node node) {}");
        nodeCu.addImport(Node.class);
        nodeMetaModel.getSuperNodeMetaModel().ifPresent(s -> this.annotateOverridden(removeNodeMethod));

        final BlockStmt body = removeNodeMethod.getBody().get();

        body.addStatement("if (node == null) return false;");

        for (PropertyMetaModel property : nodeMetaModel.getDeclaredPropertyMetaModels()) {
            if (!property.isNode()) {
                continue;
            }
            String check;
            if (property.isNodeList()) {
                check = this.nodeListCheck(property);
            } else {
                if (property.isRequired()) {
                    continue;
                }
                String removeAttributeMethodName = this.generateRemoveMethodForAttribute(nodeCoid, nodeMetaModel, property);
                check = this.attributeCheck(property, removeAttributeMethodName);
            }
            if (property.isOptional()) {
                check = f("if (this.%s != null) { %s }", property.getName(), check);
            }
            body.addStatement(check);
        }
        if (nodeMetaModel.getSuperNodeMetaModel().isPresent()) {
            body.addStatement("return super.remove(node);");
        } else {
            body.addStatement("return false;");
        }

        this.addOrReplaceWhenSameSignature(nodeCoid, removeNodeMethod);
    }

    private String attributeCheck(PropertyMetaModel property, String removeAttributeMethodName) {
        return f("if (node == this.%s) {" +
                "    this.%s();" +
                "    return true;\n" +
                "}", property.getName(), removeAttributeMethodName);
    }

    private String nodeListCheck(PropertyMetaModel property) {
        return f("for (int i = 0; i < this.%s.size(); i++) {" +
                "  if (this.%s.get(i) == node) {" +
                "    this.%s.remove(i);" +
                "    return true;" +
                "  }" +
                "}", property.getName(), property.getName(), property.getName());
    }

    private String generateRemoveMethodForAttribute(ClassOrInterfaceDeclaration nodeCoid, BaseNodeMetaModel nodeMetaModel, PropertyMetaModel property) {
        final String methodName = "remove" + capitalize(property.getName());
        final MethodDeclaration removeMethod = (MethodDeclaration) parseBodyDeclaration(f("public %s %s() {}", nodeMetaModel.getTypeName(), methodName));

        final BlockStmt block = removeMethod.getBody().get();
        block.addStatement(f("return this.%s((%s) null);", property.getSetterMethodName(), property.getTypeNameForSetter()));

        this.addOrReplaceWhenSameSignature(nodeCoid, removeMethod);
        return methodName;
    }
}
