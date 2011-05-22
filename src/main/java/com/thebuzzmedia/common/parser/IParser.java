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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.thebuzzmedia.common.token.IToken;

/**
 * Interface used to define a stateful parser.
 * <p/>
 * {@link IParser}s are meant to process data from a given input source (e.g.
 * stream), likely keeping the data in some internal temporary structure (e.g.
 * read buffer) and then generating {@link IToken}s from that temporary
 * structure that becomes the token's <code>source</code>.
 * 
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * 
 * @param <IT>
 *            The type of the input that this parser will process (e.g.
 *            {@link InputStream}, {@link Reader}, etc).
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
public interface IParser<IT, TT, VT, ST> {
	public static final int DEFAULT_BUFFER_CAPACITY = 32768;

	/**
	 * Used to indicate to the parser that it should stop parsing at its
	 * earliest convenience.
	 * <p/>
	 * It is up to implementors to decide how frequently the parser should check
	 * to see if this flag has been flipped or what stop even does (e.g. close
	 * resource streams).
	 * 
	 * @throws UnsupportedOperationException
	 *             if the parser implementation does not support being stopped.
	 */
	public void stop() throws UnsupportedOperationException;

	/**
	 * Used to reset the internal state of the parser to prepare it for another
	 * parse operation.
	 * <p/>
	 * This should be called automatically by any implementations as part of the
	 * {@link #parse(Object, IParserCallback)} method.
	 */
	public void reset();

	/**
	 * Used to initiate a parse operation on the given input, invoking the given
	 * {@link IParserCallback} every time an {@link IToken} is successfully
	 * generated.
	 * 
	 * @param input
	 *            The input providing data for this parse operation.
	 * @param callback
	 *            The callback that will be invoked every time an {@link IToken}
	 *            is successfully generated.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>input</code> or <code>callback</code> is
	 *             <code>null</code>.
	 * @throws IOException
	 *             if an I/O problem occurs while reading information from the
	 *             input.
	 * @throws ParserException
	 *             if any error occurs while parsing input data (e.g. malformed
	 *             content) and the parser cannot continue.
	 */
	public void parse(IT input, IParserCallback<IT, TT, VT, ST> callback)
			throws IllegalArgumentException, IOException, ParserException;
}