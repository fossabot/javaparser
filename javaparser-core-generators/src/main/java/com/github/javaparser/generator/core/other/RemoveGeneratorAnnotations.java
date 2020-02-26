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

package com.github.javaparser.generator.core.other;

import com.github.javaparser.ParseResult;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Generated;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.generator.AbstractGenerator;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RemoveGeneratorAnnotations extends AbstractGenerator {

    public RemoveGeneratorAnnotations(SourceRoot sourceRoot) {
        super(sourceRoot);
    }

    @Override
    public List<CompilationUnit> generate() {
        Log.info("Running %s", () -> this.getClass().getSimpleName());

        try {
            List<CompilationUnit> cus = this.sourceRoot.getCompilationUnits();
            List<ParseResult<CompilationUnit>> parseResults = this.sourceRoot.tryToParse();

            boolean allParsesSuccessful = parseResults.stream().allMatch(ParseResult::isSuccessful);
            if (!allParsesSuccessful) {
                List<ParseResult<CompilationUnit>> problemResults = parseResults.stream().filter(compilationUnitParseResult -> !compilationUnitParseResult.isSuccessful()).collect(Collectors.toList());
                for (int i = 0; i < problemResults.size(); i++) {
                    ParseResult<CompilationUnit> parseResult = problemResults.get(i);
                    List<Problem> problems = parseResult.getProblems();
                    System.out.println(
                            "\nProblems (" + (i + 1) + " of " + problemResults.size() + "): " +
                            "\n" + problems
                    );
                }
                throw new IllegalStateException("Expected all files to parse.");
            }

            System.out.println("parseResults.size() = " + parseResults.size());

            List<CompilationUnit> pr = parseResults.stream()
                    .map(ParseResult::getResult)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            System.out.println("pr.size() = " + pr.size());
            pr.forEach(compilationUnit -> {
                List<AnnotationExpr> allAnnotations = compilationUnit.findAll(AnnotationExpr.class);
                allAnnotations.stream()
                        .filter(annotationExpr -> annotationExpr.getName().asString().equals(Generated.class.getSimpleName()))
                        .forEach(Node::remove);

                this.editedCus.add(compilationUnit);
            });

            return this.editedCus;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing the file -- IOException (see stack trace for details)", e);
        }
    }

}
