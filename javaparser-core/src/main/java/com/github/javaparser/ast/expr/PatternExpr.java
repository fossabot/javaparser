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
package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Generated;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;

/**
 * @author Roger Howell
 */
public class PatternExpr extends Expression {

    private SimpleName name;

    private ReferenceType type;


    public PatternExpr() {
        this(null, new ClassOrInterfaceType(), new SimpleName());
    }

    @AllFieldsConstructor
    public PatternExpr(final ReferenceType type, SimpleName name) {
        this(null, type, name);
    }


    /**
     * This constructor is used by the parser and is considered private.
     */
    @Generated("com.github.javaparser.generator.core.node.MainConstructorGenerator")
    public PatternExpr(TokenRange tokenRange, final ReferenceType type, SimpleName name) {
        super(tokenRange);
//        setType(ReferenceType)
        customInitialization();
    }

    /**
     * Accept method for visitor support.
     *
     * @param v   the visitor implementation
     * @param arg the argument passed to the visitor (of type A)
     * @return the result of the visit (of type R)
     */
    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return null;
    }

    /**
     * Accept method for visitor support.
     *
     * @param v   the visitor implementation
     * @param arg any value relevant for the visitor (of type A)
     */
    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {

    }
}
