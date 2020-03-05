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

package com.github.javaparser.generator.core;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.generator.core.node.*;
import com.github.javaparser.generator.core.other.BndGenerator;
import com.github.javaparser.generator.core.other.RemoveGeneratorAnnotations;
import com.github.javaparser.generator.core.other.TokenKindGenerator;
import com.github.javaparser.generator.core.visitor.*;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.javaparser.ParserConfiguration.LanguageLevel.RAW;
import static com.github.javaparser.utils.CodeGenerationUtils.f;

/**
 * Generates all generated visitors in the javaparser-core module.
 * Suggested usage is by running the run_core_generators.sh script.
 * You may want to run_metamodel_generator.sh before that.
 */
public class CoreGenerator {

    private static final ParserConfiguration parserConfiguration = new ParserConfiguration()
            .setLanguageLevel(RAW)
//                                .setStoreTokens(false)
//                                .setAttributeComments(false)
//                                .setLexicalPreservationEnabled(true)
            ;
    private final SourceRoot jpCoreSourceRoot;
    private final SourceRoot jssLogicSourceRoot;
    private final SourceRoot jssModelSourceRoot;
    private final SourceRoot jssSourceRoot;
    private final SourceRoot generatedJavaCcSourceRoot;
    protected final List<SourceRoot> sourceRoots;
    public static final String GENERATOR_INIT_TIMESTAMP = String.valueOf(System.currentTimeMillis()) + "L";


    public CoreGenerator(Path projectRoot) {
        // Setup source roots
        this.jpCoreSourceRoot = getSourceRootForJpModule(projectRoot, "javaparser-core");
        this.jssLogicSourceRoot = getSourceRootForJpModule(projectRoot, "javaparser-symbol-solver-logic");
        this.jssModelSourceRoot = getSourceRootForJpModule(projectRoot, "javaparser-symbol-solver-model");
        this.jssSourceRoot = getSourceRootForJpModule(projectRoot, "javaparser-symbol-solver-core");

        final Path generatedJavaCcRoot = projectRoot.resolve("javaparser-core").resolve("target").resolve("generated-sources").resolve("javacc");
        this.generatedJavaCcSourceRoot = new SourceRoot(generatedJavaCcRoot, parserConfiguration);

        // Setup collection for later iteration
        this.sourceRoots = new ArrayList<>(7);
        this.sourceRoots.add(this.jpCoreSourceRoot);
        this.sourceRoots.add(this.jssLogicSourceRoot);
        this.sourceRoots.add(this.jssModelSourceRoot);
        this.sourceRoots.add(this.jssSourceRoot);
        this.sourceRoots.add(this.generatedJavaCcSourceRoot);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new RuntimeException("Need 1 parameter: the JavaParser generator source checkout root directory.");
        }
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        StaticJavaParser.setConfiguration(parserConfiguration);

        Path projectRoot = Paths.get(args[0]).resolve("..");

        // Do generating.
        final CoreGenerator coreGenerator = new CoreGenerator(projectRoot);
        coreGenerator.deleteAllGeneratorAnnotations(coreGenerator.sourceRoots);
        coreGenerator.runner();
    }

    private static SourceRoot getSourceRootForJpModule(Path projectRoot, String moduleName) {
        final Path jpCoreProjectRoot = projectRoot.resolve(moduleName).resolve("src").resolve("main").resolve("java");
        return getSourceRoot(jpCoreProjectRoot);
    }

    private static SourceRoot getSourceRoot(Path rootPath) {
        return new SourceRoot(rootPath, parserConfiguration); //.setPrinter(LexicalPreservingPrinter::print);
    }

    public void runner() throws Exception {
        this.run(this.jpCoreSourceRoot, this.generatedJavaCcSourceRoot);
    }

    public void deleteAllGeneratorAnnotations(List<SourceRoot> sourceRoots) {
        final String description = "deleteAllGeneratorAnnotations";
        final Set<CompilationUnit> allTouchedCus = new HashSet<>();

        // Do deletion for all source roots
        for (SourceRoot sourceRoot : sourceRoots) {
            try {
                final List<CompilationUnit> thisTouchedCus = new RemoveGeneratorAnnotations(sourceRoot).generate();
                allTouchedCus.addAll(thisTouchedCus);
                System.out.println(f(
                        "\n" +
                                "\nFinished running %s for sourceRoot %s" +
                                "\nsetup touched %d files within %s %n" +
                                "\n\n"
                        , description, sourceRoot, thisTouchedCus.size(), sourceRoot)
                );
            } catch (Exception e) {
                System.out.println(f("ERROR:: running %s for sourceRoot %s", description, sourceRoot));
                e.printStackTrace();
                throw new RuntimeException(f("ERROR:: running %s for sourceRoot %s", description, sourceRoot), e);
            }
        }

        // Save the files after removing the annotations.
        System.out.println(f("Finished %s for all sourceRoots", description));
        allTouchedCus.forEach(compilationUnit -> compilationUnit.getStorage().orElseThrow(IllegalStateException::new).save());

    }

    private void run(SourceRoot sourceRoot, SourceRoot generatedJavaCcSourceRoot) throws Exception {
        new TypeCastingGenerator(sourceRoot).generate();
        new GenericListVisitorAdapterGenerator(sourceRoot).generate();
        new GenericVisitorAdapterGenerator(sourceRoot).generate();
        new GenericVisitorWithDefaultsGenerator(sourceRoot).generate();
        new EqualsVisitorGenerator(sourceRoot).generate();
        new ObjectIdentityEqualsVisitorGenerator(sourceRoot).generate();
        new NoCommentEqualsVisitorGenerator(sourceRoot).generate();
        new VoidVisitorAdapterGenerator(sourceRoot).generate();
        new VoidVisitorGenerator(sourceRoot).generate();
        new VoidVisitorWithDefaultsGenerator(sourceRoot).generate();
        new GenericVisitorGenerator(sourceRoot).generate();
        new HashCodeVisitorGenerator(sourceRoot).generate();
        new ObjectIdentityHashCodeVisitorGenerator(sourceRoot).generate();
        new NoCommentHashCodeVisitorGenerator(sourceRoot).generate();
        new CloneVisitorGenerator(sourceRoot).generate();
        new ModifierVisitorGenerator(sourceRoot).generate();

        new PropertyGenerator(sourceRoot).generate();
        new RemoveMethodGenerator(sourceRoot).generate();
        new ReplaceMethodGenerator(sourceRoot).generate();
        new CloneGenerator(sourceRoot).generate();
        new GetMetaModelGenerator(sourceRoot).generate();
        new MainConstructorGenerator(sourceRoot).generate();
        new NodeModifierGenerator(sourceRoot).generate();
        new AcceptGenerator(sourceRoot).generate();
        new TokenKindGenerator(sourceRoot, generatedJavaCcSourceRoot).generate();
        new BndGenerator(sourceRoot).generate();

        sourceRoot.saveAll();
        generatedJavaCcSourceRoot.saveAll();
    }
}
