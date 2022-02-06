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

package com.github.javaparser_new.symbolsolver.logic;

import com.github.javaparser_new.resolution.types.ResolvedType;

/**
 * @author Federico Tomassetti
 */
public class ConfilictingGenericTypesException extends RuntimeException {

    public ConfilictingGenericTypesException(ResolvedType formalType, ResolvedType actualType) {
        super(String.format("No matching between %s (formal) and %s (actual)", formalType, actualType));
    }
}
