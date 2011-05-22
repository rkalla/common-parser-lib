/**   
 * Copyright 2011 The Buzz Media, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thebuzzmedia.common.scanner;

import java.util.List;

import com.thebuzzmedia.common.lexer.ITokenizer;
import com.thebuzzmedia.common.parser.IParser;
import com.thebuzzmedia.common.token.IToken;

/**
 * Interface used to define a stateless scanner.
 * <p/>
 * {@link IScanner}s are intended to process a source entirely, returning all
 * {@link IToken}s found in one method call. Unlike {@link ITokenizer}s which
 * are meant to be run in a loop, querying for the next token each time.
 * <p/>
 * {@link IScanner}s are also different from {@link IParser}s in that they are
 * meant to work with in-memory representations of the data (e.g. arrays) such
 * that the source passed into the <code>scan</code> methods is the same source
 * reported by the generated tokens via {@link IToken#getSource()}.
 * <p/>
 * This is unlike {@link IParser}s which are expected to process data from an
 * input (e.g. stream) that does not have the same type as the eventual
 * <code>source</code> passed to the token (e.g. in the case of a stream input,
 * the source to the token is generally a primitive array type).
 * 
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * 
 * @param <TT>
 *            The type of the token, if necessary. If not needed, simply use a
 *            type of {@link Void}.
 * @param <VT>
 *            The type of {@link IToken#getValue()} as extracted from the given
 *            <code>source</code> (e.g. <code>String</code>, <code>int</code>,
 *            <code>char[]</code>, etc).
 * @param <ST>
 *            The type of the <code>source</code> that the tokens will pull
 *            their values from (e.g. <code>StringBuilder</code>,
 *            <code>InputStream</code>, <code>byte[]</code>, etc).
 */

public interface IScanner<TT, VT, ST> {
	/**
	 * Used to scan a range of data from the given source and return a
	 * {@link List} of the {@link IToken}s found.
	 * 
	 * @param source
	 *            The source data that will be scanned (e.g. <code>byte[]</code>
	 *            ).
	 * @param index
	 *            The index in the source data to start at.
	 * @param length
	 *            The amount of data to scan in the source, starting at
	 *            <code>index</code>.
	 * 
	 * @return a {@link List} of the {@link IToken}s found in the source data.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>source</code> is <code>null</code>, if
	 *             <code>index</code> &lt; 0, if <code>length</code> < 0 or if (
	 *             <code>index + length</code>) is &gt; the bounds of the
	 *             source.
	 */
	public List<IToken<TT, VT, ST>> scan(ST source, int index, int length)
			throws IllegalArgumentException;

	/**
	 * Used to scan a range of data from the given source and add all the
	 * {@link IToken}s found to the existing {@link List}.
	 * 
	 * @param source
	 *            The source data that will be scanned (e.g. <code>byte[]</code>
	 *            ).
	 * @param index
	 *            The index in the source data to start at.
	 * @param length
	 *            The amount of data to scan in the source, starting at
	 *            <code>index</code>.
	 * @param existingTokenList
	 *            An existing list to add tokens to.
	 * 
	 * @return a {@link List} of the {@link IToken}s found in the source data.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>source</code> is <code>null</code>, if
	 *             <code>index</code> &lt; 0, if <code>length</code> < 0, if (
	 *             <code>index + length</code>) is &gt; the bounds of the source
	 *             or if <code>existingTokenList</code> is <code>null</code>.
	 */
	public List<IToken<TT, VT, ST>> scan(ST source, int index, int length,
			List<IToken<TT, VT, ST>> existingTokenList)
			throws IllegalArgumentException;
}