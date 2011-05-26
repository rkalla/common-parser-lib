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

import com.thebuzzmedia.common.io.IInput;

/**
 * Interface used to define the base properties shared by all parsers
 * implemented in this library.
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
public interface IParser<IT, TT, VT, ST> {
	/**
	 * Default size for the read buffers utilized by all parsers provided by
	 * this library.
	 * <p/>
	 * Value is: 32768 (32k)
	 * <p/>
	 * All parsers in this library utilize {@link IInput} instances for
	 * accessing the given input for the parsing process. {@link IInput}
	 * instances function by reading data out of whatever underlying source is
	 * wrapped into a caller-provided buffer, so all parser implementations in
	 * this library utilize a read buffer.
	 */
	public static final int DEFAULT_BUFFER_CAPACITY = 32768;

	/**
	 * Used to provide a mechanism for parsers to reset their internal state
	 * when necessary.
	 * <p/>
	 * Subclasses should automatically call this when appropriate and not rely
	 * on the caller to invoke a reset at the appropriate time.
	 */
	public void reset();

	/**
	 * Used to indicate to the parser that it should stop parsing at its
	 * earliest convenience (or execute whatever stop-logic the particular
	 * parser implements).
	 * <p/>
	 * A "stopped" parser cannot be restarted. A new parse operation would need
	 * to be executed to re-initialize its state and "unstop" it.
	 * <p/>
	 * It is possible that a parser stops itself if it determines that it cannot
	 * continue parsing. For example, the underlying {@link IInput} has run out
	 * of data and the read buffer is empty.
	 * <p/>
	 * It is up to the implementation to decide what this operation does, but it
	 * should be a safe and not generate an exception in any circumstance. A
	 * common implementation is to set an internal flag to indicate a stop
	 * request has been executed and the parser can then exit when ready.
	 * <p/>
	 * This operation may not be supported by every parser type. If the parser
	 * does not support being "stopped", this call would be a no-op.
	 */
	public void stop();

	/**
	 * Used to determine if the parser has been stopped.
	 * <p/>
	 * It is possible that a parser stops itself if it determines that it cannot
	 * continue parsing. For example, the underlying {@link IInput} has run out
	 * of data and the read buffer is empty.
	 * 
	 * @return <code>true</code> if {@link #stop()} has been issued on the
	 *         parser and it is in a stopped state, otherwise <code>false</code>
	 *         .
	 */
	public boolean isStopped();

	/**
	 * Used to get the <code>input</code> providing data to this parser.
	 * 
	 * @return the {@link IInput} providing data to this parser.
	 */
	public IInput<IT, ST> getInput();

	/**
	 * Used to set the <code>input</code> that will provide data to this parser
	 * as well as any initialization of the input the parser needs to prepare
	 * for the first parse operation (e.g. do an initial read buffer fill).
	 * 
	 * @param input
	 *            The source of data for this parser.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>input</code> is <code>null</code>.
	 * @throws ParseException
	 *             if any exception occurs while attempting the initial buffer
	 *             fill operation from the given <code>input</code>.
	 */
	public void setInput(IInput<IT, ST> input) throws IllegalArgumentException,
			ParseException;

	/**
	 * Used to indicate that the parser is re-using the same mutable internal
	 * {@link IToken} instance for every token this parser returns; it simple
	 * updates the values the token represents before returning it.
	 * <p/>
	 * This functionality is provided for performance reasons, allowing parsers
	 * to avoid creating new instances of {@link IToken} for every single token
	 * found and then leaving those instances for the garbage collector to
	 * pickup later.
	 * <p/>
	 * Not every parser will support this functionality as it might not make
	 * sense in the context of how the parser operates (e.g. a scanner).
	 * <p/>
	 * Any parser implementing this functionality should make it clear to the
	 * caller that if this flag is <code>true</code>, the values stored inside
	 * the returned {@link IToken}s are volatile and will likely change after
	 * the handling method returns.
	 * 
	 * @return <code>true</code> if this parser is updating and returning the
	 *         same mutable {@link IToken} instance to the caller for every
	 *         token parsed, otherwise <code>false</code> if the parser creates
	 *         a new {@link IToken} instance every time a token is returned.
	 */
	public boolean isReusingToken();
}