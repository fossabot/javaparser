package com.github.javaparser.symbolsolver;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.junit.jupiter.api.Test;

import static com.github.javaparser.Providers.provider;

public class Issue2046 {


    @Test
    public void issue2046() {
        String x = "{" +
                "if (variable.toLowerCase().equals(variable1.toLowerCase().trim()) && !var.toLowerCase().equals(var1.toLowerCase().trim())) {\n" +
                "    int a = 2;\n" +
                "    if (aname.toLowerCase().contains(\"check\") || aname.toLowerCase().contains(\"first choice\")) {\n" +
                "        a.setType(\"------\");\n" +
                "    } else if (bname.toLowerCase().contains(\"hello\")) {\n" +
                "        a.setType(\"++++\");\n" +
                "    } else if (c.toLowerCase().contains(\"market\")) {\n" +
                "        a.setType(\"*****\");\n" +
                "    } else {\n" +
                "        a.setType(\"*****\");\n" +
                "    }\n" +
                "    break;\n" +
                "}" +
                "}";

        final JavaParser javaParser = new JavaParser();
        final ParseResult<Statement> result = javaParser.parse(ParseStart.STATEMENT, provider(x));

        result.ifSuccessful(statement -> {
//            System.out.println(statement);
//            System.out.println(statement);

            final BlockStmt blockStmt = (BlockStmt) statement;
            blockStmt.findAll(IfStmt.class).forEach(ifStmt -> {

                Node parentNode = null;
                Node grandParentNode = null;

                if (ifStmt.getParentNode().isPresent()) {
                    parentNode = ifStmt.getParentNode().get();
                    if (parentNode.getParentNode().isPresent()) {
                        grandParentNode = parentNode.getParentNode().get();
                    }
                }

                if (ifStmt.getParentNode().isPresent() && (parentNode instanceof IfStmt || grandParentNode instanceof IfStmt)) {
                    System.out.println("## ELSE IF: if(" + ifStmt.getCondition() + ") {");
                } else {
                    System.out.println("## IF: if(" + ifStmt.getCondition() + ") {");
                }

                System.out.println("## THEN: \n" + ifStmt.getCondition() + "\n");

                if (ifStmt.hasElseBlock() || ifStmt.hasElseBranch() && !(ifStmt.getElseStmt().get() instanceof IfStmt)) {
                    System.out.println("## ELSE \n" + ifStmt.getElseStmt());
                }

            });
        });
    }


}
