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
package com.thebuzzmedia.common.lexer;

import java.io.InputStream;
import java.io.Reader;

import com.thebuzzmedia.common.token.IToken;

/**
 * 
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * 
 * @param <IT>
 *            The type of the input that this tokenizer will process (e.g.
 *            {@link InputStream}, <code>byte[]</code>, {@link Reader}, etc).
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
public interface ITokenizer<IT, TT, VT, ST> {
	public void reset();

	public IT getInput();

	public int getIndex();

	public int getLength();

	public boolean isReuseToken();

	public void setReuseToken(boolean reuseToken);

	public void setInput(IT input) throws IllegalArgumentException;

	public void setInput(IT input, int index, int length)
			throws IllegalArgumentException;

	public IToken<TT, VT, ST> nextToken() throws IllegalStateException;
}