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
package com.github.javaparser.ast.modules;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Generated;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.ModuleDirectiveMetaModel;
import java.util.Optional;
import java.util.function.Consumer;
import static com.github.javaparser.utils.CodeGenerationUtils.f;

/**
 * A module directive.
 */
public abstract class ModuleDirective extends Node {

    @AllFieldsConstructor
    public ModuleDirective() {
        this(null);
    }

    /**
     * This constructor is used by the parser and is considered private.
     */
    @Generated("com.github.javaparser.generator.core.node.MainConstructorGenerator")
    public ModuleDirective(TokenRange tokenRange) {
        super(tokenRange);
        this.customInitialization();
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.RemoveMethodGenerator")
    public boolean remove(Node node) {
        if (node == null) {
            return false;
        }
        return super.remove(node);
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.CloneGenerator")
    public ModuleDirective clone() {
        return (ModuleDirective) this.accept(new CloneVisitor(), null);
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.ReplaceMethodGenerator")
    public boolean replace(Node node, Node replacementNode) {
        if (node == null) {
            return false;
        }
        return super.replace(node, replacementNode);
    }

    /**
     * @deprecated in favour of {@link #isModuleExportsDirective()} following field rename.
     */
    @Deprecated
    public boolean isModuleExportsStmt() {
        // Delegate deprecated method to correct implementation.
        return this.isModuleExportsDirective();
    }

    /**
     * @deprecated in favour of {@link #asModuleExportsDirective()} following field rename.
     */
    @Deprecated
    public ModuleExportsDirective asModuleExportsStmt() {
        // Delegate deprecated method to correct implementation.
        return this.asModuleExportsDirective();
    }

    /**
     * @deprecated in favour of {@link #isModuleOpensDirective()} following field rename.
     */
    @Deprecated
    public boolean isModuleOpensStmt() {
        // Delegate deprecated method to correct implementation.
        return this.isModuleOpensDirective();
    }

    /**
     * @deprecated in favour of {@link #asModuleOpensDirective()} following field rename.
     */
    @Deprecated
    public ModuleOpensDirective asModuleOpensStmt() {
        // Delegate deprecated method to correct implementation.
        return this.asModuleOpensDirective();
    }

    /**
     * @deprecated in favour of {@link #isModuleProvidesDirective()} following field rename.
     */
    @Deprecated
    public boolean isModuleProvidesStmt() {
        // Delegate deprecated method to correct implementation.
        return this.isModuleProvidesDirective();
    }

    /**
     * @deprecated in favour of {@link #asModuleProvidesDirective()} following field rename.
     */
    @Deprecated
    public ModuleProvidesDirective asModuleProvidesStmt() {
        // Delegate deprecated method to correct implementation.
        return this.asModuleProvidesDirective();
    }

    /**
     * @deprecated in favour of {@link #isModuleRequiresDirective()} following field rename.
     */
    @Deprecated
    public boolean isModuleRequiresStmt() {
        // Delegate deprecated method to correct implementation.
        return this.isModuleRequiresDirective();
    }

    /**
     * @deprecated in favour of {@link #asModuleRequiresDirective()} following field rename.
     */
    @Deprecated
    public ModuleRequiresDirective asModuleRequiresStmt() {
        // Delegate deprecated method to correct implementation.
        return this.asModuleRequiresDirective();
    }

    /**
     * @deprecated in favour of {@link #isModuleUsesDirective()} following field rename.
     */
    @Deprecated
    public boolean isModuleUsesStmt() {
        // Delegate deprecated method to correct implementation.
        return this.isModuleUsesDirective();
    }

    /**
     * @deprecated in favour of {@link #asModuleUsesDirective()} following field rename.
     */
    @Deprecated
    public ModuleUsesDirective asModuleUsesStmt() {
        // Delegate deprecated method to correct implementation.
        return this.asModuleUsesDirective();
    }

    /**
     * @deprecated in favour of {@link #ifModuleExportsDirective(Consumer)} following field rename.
     */
    @Deprecated
    public void ifModuleExportsStmt(Consumer<ModuleExportsDirective> action) {
        // Delegate deprecated method to correct implementation.
        this.ifModuleExportsDirective(action);
    }

    /**
     * @deprecated in favour of {@link #ifModuleOpensDirective(Consumer)} following field rename.
     */
    @Deprecated
    public void ifModuleOpensStmt(Consumer<ModuleOpensDirective> action) {
        // Delegate deprecated method to correct implementation.
        this.ifModuleOpensDirective(action);
    }

    /**
     * @deprecated in favour of {@link #ifModuleProvidesDirective(Consumer)} following field rename.
     */
    @Deprecated
    public void ifModuleProvidesStmt(Consumer<ModuleProvidesDirective> action) {
        // Delegate deprecated method to correct implementation.
        this.ifModuleProvidesDirective(action);
    }

    /**
     * @deprecated in favour of {@link #ifModuleRequiresDirective(Consumer)} following field rename.
     */
    @Deprecated
    public void ifModuleRequiresStmt(Consumer<ModuleRequiresDirective> action) {
        // Delegate deprecated method to correct implementation.
        this.ifModuleRequiresDirective(action);
    }

    /**
     * @deprecated in favour of {@link #ifModuleUsesDirective(Consumer)} following field rename.
     */
    @Deprecated
    public void ifModuleUsesStmt(Consumer<ModuleUsesDirective> action) {
        // Delegate deprecated method to correct implementation.
        this.ifModuleUsesDirective(action);
    }

    /**
     * @deprecated in favour of {@link #toModuleExportsDirective()} following field rename.
     */
    @Deprecated
    public Optional<ModuleExportsDirective> toModuleExportsStmt() {
        // Delegate deprecated method to correct implementation.
        return this.toModuleExportsDirective();
    }

    /**
     * @deprecated in favour of {@link #toModuleOpensDirective()} following field rename.
     */
    @Deprecated
    public Optional<ModuleOpensDirective> toModuleOpensStmt() {
        // Delegate deprecated method to correct implementation.
        return this.toModuleOpensDirective();
    }

    /**
     * @deprecated in favour of {@link #toModuleProvidesDirective()} following field rename.
     */
    @Deprecated
    public Optional<ModuleProvidesDirective> toModuleProvidesStmt() {
        // Delegate deprecated method to correct implementation.
        return this.toModuleProvidesDirective();
    }

    /**
     * @deprecated in favour of {@link #toModuleRequiresDirective()} following field rename.
     */
    @Deprecated
    public Optional<ModuleRequiresDirective> toModuleRequiresStmt() {
        // Delegate deprecated method to correct implementation.
        return this.toModuleRequiresDirective();
    }

    /**
     * @deprecated in favour of {@link #toModuleUsesDirective()} following field rename.
     */
    @Deprecated
    public Optional<ModuleUsesDirective> toModuleUsesStmt() {
        // Delegate deprecated method to correct implementation.
        return this.toModuleUsesDirective();
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public boolean isModuleExportsDirective() {
        return false;
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public ModuleExportsDirective asModuleExportsDirective() {
        throw new IllegalStateException(f("%s is not an ModuleExportsDirective", this));
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public Optional<ModuleExportsDirective> toModuleExportsDirective() {
        return Optional.empty();
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public void ifModuleExportsDirective(Consumer<ModuleExportsDirective> action) {
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public boolean isModuleOpensDirective() {
        return false;
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public ModuleOpensDirective asModuleOpensDirective() {
        throw new IllegalStateException(f("%s is not an ModuleOpensDirective", this));
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public Optional<ModuleOpensDirective> toModuleOpensDirective() {
        return Optional.empty();
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public void ifModuleOpensDirective(Consumer<ModuleOpensDirective> action) {
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public boolean isModuleProvidesDirective() {
        return false;
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public ModuleProvidesDirective asModuleProvidesDirective() {
        throw new IllegalStateException(f("%s is not an ModuleProvidesDirective", this));
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public Optional<ModuleProvidesDirective> toModuleProvidesDirective() {
        return Optional.empty();
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public void ifModuleProvidesDirective(Consumer<ModuleProvidesDirective> action) {
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public boolean isModuleRequiresDirective() {
        return false;
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public ModuleRequiresDirective asModuleRequiresDirective() {
        throw new IllegalStateException(f("%s is not an ModuleRequiresDirective", this));
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public Optional<ModuleRequiresDirective> toModuleRequiresDirective() {
        return Optional.empty();
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public void ifModuleRequiresDirective(Consumer<ModuleRequiresDirective> action) {
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public boolean isModuleUsesDirective() {
        return false;
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public ModuleUsesDirective asModuleUsesDirective() {
        throw new IllegalStateException(f("%s is not an ModuleUsesDirective", this));
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public Optional<ModuleUsesDirective> toModuleUsesDirective() {
        return Optional.empty();
    }

    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public void ifModuleUsesDirective(Consumer<ModuleUsesDirective> action) {
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.GetMetaModelGenerator")
    public ModuleDirectiveMetaModel getMetaModel() {
        return JavaParserMetaModel.moduleDirectiveMetaModel;
    }
}
