/*
 * Copyright (C) 2015-2016 Federico Tomassetti
 * Copyright (C) 2017-2020 The JavaParser Team.
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

package com.github.javaparser_new.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser_new.ast.Node;
import com.github.javaparser_new.ast.body.VariableDeclarator;
import com.github.javaparser_new.ast.expr.AssignExpr;
import com.github.javaparser_new.ast.expr.Expression;
import com.github.javaparser_new.ast.expr.MethodCallExpr;
import com.github.javaparser_new.ast.expr.UnaryExpr;
import com.github.javaparser_new.ast.expr.VariableDeclarationExpr;
import com.github.javaparser_new.ast.nodeTypes.NodeWithStatements;
import com.github.javaparser_new.ast.stmt.ForStmt;
import com.github.javaparser_new.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser_new.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser_new.resolution.types.ResolvedType;
import com.github.javaparser_new.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration;
import com.github.javaparser_new.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser_new.symbolsolver.model.resolution.TypeSolver;

import java.util.LinkedList;
import java.util.List;

import static com.github.javaparser_new.symbolsolver.javaparser.Navigator.demandParentNode;

public class ForStatementContext extends AbstractJavaParserContext<ForStmt> {

    public ForStatementContext(ForStmt wrappedNode, TypeSolver typeSolver) {
        super(wrappedNode, typeSolver);
    }

    @Override
    public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name) {
        for (Expression expression : wrappedNode.getInitialization()) {
            if (expression instanceof VariableDeclarationExpr) {
                VariableDeclarationExpr variableDeclarationExpr = (VariableDeclarationExpr) expression;
                for (VariableDeclarator variableDeclarator : variableDeclarationExpr.getVariables()) {
                    if (variableDeclarator.getName().getId().equals(name)) {
                        return SymbolReference.solved(JavaParserSymbolDeclaration.localVar(variableDeclarator, typeSolver));
                    }
                }
            } else if (!(expression instanceof AssignExpr || expression instanceof MethodCallExpr || expression instanceof UnaryExpr)) {
                throw new UnsupportedOperationException(expression.getClass().getCanonicalName());
            }
        }

        if (demandParentNode(wrappedNode) instanceof NodeWithStatements) {
            return StatementContext.solveInBlock(name, typeSolver, wrappedNode);
        } else {
            return solveSymbolInParentContext(name);
        }
    }

    @Override
    public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly) {
        // TODO: Document why staticOnly is forced to be false.
        return solveMethodInParentContext(name, argumentsTypes, false);
    }

    @Override
    public List<VariableDeclarator> localVariablesExposedToChild(Node child) {
        List<VariableDeclarator> res = new LinkedList<>();
        for (Expression expression : wrappedNode.getInitialization()) {
            if (expression instanceof VariableDeclarationExpr) {
                VariableDeclarationExpr variableDeclarationExpr = (VariableDeclarationExpr) expression;
                res.addAll(variableDeclarationExpr.getVariables());
            }
        }
        return res;
    }
}
