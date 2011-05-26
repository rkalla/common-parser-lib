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
package com.thebuzzmedia.common.parser.general;

import com.thebuzzmedia.common.parser.AbstractDelimitedTokenizer;
import com.thebuzzmedia.common.parser.AbstractReusableToken;
import com.thebuzzmedia.common.parser.IToken;
import com.thebuzzmedia.common.parser.ParseException;
import com.thebuzzmedia.common.util.ArrayUtils;

public class ByteArrayTokenizer extends
		AbstractDelimitedTokenizer<byte[], byte[], Void, byte[], byte[]> {
	private ReusableByteArrayToken reusableToken = new ReusableByteArrayToken();

	public ByteArrayTokenizer() {
		super(DEFAULT_BUFFER_CAPACITY);
	}

	public ByteArrayTokenizer(int bufferCapacity)
			throws IllegalArgumentException {
		super(bufferCapacity);
	}

	/*
	 * No constructors are specified to help encourage re-use of Tokenizer
	 * instances via setInput(...)
	 * 
	 * With no constructors present, callers are directed to learn about the
	 * setInput methods and realize that they can re-use tokenizers over and
	 * over.
	 * 
	 * Had equivalent constructors been present, it leads to the
	 * all-too-familiar and wasteful pattern of wrapping data you want to parse
	 * with a new tokenizer, using it, then throwing it away. The JDK's
	 * tokenizer implementation taught this pattern.
	 * 
	 * The goal of removing constructors is to encourage users to look closer at
	 * the API or Javadoc and understand the re-use pattern.
	 */

	@Override
	protected byte[] createBuffer(int capacity) throws IllegalArgumentException {
		if (capacity < 0)
			throw new IllegalArgumentException("capacity [" + capacity
					+ "] must be >= 0");

		return new byte[capacity];
	}

	@Override
	protected IToken<Void, byte[], byte[]> parseTokenImpl(int index, int length)
			throws ParseException {
		// Token start/end indices
		int tsIndex = index;
		int teIndex = ArrayUtils.INVALID_INDEX;
		IToken<Void, byte[], byte[]> token = null;

		// Scan for start/end based on our delim mode.
		switch (mode) {
		case MATCH_ANY:
			// Find first non-delim value.
			tsIndex = ArrayUtils.indexAfterAnyNoCheck(delimiters, buffer,
					tsIndex, length);

			// Check if we found a start before trying to find end.
			if (tsIndex != ArrayUtils.INVALID_INDEX) {
				// Update remaining length
				length -= (tsIndex - index);

				// Now find first delim value after our start.
				teIndex = ArrayUtils.indexOfAnyNoCheck(delimiters, buffer,
						tsIndex, length);
			}
			break;

		case MATCH_EXACT:
			// Find first non-delim value after exact-matching delim.
			tsIndex = ArrayUtils.indexAfterNoCheck(delimiters, buffer, tsIndex,
					length);

			// Check if we found a start before trying to find end.
			if (tsIndex != ArrayUtils.INVALID_INDEX) {
				// Update remaining length
				length -= (tsIndex - index);

				// Now find the first exact-match delim occurrence after.
				teIndex = ArrayUtils.indexOfNoCheck(delimiters, buffer,
						tsIndex, length);
			}
			break;
		}

		// Check to make sure we have valid indices marked or return null
		if (tsIndex != ArrayUtils.INVALID_INDEX
				&& teIndex != ArrayUtils.INVALID_INDEX) {
			// Check if we are reusing a token or creating a new one.
			if (reuseToken) {
				reusableToken.setValue(buffer, teIndex, (teIndex - tsIndex));
				token = reusableToken;
			} else
				token = new ReusableByteArrayToken(buffer, teIndex,
						(teIndex - tsIndex));
		}

		return token;
	}

	class ReusableByteArrayToken extends
			AbstractReusableToken<Void, byte[], byte[]> {
		public ReusableByteArrayToken() {
			// default constructor
		}

		public ReusableByteArrayToken(byte[] source, int index, int length) {
			super(source, index, length);
		}

		public byte[] getValue() {
			byte[] value = new byte[length];

			// Check if there is any data to copy.
			if (length > 0)
				System.arraycopy(source, index, value, 0, length);

			return value;
		}
	}
}