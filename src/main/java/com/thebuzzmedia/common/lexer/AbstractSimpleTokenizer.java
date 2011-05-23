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

public abstract class AbstractSimpleTokenizer<IT, TT, VT> extends
		AbstractTokenizer<IT, TT, VT, IT> {
	protected boolean moreTokens;

	protected int tsIndex;
	protected int teIndex;

	public void reset() {
		super.reset();

		moreTokens = false;

		tsIndex = ArrayUtils.INVALID_INDEX;
		teIndex = ArrayUtils.INVALID_INDEX;
	}

	public IToken<TT, VT, IT> nextToken() throws IllegalStateException {
		if (input == null)
			throw new IllegalStateException(
					"Tokenizer has not been initialized with any input. setInput(...) must be called to prepare this tokenizer for work.");

		IToken<TT, VT, IT> token = null;

		// Skip processing if we already exhausted the input.
		if (moreTokens) {
			// Mark the bounds of the next token found.
			updateTokenBounds();

			// Ensure that we didn't just exhaust the data source.
			if (moreTokens)
				token = createToken(input, tsIndex, (teIndex - tsIndex));
		}

		// Either return a valid token or null if there was none
		return token;
	}

	protected abstract void updateTokenBounds();

	protected abstract IToken<TT, VT, IT> createToken(IT source, int index,
			int length) throws IllegalArgumentException;
}