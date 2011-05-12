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

public class CharArrayTokenizer extends AbstractDelimitedTokenizer<char[], char[]> {
	private ReusableCharArrayToken reusableToken = new ReusableCharArrayToken();

	/*
	 * No constructors are specified to help encourage re-use of Tokenizer
	 * instances. With no constructors present, callers are forced to learn
	 * about the setSource methods and then ask the question
	 * "Oh, can I just re-use this by setting new source data later?"
	 * 
	 * Had equivalent constructors been present, it leads to the
	 * all-too-familiar and wasteful pattern of wrapping data you want to parse
	 * with a new tokenizer instance; much like the JDK's tokenizer
	 * implementations taught us.
	 * 
	 * My hope is that the discomfort of not having the constructors encourages
	 * someone to think twice about reuse (intuitive) or at least check
	 * Javadocs/Source to see why there isn't one and then understand setSource
	 * is meant to be used over and over.
	 */
	public void setSource(char[] source, int index, int length,
			char[] delimiters, DelimiterMode mode)
			throws IllegalArgumentException {
		if (source == null || delimiters == null || mode == null)
			throw new IllegalArgumentException(
					"source, delimiters and mode cannot be null.");
		if (index < 0 || length <= 0 || (index + length) > source.length)
			throw new IllegalArgumentException("index [" + index
					+ "] must be >= 0, length [" + length
					+ "] must be > 0 and (index + length) [" + (index + length)
					+ "] must be <= source.length [" + source.length + "]");
		if (mode == DelimiterMode.MATCH_EXACT
				&& source.length < delimiters.length)
			throw new IllegalArgumentException(
					"mode specifies DelimiterMode.MATCH_EXACT, but source.length ["
							+ source.length
							+ "] is < delimiters.length ["
							+ delimiters.length
							+ "]. source must contain at least enough values to match against delimiters when using MATCH_EXACT.");

		// Reset the tokenizer's state.
		reset();

		// Set all param values.
		this.moreTokens = true;

		this.index = index;
		this.length = length;
		this.endIndex = (index + length);

		this.source = source;
		this.delimiters = delimiters;
		this.mode = mode;
	}

	protected void nextTokenBounds() {
		// On first call, start at index, otherwise start at teIndex.
		tsIndex = (teIndex == ArrayUtils.INVALID_INDEX ? index : teIndex);

		/*
		 * Scan for start/end indices using different methods based on the
		 * delimiter matching mode.
		 */
		switch (mode) {
		case MATCH_ANY:
			// Scan forward (match any) to first non-delimiter value.
			tsIndex = ArrayUtils.indexAfterAnyNoCheck(delimiters, source,
					tsIndex, length - tsIndex + index);

			// Scan from tsIndex to first (any) delimiter value.
			teIndex = ArrayUtils.indexOfAnyNoCheck(delimiters, source, tsIndex,
					length - tsIndex + index);
			break;

		case MATCH_EXACT:
			// Scan forward (match all) to first non-delimiter value.
			tsIndex = ArrayUtils.indexAfterNoCheck(delimiters, source, tsIndex,
					length - tsIndex + index);

			// Scan from tsIndex to first (match all) delimiter values.
			teIndex = ArrayUtils.indexOfNoCheck(delimiters, source, tsIndex,
					length - tsIndex + index);
			break;
		}

		// Check if we have exhausted our data source
		if (teIndex == ArrayUtils.INVALID_INDEX || tsIndex >= endIndex
				|| teIndex >= endIndex)
			moreTokens = false;
	}

	@Override
	protected IToken<char[]> createToken(char[] source, int index, int length)
			throws IllegalArgumentException {
		IToken<char[]> token;

		if (reuseToken) {
			reusableToken.setValues(source, index, length);
			token = reusableToken;
		} else
			token = new CharArrayToken(source, index, length);

		return token;
	}

	class ReusableCharArrayToken extends CharArrayToken {
		public void setValues(char[] source, int index, int length)
				throws IllegalArgumentException {
			super.setValues(source, index, length);
		}
	}
}