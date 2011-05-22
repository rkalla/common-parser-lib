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

	private static final int MIN_BUFFER_CAPACITY = 128;

	private boolean running;
	private boolean inputExhausted;

	private int index;
	private int bufferLength;

	private ST buffer;
	private int bufferCapacity;
	private int bufferRefillThreshold;

	protected IT input;

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
			if ((bufferLength - index) < bufferRefillThreshold) {
				bufferLength = refillBuffer(buffer, bufferCapacity, index,
						bufferLength);

				// Reset index to point at the front of the buffer
				index = 0;

				// Check for stop-condition
				if (bufferLength == 0)
					running = false;
			}

			// Invoke user-supplied parse logic
			IToken<TT, VT, ST> token = parseImpl(buffer, index, bufferLength);

			// Check for stop-condition
			if (token == null && bufferLength == 0)
				running = false;
			else {
				// Adjust the parser index
				index = (token.getIndex() + token.getLength());

				// Send to callback
				callback.tokenParsed(token, this);
			}
		}
	}

	protected int refillBuffer(ST buffer, int bufferCapacity,
			int keepFromIndex, int bufferLength) throws IOException {
		// Calculate how many bytes are being kept
		int bytesKept = bufferLength - keepFromIndex;
		int bytesRead = 0;

		// If necessary, move kept bytes to the front of the buffer.
		if (bytesKept > 0)
			System.arraycopy(buffer, keepFromIndex, buffer, 0, bytesKept);

		// If we haven't exhausted the input source, read more data in.
		if (!inputExhausted) {
			bytesRead = readInput(buffer, bytesKept, bufferCapacity - bytesKept);

			// Check if we just exhausted the input source
			if (bytesRead == -1) {
				inputExhausted = true;

				// Reset to 0 to ease our length calculation below
				bytesRead = 0;
			}
		}

		// Return the buffer's new length
		return (bytesKept + bytesRead);
	}

	protected abstract ST createBuffer(int bufferCapacity)
			throws IllegalArgumentException;

	protected abstract int readInput(ST buffer, int offset, int length)
			throws IOException;

	protected abstract IToken<TT, VT, ST> parseImpl(ST buffer, int index,
			int length) throws IOException, ParserException;
}