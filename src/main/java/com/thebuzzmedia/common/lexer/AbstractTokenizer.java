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

import com.thebuzzmedia.common.IToken;
import com.thebuzzmedia.common.util.ArrayUtils;

public abstract class AbstractTokenizer<T> implements ITokenizer<T> {
	protected boolean moreTokens;
	protected boolean reuseToken;

	protected int index;
	protected int length;
	protected int endIndex;

	protected int tsIndex;
	protected int teIndex;

	protected T source;

	public void reset() {
		moreTokens = false;
		reuseToken = false;

		index = ArrayUtils.INVALID_INDEX;
		length = 0;
		endIndex = ArrayUtils.INVALID_INDEX;

		tsIndex = ArrayUtils.INVALID_INDEX;
		teIndex = ArrayUtils.INVALID_INDEX;

		source = null;
	}

	public int getIndex() {
		return index;
	}

	public int getLength() {
		return length;
	}

	public T getSource() {
		return source;
	}

	public boolean isReuseToken() {
		return reuseToken;
	}

	public void setReuseToken(boolean reuseToken) {
		this.reuseToken = reuseToken;
	}

	public IToken<T> nextToken() throws IllegalStateException {
		if (source == null)
			throw new IllegalStateException(
					"Tokenizer has not been initialized with a source. setSource(...) must be called to prepare this tokenizer for parsing.");

		IToken<T> token = null;

		// Skip processing if we already exhausted the data source.
		if (moreTokens) {
			// Mark the bounds of the next token found.
			nextTokenBounds();

			// Ensure that we didn't just exhaust the data source.
			if (moreTokens)
				token = createToken(source, tsIndex, (teIndex - tsIndex));
		}

		// Either return a valid token or null if there was none
		return token;
	}

	protected abstract void nextTokenBounds();

	protected abstract IToken<T> createToken(T source, int index, int length)
			throws IllegalArgumentException;
}