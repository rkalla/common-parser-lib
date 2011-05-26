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
package com.thebuzzmedia.common.parser;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import com.thebuzzmedia.common.io.IInput;

/*
 * TODO: Because of the need for using a read-buffer, Scanners must be stateful
 * since they are not thread-safe. Need to reconsider this API.
 */

/**
 * Interface used to define a stateless "scanner"-style parser.
 * <p/>
 * Scanners are intended to process their input in its entirety; returning all
 * the {@link IToken}s found in one method call. Unlike other stateful parsers
 * like {@link ITokenizer}s which are meant to wrap input and run in a loop.
 * 
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * 
 * @param <IT>
 *            The type of the input that this parser will process (e.g.
 *            {@link InputStream}, <code>byte[]</code>, {@link Reader}, etc).
 *            <p/>
 *            This is also the IT (input type) for the {@link IInput} the parser
 *            will process.
 * @param <TT>
 *            The type of the token, if necessary. If not needed, simply use a
 *            type of {@link Void}.
 * @param <VT>
 *            The type of the values returned by {@link IToken#getValue()}.
 *            <p/>
 *            This may be different from the &lt;ST&gt; type based on any
 *            processing the token does to the <code>source</code> while pulling
 *            the marked data from it. For example, the source might be a
 *            {@link StringBuilder} and the value returned by the token might be
 *            <code>char[]</code>.
 * @param <ST>
 *            The type of the <code>source</code> that the tokens created by
 *            this parser reference and pull {@link IToken#getValue()} values
 *            from.
 *            <p/>
 *            This is also the BT (buffer type) for the {@link IInput} the
 *            parser will process.
 *            <p/>
 *            It is not uncommon for this type to be different from the
 *            <code>IT</code> type. For example, a parser that processes data
 *            from an {@link InputStream} would have an <code>IT</code> type
 *            (input type) of {@link InputStream}, but a <code>ST</code> type
 *            (source type) of <code>byte[]</code> most likely.
 */

public interface IScanner<IT, TT, VT, ST> extends IParser<IT, TT, VT, ST> {
	/**
	 * Used to completely scan the given input source and return a {@link List}
	 * of the {@link IToken}s found.
	 * 
	 * @param input
	 *            The input source for the data that will be scanned (e.g.
	 *            <code>byte[]</code>, {@link InputStream}, etc).
	 * 
	 * @return a {@link List} of the {@link IToken}s found in the input data.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>input</code> is <code>null</code>.
	 * @throws ParseException
	 *             if any error occurs while trying to parse the data from the
	 *             given input (e.g. I/O error, malformed content, etc). Details
	 *             of the underlying exception and reason for failure can be
	 *             retrieved from the exception instance.
	 */
	public List<IToken<TT, VT, ST>> scan(IInput<IT, ST> input)
			throws IllegalArgumentException, ParseException;

	/**
	 * Used to scan the given input source and add all the {@link IToken}s found
	 * to the existing {@link List}.
	 * 
	 * @param input
	 *            The input source for the data that will be scanned (e.g.
	 *            <code>byte[]</code>, {@link InputStream}, etc).
	 * @param existingTokenList
	 *            An existing list to add tokens to.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>input</code> is <code>null</code> or if
	 *             <code>existingTokenList</code> is <code>null</code>.
	 * @throws ParseException
	 *             if any error occurs while trying to parse the data from the
	 *             given input (e.g. I/O error, malformed content, etc). Details
	 *             of the underlying exception and reason for failure can be
	 *             retrieved from the exception instance.
	 */
	public void scan(IInput<IT, ST> input,
			List<IToken<TT, VT, ST>> existingTokenList)
			throws IllegalArgumentException, ParseException;
}