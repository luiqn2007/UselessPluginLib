/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2021 The JavaParser Team.
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
package com.github.javaparser_new.ast.stmt;

import com.github.javaparser_new.TokenRange;
import com.github.javaparser_new.ast.AllFieldsConstructor;
import com.github.javaparser_new.ast.Generated;
import com.github.javaparser_new.ast.Node;
import com.github.javaparser_new.ast.expr.Expression;
import com.github.javaparser_new.ast.expr.NameExpr;
import com.github.javaparser_new.ast.nodeTypes.NodeWithExpression;
import com.github.javaparser_new.ast.observer.ObservableProperty;
import com.github.javaparser_new.ast.visitor.CloneVisitor;
import com.github.javaparser_new.ast.visitor.GenericVisitor;
import com.github.javaparser_new.ast.visitor.VoidVisitor;
import com.github.javaparser_new.metamodel.JavaParserMetaModel;
import com.github.javaparser_new.metamodel.ThrowStmtMetaModel;
import java.util.Optional;
import java.util.function.Consumer;
import static com.github.javaparser_new.utils.Utils.assertNotNull;

/**
 * Usage of the throw statement.
 * <br>{@code throw new Exception()}
 *
 * @author Julio Vilmar Gesser
 */
public class ThrowStmt extends Statement implements NodeWithExpression<ThrowStmt> {

    private Expression expression;

    public ThrowStmt() {
        this(null, new NameExpr());
    }

    @AllFieldsConstructor
    public ThrowStmt(final Expression expression) {
        this(null, expression);
    }

    /**
     * This constructor is used by the parser and is considered private.
     */
    @Generated("com.github.javaparser.generator.core.node.MainConstructorGenerator")
    public ThrowStmt(TokenRange tokenRange, Expression expression) {
        super(tokenRange);
        setExpression(expression);
        customInitialization();
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.AcceptGenerator")
    public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
        return v.visit(this, arg);
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.AcceptGenerator")
    public <A> void accept(final VoidVisitor<A> v, final A arg) {
        v.visit(this, arg);
    }

    @Generated("com.github.javaparser.generator.core.node.PropertyGenerator")
    public Expression getExpression() {
        return expression;
    }

    @Generated("com.github.javaparser.generator.core.node.PropertyGenerator")
    public ThrowStmt setExpression(final Expression expression) {
        assertNotNull(expression);
        if (expression == this.expression) {
            return this;
        }
        notifyPropertyChange(ObservableProperty.EXPRESSION, this.expression, expression);
        if (this.expression != null)
            this.expression.setParentNode(null);
        this.expression = expression;
        setAsParentNodeOf(expression);
        return this;
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.CloneGenerator")
    public ThrowStmt clone() {
        return (ThrowStmt) accept(new CloneVisitor(), null);
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.GetMetaModelGenerator")
    public ThrowStmtMetaModel getMetaModel() {
        return JavaParserMetaModel.throwStmtMetaModel;
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.ReplaceMethodGenerator")
    public boolean replace(Node node, Node replacementNode) {
        if (node == null) {
            return false;
        }
        if (node == expression) {
            setExpression((Expression) replacementNode);
            return true;
        }
        return super.replace(node, replacementNode);
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public boolean isThrowStmt() {
        return true;
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public ThrowStmt asThrowStmt() {
        return this;
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public void ifThrowStmt(Consumer<ThrowStmt> action) {
        action.accept(this);
    }

    @Override
    @Generated("com.github.javaparser.generator.core.node.TypeCastingGenerator")
    public Optional<ThrowStmt> toThrowStmt() {
        return Optional.of(this);
    }
}
