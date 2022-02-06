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

package com.github.javaparser_new.printer.lexicalpreservation.changes;

import com.github.javaparser_new.ast.Node;
import com.github.javaparser_new.ast.observer.ObservableProperty;
import com.github.javaparser_new.printer.concretesyntaxmodel.CsmConditional;
import com.github.javaparser_new.utils.Utils;

/**
 * This represents a change that has happened to a specific Node.
 */
public interface Change {

    default boolean evaluate(CsmConditional csmConditional, Node node) {
        switch (csmConditional.getCondition()) {
            case FLAG:
                return csmConditional.getProperties().stream().anyMatch(p -> (Boolean) getValue(p, node));
            case IS_NOT_EMPTY:
                return !Utils.valueIsNullOrEmpty(getValue(csmConditional.getProperty(), node));
            case IS_EMPTY:
                return Utils.valueIsNullOrEmpty(getValue(csmConditional.getProperty(), node));
            case IS_PRESENT:
                return !Utils.valueIsNullOrEmptyStringOrOptional(getValue(csmConditional.getProperty(), node));
            default:
                throw new UnsupportedOperationException("" + csmConditional.getProperty() + " " + csmConditional.getCondition());
        }
    }

    Object getValue(ObservableProperty property, Node node);
}
