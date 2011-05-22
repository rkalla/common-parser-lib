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

import java.io.Reader;

import com.thebuzzmedia.common.token.IToken;

/**
 * Interface used to define a callback invoked by instances of
 * {@link IStreamParser} every time an {@link IToken} is generated from the
 * parsed content.
 * 
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * 
 * @param <TT>
 *            The type of the token, if necessary. If not needed, simply use a
 *            type of {@link Void}.
 */
public interface IStreamParserCallback<TT> extends
		IParserCallback<Reader, TT, char[], char[]> {
	/**
	 * Used to provide the parsed {@link IToken} to a handler implementation.
	 * 
	 * @param token
	 *            The token that was parsed.
	 * @param parser
	 *            The parser that created the token. Having this reference can
	 *            be handy if you wish to stop parsing due to some condition
	 *            with {@link IStreamParser#stop()}.
	 */
	public void tokenParsed(IToken<TT, byte[], byte[]> token,
			IStreamParser<TT> parser);
}