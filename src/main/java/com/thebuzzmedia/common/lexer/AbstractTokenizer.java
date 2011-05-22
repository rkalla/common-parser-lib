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

import com.thebuzzmedia.common.token.IToken;
import com.thebuzzmedia.common.util.ArrayUtils;

public abstract class AbstractTokenizer<IT, TT, VT> implements
		ITokenizer<IT, TT, VT, IT> {
	protected boolean moreTokens;
	protected boolean reuseToken;

	protected int index;
	protected int length;
	protected int endIndex;

	protected int tsIndex;
	protected int teIndex;

	protected IT input;

	public void reset() {
		moreTokens = false;
		reuseToken = false;

		index = ArrayUtils.INVALID_INDEX;
		length = 0;
		endIndex = ArrayUtils.INVALID_INDEX;

		tsIndex = ArrayUtils.INVALID_INDEX;
		teIndex = ArrayUtils.INVALID_INDEX;

		input = null;
	}

	public int getIndex() {
		return index;
	}

	public int getLength() {
		return length;
	}

	public IT getInput() {
		return input;
	}

	public boolean isReuseToken() {
		return reuseToken;
	}

	public void setReuseToken(boolean reuseToken) {
		this.reuseToken = reuseToken;
	}

	public IToken<TT, VT, IT> nextToken() throws IllegalStateException {
		if (input == null)
			throw new IllegalStateException(
					"Tokenizer has not been initialized with any input. setInput(...) must be called to prepare this tokenizer for work.");

		IToken<TT, VT, IT> token = null;

		// Skip processing if we already exhausted the input.
		if (moreTokens) {
			// Mark the bounds of the next token found.
			nextTokenBounds();

			// Ensure that we didn't just exhaust the data source.
			if (moreTokens)
				token = createToken(input, tsIndex, (teIndex - tsIndex));
		}

		// Either return a valid token or null if there was none
		return token;
	}

	protected abstract void nextTokenBounds();

	protected abstract IToken<TT, VT, IT> createToken(IT source, int index,
			int length) throws IllegalArgumentException;
}