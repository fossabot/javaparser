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

package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.GeneratedJavaParserConstants;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.printer.ConcreteSyntaxModel;
import com.github.javaparser.printer.Printable;
import com.github.javaparser.printer.SourcePrinter;
import com.github.javaparser.printer.concretesyntaxmodel.*;
import com.github.javaparser.printer.lexicalpreservation.changes.Change;
import com.github.javaparser.printer.lexicalpreservation.changes.ListAdditionChange;
import com.github.javaparser.printer.lexicalpreservation.changes.ListRemovalChange;
import com.github.javaparser.printer.lexicalpreservation.changes.ListReplacementChange;
import com.github.javaparser.printer.lexicalpreservation.changes.NoChange;
import com.github.javaparser.printer.lexicalpreservation.changes.PropertyChange;
import com.github.javaparser.utils.LineSeparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.github.javaparser.TokenTypes.eolTokenKind;

class LexicalDifferenceCalculator {

    /**
     * The ConcreteSyntaxModel represents the general format. This model is a calculated version of the ConcreteSyntaxModel,
     * with no condition, no lists, just tokens and node children.
     */
    static class CalculatedSyntaxModel {
        final List<CsmElement> elements;

        CalculatedSyntaxModel(List<CsmElement> elements) {
            this.elements = elements;
        }

        public CalculatedSyntaxModel from(int index) {
            return new CalculatedSyntaxModel(new ArrayList<>(elements.subList(index, elements.size())));
        }

        @Override
        public String toString() {
            return "CalculatedSyntaxModel{" +
                    "elements=" + elements +
                    '}';
        }

        CalculatedSyntaxModel sub(int start, int end) {
            return new CalculatedSyntaxModel(elements.subList(start, end));
        }

        void removeIndentationElements() {
            elements.removeIf(el -> el instanceof CsmIndent || el instanceof CsmUnindent);
        }
    }

    static class CsmChild implements CsmElement {
        private final Node child;
        private String contextNote;

        public Node getChild() {
            return child;
        }

        CsmChild(Node child) {
            this.child = child;
            this.contextNote = "";
        }

        @Override
        public CsmElement addToContextNote(String contextNote) {
            this.contextNote += contextNote;
            return this;
        }

        @Override
        public String getContextNote() {
            return this.contextNote;
        }

        @Override
        public void prettyPrint(Node node, SourcePrinter printer) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "child(" +
                    "childSimpleName='" + child.getClass().getSimpleName() + "'" +
                    ", contextNote='" + contextNote + "'" +
                    ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CsmChild csmChild = (CsmChild) o;

            return child.equals(csmChild.child);
        }

        @Override
        public int hashCode() {
            return child.hashCode();
        }
    }

    List<DifferenceElement> calculateListRemovalDifference(ObservableProperty observableProperty, NodeList<?> nodeList, int index) {
        Node container = nodeList.getParentNodeForChildren();
        CsmElement element = ConcreteSyntaxModel.forClass(container.getClass());
        CalculatedSyntaxModel original = calculatedSyntaxModelForNode(element, container);
        CalculatedSyntaxModel after = calculatedSyntaxModelAfterListRemoval(element, observableProperty, nodeList, index);
        return DifferenceElementCalculator.calculate(original, after);
    }

    List<DifferenceElement> calculateListAdditionDifference(ObservableProperty observableProperty, NodeList<?> nodeList, int index, Node nodeAdded) {
        Node container = nodeList.getParentNodeForChildren();
        CsmElement element = ConcreteSyntaxModel.forClass(container.getClass());
        CalculatedSyntaxModel original = calculatedSyntaxModelForNode(element, container);
        CalculatedSyntaxModel after = calculatedSyntaxModelAfterListAddition(element, observableProperty, nodeList, index, nodeAdded);

        List<DifferenceElement> differenceElements = DifferenceElementCalculator.calculate(original, after);

        // Set the line separator character tokens
        LineSeparator lineSeparator = container.getLineEndingStyleOrDefault(LineSeparator.SYSTEM);
        normaliseEolTokens(differenceElements, lineSeparator);

        return differenceElements;
    }

    private void normaliseEolTokens(List<DifferenceElement> differenceElements, LineSeparator desiredLineSeparator) {
        for (int i = 0; i < differenceElements.size(); i++) {
            DifferenceElement differenceElement = differenceElements.get(i);
            if (differenceElement.isAdded()) {
                CsmElement element = differenceElement.getElement();
                boolean isWhitespaceToken = element instanceof CsmToken && ((CsmToken) element).isNewLine();
                if (isWhitespaceToken) {
                    differenceElements.set(i, new Added(CsmElement.newline(desiredLineSeparator)));
                }
            }
        }
    }

    List<DifferenceElement> calculateListReplacementDifference(ObservableProperty observableProperty, NodeList<?> nodeList, int index, Node newValue) {
        Node container = nodeList.getParentNodeForChildren();
        CsmElement element = ConcreteSyntaxModel.forClass(container.getClass());
        CalculatedSyntaxModel original = calculatedSyntaxModelForNode(element, container);
        CalculatedSyntaxModel after = calculatedSyntaxModelAfterListReplacement(element, observableProperty, nodeList, index, newValue);
        return DifferenceElementCalculator.calculate(original, after);
    }

    void calculatePropertyChange(NodeText nodeText, Node observedNode, ObservableProperty property, Object oldValue, Object newValue) {
        if (nodeText == null) {
            throw new NullPointerException();
        }
        CsmElement element = ConcreteSyntaxModel.forClass(observedNode.getClass());
        CalculatedSyntaxModel original = calculatedSyntaxModelForNode(element, observedNode);
        CalculatedSyntaxModel after = calculatedSyntaxModelAfterPropertyChange(element, observedNode, property, oldValue, newValue);
        List<DifferenceElement> differenceElements = DifferenceElementCalculator.calculate(original, after);
        Difference difference = new Difference(differenceElements, nodeText, observedNode);
        difference.apply();
    }

    // Visible for testing
    CalculatedSyntaxModel calculatedSyntaxModelForNode(CsmElement csm, Node node) {
        List<CsmElement> elements = new LinkedList<>();
        calculatedSyntaxModelForNode(csm, node, elements, new NoChange(), "");
        return new CalculatedSyntaxModel(elements);
    }

    CalculatedSyntaxModel calculatedSyntaxModelForNode(Node node) {
        return calculatedSyntaxModelForNode(ConcreteSyntaxModel.forClass(node.getClass()), node);
    }

    private void calculatedSyntaxModelForNode(CsmElement csm, Node node, List<CsmElement> elements, Change change, String contextNote) {
//        contextNote += "; calcForNode";
//        csm.addToContextNote("; calcForNode");

        if (csm instanceof CsmSequence) {
            CsmSequence csmSequence = (CsmSequence) csm;
            for (CsmElement e : csmSequence.getElements()) {
//                e.addToContextNote("; element within CsmSequence");
//                e.addToContextNote("; exploded CsmSequence element");
                calculatedSyntaxModelForNode(e, node, elements, change, contextNote + "; exploded CsmSequence element");
            }
        } else if (csm instanceof CsmComment) {
            // nothing to do
        } else if (csm instanceof CsmSingleReference) {
            CsmSingleReference csmSingleReference = (CsmSingleReference)csm;
            Node child;
            if (change instanceof PropertyChange && ((PropertyChange)change).getProperty() == csmSingleReference.getProperty()) {
                child = (Node)((PropertyChange)change).getNewValue();
            } else {
                child = csmSingleReference.getProperty().getValueAsSingleReference(node);
            }
            if (child != null) {
                // fix issue #2374
                // Add node comment if needed (it's not very elegant but it works)
                // We need to be sure that the node is an ExpressionStmt because we can meet
                // this class definition
                // a line comment <This is my class, with my comment> followed by
                // class A {}
                // In this case keyworld [class] is considered as a token and [A] is a child element
                // So if we don't care that the node is an ExpressionStmt we could try to generate a wrong definition
                // like this [class // This is my class, with my comment A {}]
                if (node.getComment().isPresent() && node instanceof ExpressionStmt) {
                    LineSeparator lineSeparator = node.getLineEndingStyleOrDefault(LineSeparator.SYSTEM);
                    elements.add(new CsmChild(node.getComment().get()).addToContextNote(contextNote + "; comment to node"));
                    elements.add(new CsmToken(eolTokenKind(lineSeparator), lineSeparator.asRawString()).addToContextNote(contextNote + "; comment to node"));
                }
                elements.add(new CsmChild(child).addToContextNote(contextNote + "; CsmSingleReference"));
            }
        } else if (csm instanceof CsmNone) {
            // nothing to do
        } else if (csm instanceof CsmToken) {
            elements.add(csm);
        } else if (csm instanceof CsmOrphanCommentsEnding) {
            // nothing to do
        } else if (csm instanceof CsmList) {
            CsmList csmList = (CsmList) csm;
            if (csmList.getProperty().isAboutNodes()) {
                contextNote += "; is list of nodes";
                Object rawValue = change.getValue(csmList.getProperty(), node);

                // Setup an appropriately typed NodeList, else throw...
                NodeList<?> nodeList;
                if (rawValue instanceof Optional) {
                    Optional<?> optional = (Optional<?>)rawValue;
                    if (optional.isPresent()) {
                        if (optional.get() instanceof NodeList) {
                            nodeList = (NodeList<?>) optional.get();
                        } else {
                            throw new IllegalStateException("Expected NodeList, found " + optional.get().getClass().getCanonicalName());
                        }
                    } else {
                        nodeList = new NodeList<>();
                    }
                } else {
                    if (rawValue instanceof NodeList) {
                        nodeList = (NodeList<?>) rawValue;
                    } else {
                        throw new IllegalStateException("Expected NodeList, found " + rawValue.getClass().getCanonicalName());
                    }
                }

                // Next "explode" any lists/sequences so that we just have a flat list (rather than nested)...
                if (!nodeList.isEmpty()) {
                    // Insert whatever comes before the list starts.
                    calculatedSyntaxModelForNode(csmList.getPreceeding(), node, elements, change, contextNote + "; added because before list starting");
                    for (int i = 0; i < nodeList.size(); i++) {
                        if (i != 0) {
                            // If we're not the FIRST element, include the separator that comes BEFORE the element.
                            calculatedSyntaxModelForNode(csmList.getSeparatorPre(), node, elements, change, contextNote + "; added because separatorPre");
                        }
                        // Add the current element
                        elements.add(new CsmChild(nodeList.get(i)).addToContextNote(contextNote + "; added because element in list"));
                        if (i != (nodeList.size() - 1)) {
                            // If we're not the FINAL element, include the separator that comes AFTER the element.
                            calculatedSyntaxModelForNode(csmList.getSeparatorPost(), node, elements, change, contextNote + "; added because separatorPost");
                        }
                    }
                    // Insert whatever comes before the list ends.
                    calculatedSyntaxModelForNode(csmList.getFollowing(), node, elements, change, contextNote + "; added because listFollowing");
                }
            } else {
                contextNote += "; is list of NON nodes";
                Collection<?> collection = (Collection<?>) change.getValue(csmList.getProperty(), node);
                if (!collection.isEmpty()) {
                    calculatedSyntaxModelForNode(csmList.getPreceeding(), node, elements, change, contextNote + "; added because listPreceeding");

                    boolean first = true;
                    for (Iterator<?> it = collection.iterator(); it.hasNext(); ) {
                        if (!first) {
                            calculatedSyntaxModelForNode(csmList.getSeparatorPre(), node, elements, change, contextNote + "; added because separatorPre");
                        }
                        Object value = it.next();
                        if (value instanceof Modifier) {
                            Modifier modifier = (Modifier)value;
                            elements.add(new CsmToken(toToken(modifier)).addToContextNote(contextNote + "; added because element in list"));
                        } else {
                            throw new UnsupportedOperationException(it.next().getClass().getSimpleName());
                        }
                        if (it.hasNext()) {
                            calculatedSyntaxModelForNode(csmList.getSeparatorPost(), node, elements, change, contextNote + "; added because separatorPost");
                        }
                        first = false;
                    }
                    calculatedSyntaxModelForNode(csmList.getFollowing(), node, elements, change, contextNote + "; added because listFollowing");
                }
            }
        } else if (csm instanceof CsmConditional) {
            CsmConditional csmConditional = (CsmConditional) csm;
            boolean satisfied = change.evaluate(csmConditional, node);
            if (satisfied) {
                calculatedSyntaxModelForNode(csmConditional.getThenElement(), node, elements, change, contextNote + "; added because conditionSatisfied");
            } else {
                calculatedSyntaxModelForNode(csmConditional.getElseElement(), node, elements, change, contextNote + "; added because conditionNotSatisfied");
            }
        } else if (csm instanceof CsmIndent) {
            elements.add(csm);
        } else if (csm instanceof CsmUnindent) {
            elements.add(csm);
        } else if (csm instanceof CsmAttribute) {
            CsmAttribute csmAttribute = (CsmAttribute) csm;
            Object value = change.getValue(csmAttribute.getProperty(), node);
            String text = value.toString();
            if (value instanceof Printable) {
                text = ((Printable) value).asString();
            }
            elements.add(new CsmToken(csmAttribute.getTokenType(node, value.toString(), text), text).addToContextNote(contextNote));
        } else if ((csm instanceof CsmString) && (node instanceof StringLiteralExpr)) {
            // fix #2382:
            // This method calculates the syntax model _after_ the change has been applied.
            // If the given change is a PropertyChange, the returned model should
            // contain the new value, otherwise the original/current value should be used.
            if (change instanceof PropertyChange) {
                elements.add(new CsmToken(GeneratedJavaParserConstants.STRING_LITERAL,
                        "\"" + ((PropertyChange) change).getNewValue() + "\"").addToContextNote(contextNote));
            } else {
                elements.add(new CsmToken(GeneratedJavaParserConstants.STRING_LITERAL,
                        "\"" + ((StringLiteralExpr) node).getValue() + "\"").addToContextNote(contextNote));
            }
        } else if ((csm instanceof CsmString) && (node instanceof TextBlockLiteralExpr)) {
            // FIXME: csm should be CsmTextBlock -- See also #2677
            if (change instanceof PropertyChange) {
                elements.add(new CsmToken(GeneratedJavaParserConstants.TEXT_BLOCK_LITERAL,
                        "\"\"\"" + ((PropertyChange) change).getNewValue() + "\"\"\"").addToContextNote(contextNote));
            } else {
                elements.add(new CsmToken(GeneratedJavaParserConstants.TEXT_BLOCK_LITERAL,
                        "\"\"\"" + ((TextBlockLiteralExpr) node).getValue() + "\"\"\"").addToContextNote(contextNote));
            }
        } else if ((csm instanceof CsmChar) && (node instanceof CharLiteralExpr)) {
            if (change instanceof PropertyChange) {
                elements.add(new CsmToken(GeneratedJavaParserConstants.CHAR,
                        "'" + ((PropertyChange) change).getNewValue() + "'").addToContextNote(contextNote));
            } else {
                elements.add(new CsmToken(GeneratedJavaParserConstants.CHAR,
                        "'" + ((CharLiteralExpr) node).getValue() + "'").addToContextNote(contextNote));
            }
        } else if (csm instanceof CsmMix) {
            CsmMix csmMix = (CsmMix)csm;
            List<CsmElement> mixElements = new LinkedList<>();
            for (CsmElement e : csmMix.getElements()) {
                calculatedSyntaxModelForNode(e, node, mixElements, change, contextNote + "; added because element within CsmMix");
            }
            elements.add(new CsmMix(mixElements).addToContextNote(contextNote));
        } else if (csm instanceof CsmChild) {
            elements.add(csm);
        } else {
            throw new UnsupportedOperationException(csm.getClass().getSimpleName()+ " " + csm);
        }
    }

    public static int toToken(Modifier modifier) {
        switch (modifier.getKeyword()) {
            case PUBLIC:
                return GeneratedJavaParserConstants.PUBLIC;
            case PRIVATE:
                return GeneratedJavaParserConstants.PRIVATE;
            case PROTECTED:
                return GeneratedJavaParserConstants.PROTECTED;
            case STATIC:
                return GeneratedJavaParserConstants.STATIC;
            case FINAL:
                return GeneratedJavaParserConstants.FINAL;
            case ABSTRACT:
                return GeneratedJavaParserConstants.ABSTRACT;
            case TRANSIENT:
                return GeneratedJavaParserConstants.TRANSIENT;
            case SYNCHRONIZED:
                return GeneratedJavaParserConstants.SYNCHRONIZED;
            case VOLATILE:
                return GeneratedJavaParserConstants.VOLATILE;
            case NATIVE:
                return GeneratedJavaParserConstants.NATIVE;
            case STRICTFP:
                return GeneratedJavaParserConstants.STRICTFP;
            case TRANSITIVE:
                return GeneratedJavaParserConstants.TRANSITIVE;
            default:
                throw new UnsupportedOperationException(modifier.getKeyword().name());
        }
    }

    ///
    /// Methods that calculate CalculatedSyntaxModel
    ///

    // Visible for testing
    CalculatedSyntaxModel calculatedSyntaxModelAfterPropertyChange(Node node, ObservableProperty property, Object oldValue, Object newValue) {
        return calculatedSyntaxModelAfterPropertyChange(ConcreteSyntaxModel.forClass(node.getClass()), node, property, oldValue, newValue);
    }

    // Visible for testing
    CalculatedSyntaxModel calculatedSyntaxModelAfterPropertyChange(CsmElement csm, Node node, ObservableProperty property, Object oldValue, Object newValue) {
        List<CsmElement> elements = new LinkedList<>();
        calculatedSyntaxModelForNode(csm, node, elements, new PropertyChange(property, oldValue, newValue), "");
        return new CalculatedSyntaxModel(elements);
    }

    // Visible for testing
    CalculatedSyntaxModel calculatedSyntaxModelAfterListRemoval(CsmElement csm, ObservableProperty observableProperty, NodeList<?> nodeList, int index) {
        List<CsmElement> elements = new LinkedList<>();
        Node container = nodeList.getParentNodeForChildren();
        if(nodeList.size() == 1) {
            // We're about to remove the last element in the list.
            calculatedSyntaxModelForNode(csm, container, elements, new ListRemovalChange(observableProperty, index), "");
        } else {
            calculatedSyntaxModelForNode(csm, container, elements, new ListRemovalChange(observableProperty, index), "");
        }
        return new CalculatedSyntaxModel(elements);
    }

    // Visible for testing
    CalculatedSyntaxModel calculatedSyntaxModelAfterListAddition(CsmElement csm, ObservableProperty observableProperty, NodeList<?> nodeList, int index, Node nodeAdded) {
        List<CsmElement> elements = new LinkedList<>();
        Node container = nodeList.getParentNodeForChildren();
        calculatedSyntaxModelForNode(csm, container, elements, new ListAdditionChange(observableProperty, index, nodeAdded), "");
        return new CalculatedSyntaxModel(elements);
    }

    // Visible for testing
    CalculatedSyntaxModel calculatedSyntaxModelAfterListAddition(Node container, ObservableProperty observableProperty, int index, Node nodeAdded) {
        CsmElement csm = ConcreteSyntaxModel.forClass(container.getClass());
        Object rawValue = observableProperty.getRawValue(container);
        if (!(rawValue instanceof NodeList)) {
            throw new IllegalStateException("Expected NodeList, found " + rawValue.getClass().getCanonicalName());
        }
        NodeList<?> nodeList = (NodeList<?>)rawValue;
        return calculatedSyntaxModelAfterListAddition(csm, observableProperty, nodeList, index, nodeAdded);
    }

    // Visible for testing
    CalculatedSyntaxModel calculatedSyntaxModelAfterListRemoval(Node container, ObservableProperty observableProperty, int index) {
        CsmElement csm = ConcreteSyntaxModel.forClass(container.getClass());
        Object rawValue = observableProperty.getRawValue(container);
        if (!(rawValue instanceof NodeList)) {
            throw new IllegalStateException("Expected NodeList, found " + rawValue.getClass().getCanonicalName());
        }
        NodeList<?> nodeList = (NodeList<?>)rawValue;
        return calculatedSyntaxModelAfterListRemoval(csm, observableProperty, nodeList, index);
    }

    // Visible for testing
    private CalculatedSyntaxModel calculatedSyntaxModelAfterListReplacement(CsmElement csm, ObservableProperty observableProperty, NodeList<?> nodeList, int index, Node newValue) {
        List<CsmElement> elements = new LinkedList<>();
        Node container = nodeList.getParentNodeForChildren();
        calculatedSyntaxModelForNode(csm, container, elements, new ListReplacementChange(observableProperty, index, newValue), "");
        return new CalculatedSyntaxModel(elements);
    }

}
