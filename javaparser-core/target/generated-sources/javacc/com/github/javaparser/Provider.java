/* Generated by: ParserGeneratorCC: Do not edit this line. Provider.java Version 1.1 */
/* ParserGeneratorCCOptions:KEEP_LINE_COLUMN=true */
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
package com.github.javaparser;

import java.io.IOException;

/**
 * Abstract interface for reading from a stream.
 * The buffering should be done internally.
 */
public interface Provider extends java.io.Closeable
{
  /**
   * Reads characters into an array
   *
   * @param aDest Destination buffer. May not be <code>null</code>.
   * @param nOfs Offset at which to start storing characters. Must be &ge; 0.
   * @param nLen The maximum possible number of characters to read. Must be &ge; 0.
   * @return The number of characters read, or -1 at the end of the stream
   * @exception IOException if reading fails
   */
  int read (char [] aDest, int nOfs, int nLen) throws IOException;
}
 
/* ParserGeneratorCC - OriginalChecksum=f1df1f7caa529cc6a5e596afa50c1669 (do not edit this line) */
