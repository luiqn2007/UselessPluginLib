/* Generated by: ParserGeneratorCC: Do not edit this line. SimpleCharStream.java Version 1.1 */
/* ParserGeneratorCCOptions:SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
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
package com.github.javaparser_new;

/**
 * An implementation of interface CharStream, where the stream is assumed to
 * contain only ASCII characters (without unicode processing).
 */
public
class SimpleCharStream extends AbstractCharStream
{
  private Provider inputStream;

  @Override
  protected int streamRead (final char[] aBuf, final int nOfs, final int nLen) throws java.io.IOException
  {
    return inputStream.read (aBuf, nOfs, nLen); 
  }
  
  @Override
  protected void streamClose() throws java.io.IOException
  {
    inputStream.close (); 
  }

  /** Constructor. */
  public SimpleCharStream(final Provider dstream,
                          final int startline,
                          final int startcolumn,
                          final int buffersize)
  {
    super (startline, startcolumn, buffersize);
    inputStream = dstream;
  }

  /** Constructor. */
  public SimpleCharStream(final Provider dstream,
                          final int startline,
                          final int startcolumn)
  {
    this(dstream, startline, startcolumn, DEFAULT_BUF_SIZE);
  }

  /** Constructor. */
  public SimpleCharStream(final Provider dstream)
  {
    this(dstream, 1, 1, DEFAULT_BUF_SIZE);
  }

  /** Reinitialise. */
  public void reInit(final Provider dstream,
                     final int startline,
                     final int startcolumn,
                     final int buffersize)
  {
    inputStream = dstream;
    super.reInit (startline, startcolumn, buffersize);
  }

  /** Reinitialise. */
  public void reInit(final Provider dstream,
                     final int startline,
                     final int startcolumn)
  {
    reInit(dstream, startline, startcolumn, DEFAULT_BUF_SIZE);
  }

  /** Reinitialise. */
  public void reInit(final Provider dstream)
  {
    reInit(dstream, 1, 1, DEFAULT_BUF_SIZE);
  }
}
/* ParserGeneratorCC - OriginalChecksum=9b6ba29c6edc2d17a5f2a6053a85cc82 (do not edit this line) */
