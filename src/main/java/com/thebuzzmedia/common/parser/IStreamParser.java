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

import com.thebuzzmedia.common.token.IToken;

/**
 * Interface used to define an {@link IParser} that is specialized to operate on
 * {@link InputStream}s using <code>byte[]</code> buffers.
 * 
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * 
 * @param <TT>
 *            The type of the token, if necessary. If not needed, simply use a
 *            type of {@link Void}.
 */
public interface IStreamParser<TT> extends
		IParser<InputStream, TT, byte[], byte[]> {
	/**
	 * Used to initiate a parse operation on the given {@link InputStream},
	 * invoking the given {@link IParserCallback} every time an {@link IToken}
	 * is successfully generated.
	 * 
	 * @param input
	 *            The input stream providing data for this parse operation.
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
	public void parse(InputStream input,
			IParserCallback<InputStream, TT, byte[], byte[]> callback)
			throws IllegalArgumentException, IOException, ParserException;
}