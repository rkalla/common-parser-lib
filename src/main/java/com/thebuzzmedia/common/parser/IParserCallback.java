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

import com.thebuzzmedia.common.token.IToken;

/**
 * Interface used to define a callback invoked by instances of {@link IParser}
 * every time an {@link IToken} is generated from the parsed content.
 * 
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * 
 * @param <IT>
 *            The type of the input that the parser is processing (e.g.
 *            {@link InputStream}, {@link Reader}, etc).
 * @param <TT>
 *            The type of the token, if necessary. If not needed, simply use a
 *            type of {@link Void}.
 * @param <VT>
 *            The type of {@link IToken#getValue()} as extracted from the given
 *            <code>source</code> (e.g. <code>String</code>, <code>int</code>,
 *            <code>char[]</code>, etc).
 * @param <ST>
 *            The type of the <code>source</code> that the tokens will pulls
 *            their values from (e.g. <code>StringBuilder</code>,
 *            <code>InputStream</code>, <code>byte[]</code>, etc).
 */
public interface IParserCallback<IT, TT, VT, ST> {
	/**
	 * Used to provide the parsed {@link IToken} to a handler implementation.
	 * 
	 * @param token
	 *            The token that was parsed.
	 * @param parser
	 *            The parser that created the token. Having this reference can
	 *            be handy if you wish to stop parsing due to some condition
	 *            with {@link IParser#stop()}.
	 */
	public void tokenParsed(IToken<TT, VT, ST> token,
			IParser<IT, TT, VT, ST> parser);
}