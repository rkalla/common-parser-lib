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

import com.thebuzzmedia.common.token.IToken;
import com.thebuzzmedia.common.util.ArrayUtils;

public abstract class AbstractParser<IT, TT, VT, ST> implements
		IParser<IT, TT, VT, ST> {
	public static final int DEFAULT_BUFFER_REFILL_THRESHOLD = 1;

	private static final int MIN_BUFFER_CAPACITY = 128;

	private boolean running;
	private boolean inputExhausted;

	private int index;
	private int length;

	private ST buffer;
	private int bufferCapacity;
	private int bufferRefillThreshold;

	public AbstractParser() throws IllegalArgumentException {
		this(DEFAULT_BUFFER_CAPACITY);
	}

	public AbstractParser(int bufferCapacity) throws IllegalArgumentException {
		this(bufferCapacity, 1);
	}

	public AbstractParser(int bufferCapacity, int bufferRefillThreshold)
			throws IllegalArgumentException {
		if (bufferCapacity < MIN_BUFFER_CAPACITY)
			throw new IllegalArgumentException("bufferCapacity must be >= "
					+ MIN_BUFFER_CAPACITY + " to be useful");
		if (bufferRefillThreshold < 1)
			throw new IllegalArgumentException(
					"bufferRefillThreshold must be >= 1");

		this.buffer = createBuffer(bufferCapacity);
		this.bufferCapacity = bufferCapacity;
		this.bufferRefillThreshold = bufferRefillThreshold;
	}

	public void stop() throws UnsupportedOperationException {
		running = false;
	}

	public void reset() {
		running = true;
		inputExhausted = false;

		index = ArrayUtils.INVALID_INDEX;
		length = 0;
	}

	public void parse(IT input, IParserCallback<IT, TT, VT, ST> callback)
			throws IllegalArgumentException, IOException, ParserException {
		if (input == null)
			throw new IllegalArgumentException("input cannot be null");
		if (callback == null)
			throw new IllegalArgumentException("callback cannot be null");

		// Reset the parser state
		reset();

		// Do the initial buffer fill
		length = refillBuffer(input, buffer, bufferCapacity, 0, 0);

		// Loop until stopped
		while (running) {
			// Attempt to parse a token.
			IToken<TT, VT, ST> token = parseImpl(buffer, index, length);

			// If we couldn't parse a token, time to stop.
			if (token == null)
				running = false;
			else {
				// Give the token to the callback.
				callback.tokenParsed(token, this);

				// Adjust the parser index to the next pos after token.
				index = (token.getIndex() + token.getLength());
			}

			// Refill the buffer if needed and input isn't used up.
			if (!inputExhausted && ((length - index) < bufferRefillThreshold)) {
				length = refillBuffer(input, buffer, bufferCapacity, index,
						length);

				// Reset index to point at the front of the buffer
				index = 0;

				// If our buffer is empty, it is time to stop.
				if (length <= 0)
					running = false;
			}
		}
	}

	protected int refillBuffer(IT input, ST buffer, int bufferCapacity,
			int keepFromIndex, int bufferLength) throws IOException {
		int bytesRead = 0;

		// Calculate how many bytes are being kept
		int bytesKept = bufferLength - keepFromIndex;

		// If needed, move "kept" data to the front of the buffer.
		if (bytesKept > 0)
			System.arraycopy(buffer, keepFromIndex, buffer, 0, bytesKept);

		// Read data into the buffer from our input
		bytesRead = readFromInput(input, buffer, bytesKept, bufferCapacity
				- bytesKept);

		// Check if we exhausted the input source
		if (bytesRead == -1)
			inputExhausted = true;

		// Return the length of data in the buffer now, -1 if nothing.
		return (bytesRead + bytesKept);
	}

	protected abstract ST createBuffer(final int bufferCapacity)
			throws IllegalArgumentException;

	protected abstract int readFromInput(final IT input, final ST buffer,
			final int offset, final int length) throws IOException;

	protected abstract IToken<TT, VT, ST> parseImpl(final ST buffer,
			final int index, final int length) throws IOException,
			ParserException;
}