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
 * Interface used to define a "callback"-style parser.
 * <p/>
 * Callback parsers are somewhat similar to {@link IScanner}s in that they are
 * intended to process the data from an input source entirely in one go. The
 * difference being that like {@link ITokenizer}s, callback parsers will deliver
 * {@link IToken}s to the provided callback as it finds them in an on-demand
 * fashion.
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
public interface ICallbackParser<IT, TT, VT, ST> extends
		IParser<IT, TT, VT, ST> {
	/**
	 * Used to initiate a parse operation on the previously set input (
	 * {@link IParser#setInput(IInput)}), invoking the given {@link ICallback}
	 * every time an {@link IToken} is generated.
	 * 
	 * @param callback
	 *            The callback that will be invoked every time an {@link IToken}
	 *            is successfully generated.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>callback</code> is <code>null</code>.
	 * @throws ParseException
	 *             if any error occurs while trying to parse the data from the
	 *             underlying {@link IInput} (e.g. I/O error, malformed content,
	 *             etc). Details of the underlying exception and reason for
	 *             failure can be retrieved from the exception instance.
	 */
	public void parse(ICallback<IT, TT, VT, ST> callback)
			throws IllegalArgumentException, ParseException;

	/**
	 * Interface used to define a callback invoked by instances of
	 * {@link ICallbackParser} every time an {@link IToken} is generated from
	 * the parsed content.
	 * 
	 * @author Riyad Kalla (software@thebuzzmedia.com)
	 * 
	 * @param <IT>
	 *            The type of the input that this parser will process (e.g.
	 *            {@link InputStream}, <code>byte[]</code>, {@link Reader},
	 *            etc).
	 * @param <TT>
	 *            The type of the token, if necessary. If not needed, simply use
	 *            a type of {@link Void}.
	 * @param <VT>
	 *            The type of the values returned by {@link IToken#getValue()}.
	 *            <p/>
	 *            This may be different from the &lt;ST&gt; type based on any
	 *            processing the token does to the <code>source</code> while
	 *            pulling the marked data from it. For example, the source might
	 *            be a {@link StringBuilder} and the value returned by the token
	 *            might be <code>char[]</code>.
	 * @param <ST>
	 *            The type of the <code>source</code> that the tokens created by
	 *            this parser reference and pull {@link IToken#getValue()} value
	 *            from.
	 *            <p/>
	 *            This may be different from the &lt;IT&gt; type based on any
	 *            internal structures (e.g. <code>StringBuilder</code>,
	 *            <code>byte[]</code>, etc) the parser utilizes while processing
	 *            the input. In this case, the <code>source</code> of the tokens
	 *            created by this parser will reference this interim data
	 *            structure as opposed to the input object itself.
	 *            <p/>
	 *            For example, a parser may take {@link InputStream}s as input,
	 *            but internally utilize a <code>byte[]</code> for a read buffer
	 *            that the generated {@link IToken} instances reference as their
	 *            <code>source</code>.
	 */
	public interface ICallback<IT, TT, VT, ST> {
		/**
		 * Used to provide the parsed {@link IToken} to a handler
		 * implementation.
		 * 
		 * @param token
		 *            The token that was generated by the parser.
		 * @param parser
		 *            The parser that created the token.
		 *            <p/>
		 *            Having this reference can be helpful if some condition is
		 *            met and the parser needs to be stopped or behavior altered
		 *            in some other implementation-specific way.
		 *            <p/>
		 *            This way, the handler has a reference to the source-parser
		 *            and can make those changes if necessary.
		 */
		public void tokenParsed(IToken<TT, VT, ST> token,
				ICallbackParser<IT, TT, VT, ST> parser);
	}
}