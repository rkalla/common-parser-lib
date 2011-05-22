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

public abstract class AbstractParser<IT, TT, VT, ST> implements
		IParser<IT, TT, VT, ST> {
	public static final int DEFAULT_BUFFER_REFILL_THRESHOLD = 1;

	private static final int MIN_BUFFER_SIZE = 128;

	protected boolean running;
	protected boolean inputExhausted;

	// Volatile values that are modified during parsing
	protected int index;
	protected int bufferLength;

	// Static buffer values, used for initialization of the buffer.
	protected ST buffer;
	protected int bufferSize;
	protected int bufferRefillThreshold;

	protected IT input;

	public AbstractParser() throws IllegalArgumentException {
		this(MIN_BUFFER_SIZE);
	}

	public AbstractParser(int bufferSize) throws IllegalArgumentException {
		this(bufferSize, 1);
	}

	public AbstractParser(int bufferSize, int bufferRefillThreshold)
			throws IllegalArgumentException {
		if (bufferSize < MIN_BUFFER_SIZE)
			throw new IllegalArgumentException("bufferSize must be >= "
					+ MIN_BUFFER_SIZE + " to be useful");
		if (bufferRefillThreshold < 1)
			throw new IllegalArgumentException(
					"bufferRefillThreshold must be >= 1");

		this.bufferSize = bufferSize;
		this.bufferRefillThreshold = bufferRefillThreshold;
	}

	public void stop() throws UnsupportedOperationException {
		running = false;
	}

	public void reset() {
		running = true;

		index = -1;
		bufferLength = 0;

		input = null;
	}

	public void parse(IT input, IParserCallback<IT, TT, VT, ST> callback)
			throws IllegalArgumentException, IOException, ParserException {
		if (input == null)
			throw new IllegalArgumentException("input cannot be null");
		if (callback == null)
			throw new IllegalArgumentException("callback cannot be null");

		// Reset the parser state
		reset();

		// Run until input is empty or we are manually stopped.
		while (running) {
			// Refill the buffer as needed
			if ((bufferLength - index) < bufferRefillThreshold)
				refillBuffer();

			// Invoke user-supplied parse logic
			IToken<TT, VT, ST> token = parseImpl();

			// Check for a stop-condition: if input is empty and we got no token
			if (token == null && inputExhausted)
				running = false;
			else
				callback.tokenParsed(token, this);
		}
	}

	protected int refillBuffer() throws IOException {
		// -1 indicates this op never ran.
		int bytesKept = -1;

		// Only try and refill if we haven't drained the input already.
		if (!inputExhausted) {
			int bytesRead = 0;

			// Calculate how many bytes are being kept
			bytesKept = bufferLength - index;

			// Are we keeping any bytes or not?
			if (bytesKept == 0) {
				// No kept bytes, try and refill the entire buffer.
				bytesRead = readInput(buffer, 0, bufferSize);
			} else {
				// Move all the "kept" bytes to the beginning of the buffer.
				System.arraycopy(buffer, index, buffer, 0, bytesKept);

				// Only refill the remainder of space in the buffer
				bytesRead = readInput(buffer, bytesKept, bufferSize - bytesKept);
			}

			// Check if we exhausted the input source.
			if (bytesRead == -1)
				inputExhausted = true;

			// Move the index back to the front of the buffer
			index = 0;

			// Calculate the buffer's new length
			bufferLength = bytesKept + (running ? bytesRead : 0);
		}

		// Return the number of bytes kept to the caller.
		return bytesKept;
	}

	protected abstract int readInput(ST buffer, int offset, int length)
			throws IOException;

	protected abstract IToken<TT, VT, ST> parseImpl() throws IOException,
			ParserException;
}