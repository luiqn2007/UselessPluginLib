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

package com.github.javaparser_new.symbolsolver.javaparsermodel.declarations;

import static com.github.javaparser_new.symbolsolver.javaparser.Navigator.demandParentNode;

import java.util.Optional;

import com.github.javaparser_new.ast.AccessSpecifier;
import com.github.javaparser_new.ast.Modifier;
import com.github.javaparser_new.ast.body.FieldDeclaration;
import com.github.javaparser_new.ast.body.TypeDeclaration;
import com.github.javaparser_new.ast.body.VariableDeclarator;
import com.github.javaparser_new.resolution.declarations.AssociableToAST;
import com.github.javaparser_new.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser_new.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser_new.resolution.types.ResolvedType;
import com.github.javaparser_new.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser_new.symbolsolver.model.resolution.TypeSolver;

/**
 * @author Federico Tomassetti
 */
public class JavaParserFieldDeclaration implements ResolvedFieldDeclaration, AssociableToAST<FieldDeclaration> {

    private VariableDeclarator variableDeclarator;
    private FieldDeclaration wrappedNode;
    private TypeSolver typeSolver;

    public JavaParserFieldDeclaration(VariableDeclarator variableDeclarator, TypeSolver typeSolver) {
        if (typeSolver == null) {
            throw new IllegalArgumentException("typeSolver should not be null");
        }
        this.variableDeclarator = variableDeclarator;
        this.typeSolver = typeSolver;
        if (!(demandParentNode(variableDeclarator) instanceof FieldDeclaration)) {
            throw new IllegalStateException(demandParentNode(variableDeclarator).getClass().getCanonicalName());
        }
        this.wrappedNode = (FieldDeclaration) demandParentNode(variableDeclarator);
    }

    @Override
    public ResolvedType getType() {
        return JavaParserFacade.get(typeSolver).convert(variableDeclarator.getType(), wrappedNode);
    }

    @Override
    public String getName() {
        return variableDeclarator.getName().getId();
    }

    @Override
    public boolean isStatic() {
        return wrappedNode.hasModifier(Modifier.Keyword.STATIC);
    }
    
    @Override
    public boolean isVolatile() {
        return wrappedNode.hasModifier(Modifier.Keyword.VOLATILE);
    }

    @Override
    public boolean isField() {
        return true;
    }

    /**
     * Returns the JavaParser node associated with this JavaParserFieldDeclaration.
     *
     * @return A visitable JavaParser node wrapped by this object.
     */
    public FieldDeclaration getWrappedNode() {
        return wrappedNode;
    }

    public VariableDeclarator getVariableDeclarator() {
        return variableDeclarator;
    }

    @Override
    public String toString() {
        return "JavaParserFieldDeclaration{" + getName() + "}";
    }

    @Override
    public AccessSpecifier accessSpecifier() {
        return wrappedNode.getAccessSpecifier();
    }

    @Override
    public ResolvedTypeDeclaration declaringType() {
        Optional<TypeDeclaration> typeDeclaration = wrappedNode.findAncestor(TypeDeclaration.class);
        if (typeDeclaration.isPresent()) {
            return JavaParserFacade.get(typeSolver).getTypeDeclaration(typeDeclaration.get());
        }
        throw new IllegalStateException();
    }
    
    @Override
    public Optional<FieldDeclaration> toAst() {
        return Optional.ofNullable(wrappedNode);
    }
}