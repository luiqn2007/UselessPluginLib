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

package com.github.javaparser_new.ast.nodeTypes;

import com.github.javaparser_new.ast.Node;
import com.github.javaparser_new.ast.NodeList;
import com.github.javaparser_new.ast.type.ClassOrInterfaceType;

import static com.github.javaparser_new.StaticJavaParser.parseClassOrInterfaceType;

/**
 * A node that implements other types.
 */
public interface NodeWithImplements<N extends Node> {
    NodeList<ClassOrInterfaceType> getImplementedTypes();

    default ClassOrInterfaceType getImplementedTypes(int i) {
        return getImplementedTypes().get(i);
    }

    N setImplementedTypes(NodeList<ClassOrInterfaceType> implementsList);
    
    void tryAddImportToParentCompilationUnit(Class<?> clazz);
    
    @SuppressWarnings("unchecked")
    default N setImplementedType(int i, ClassOrInterfaceType implement) {
        getImplementedTypes().set(i, implement);
        return (N) this;
    }

    @SuppressWarnings("unchecked")
    default N addImplementedType(ClassOrInterfaceType implement) {
        getImplementedTypes().add(implement);
        return (N) this;
    }

    /**
     * Add an implements to this
     *
     * @param name the name of the type to extends from
     * @return this
     */
    @SuppressWarnings("unchecked")
    default N addImplementedType(String name) {
        getImplementedTypes().add(parseClassOrInterfaceType(name));
        return (N) this;
    }

    /**
     * Add an implements to this and automatically add the import
     *
     * @param clazz the type to implements from
     * @return this
     */
    default N addImplementedType(Class<?> clazz) {
        tryAddImportToParentCompilationUnit(clazz);
        return addImplementedType(clazz.getSimpleName());
    }
}
