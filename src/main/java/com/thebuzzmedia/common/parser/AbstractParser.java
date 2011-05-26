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

import com.thebuzzmedia.common.io.IInput;
import com.thebuzzmedia.common.parser.ParseException.Type;
import com.thebuzzmedia.common.util.ArrayUtils;

public abstract class AbstractParser<IT, TT, VT, ST> implements
		IParser<IT, TT, VT, ST> {
	/**
	 * Flag used to keep track of the parser's state to see if it can or should
	 * continue parsing.
	 * <p/>
	 * In this base implementation this flag is set to <code>false</code> when
	 * new input is initialized via {@link #setInput(IInput)} and set to
	 * <code>true</code> when a read operation against the the underlying
	 * <code>input</code> returns <code>0</code> <strong>AND</code>
	 * <code>bIndex &gt;= bEndIndex</code>; indicating that both the
	 * <code>input</code> is empty and our read buffer has been read completely.
	 * <p/>
	 * This flag is also set to <code>true</code> if two attempts are made to
	 * parse a token from the buffer and fails. After the first attempt, the
	 * parser will refill the buffer and attempt the parse 1 more time. If that
	 * attempt fails, this flag is flipped and the parser is considered stopped
	 * because it is incapable of correctly parsing a token from whatever
	 * content is left.
	 */
	protected boolean stopped;

	/**
	 * Flag used to indicate if the parse should re-use the same, mutable token
	 * instance when returning single results to the caller. Not all parser
	 * types support this, but it is defined in the base interface so common
	 * support is implemented here.
	 */
	protected boolean reuseToken;

	/**
	 * The current position in the <code>buffer</code> where the parser will
	 * likely begin it's next parse operation at.
	 * <p/>
	 * Of course the use of this variable depends heavily on what the subclass
	 * does with it, but it is intended to be a marker in <code>buffer</code> of
	 * the next piece of data we have not looked at yet and where the next parse
	 * operation will most likely begin from.
	 */
	protected int bIndex;

	/**
	 * The amount of data contained in <code>buffer</code>.
	 */
	protected int bLength;

	/**
	 * Convenience variable that pre-computes the ending index (exclusive) of
	 * data contained in the <code>buffer</code>.
	 * <p/>
	 * Because so much of parsing involves looping, we frequently-enough need to
	 * know where to stop searching in <code>buffer</code> that this class
	 * pre-computes this for you.
	 */
	protected int bEndIndex;

	/**
	 * The read buffer that our underlying <code>input</code> writes data into
	 * for us to scan and look for tokens.
	 * <p/>
	 * Parsers never operate directly on the <code>input</code> and always read
	 * content to their <code>buffer</code> first before processing it.
	 */
	protected ST buffer;

	/**
	 * The source of data that the parser will process.
	 * <p/>
	 * Parsers never operate directly on the <code>input</code> and always read
	 * content to their <code>buffer</code> first before processing it.
	 */
	protected IInput<IT, ST> input;

	/**
	 * Create an instance with a read buffer of size
	 * {@link IParser#DEFAULT_BUFFER_CAPACITY}.
	 */
	public AbstractParser() {
		this(DEFAULT_BUFFER_CAPACITY);
	}

	/**
	 * Create an instance with a read buffer of the given size.
	 * 
	 * @param bufferCapacity
	 *            The size of the read buffer that will be created.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>bufferCapacity</code> is &lt; <code>0</code>.
	 */
	public AbstractParser(int bufferCapacity) throws IllegalArgumentException {
		buffer = createBuffer(bufferCapacity);
	}

	public void reset() {
		stopped = true;

		bIndex = ArrayUtils.INVALID_INDEX;
		bLength = 0;
		bEndIndex = ArrayUtils.INVALID_INDEX;

		input = null;
	}

	public void stop() {
		this.stopped = true;
	}

	public boolean isStopped() {
		return stopped;
	}

	public IInput<IT, ST> getInput() {
		return input;
	}

	public void setInput(IInput<IT, ST> input) throws IllegalArgumentException,
			ParseException {
		if (input == null)
			throw new IllegalArgumentException("input cannot be null");

		// Reset state
		reset();

		this.input = input;

		try {
			// Initial buffer fill and setup of indices/lengths.
			refillBuffer();
		} catch (IOException e) {
			throw new ParseException(
					Type.IO,
					this,
					"An exception occurred while attempting the initial buffer fill for this parser.",
					e);
		}

		// Buffer fill succeeded, set ready state.
		this.stopped = false;
	}

	public boolean isReusingToken() {
		return reuseToken;
	}

	/**
	 * Convenience method used to do the following work that is common to all
	 * parser implementations:
	 * <ol>
	 * <li>Checks if <code>input.isEmpty()</code>. If it is AND
	 * <code>bIndex &gt;= bEndIndex</code>, then updates <code>hasData</code> to
	 * <code>false</code> before returning.</li>
	 * <li>If <code>buffer</code> has remaining data in it (
	 * <code>bEndIndex - bIndex &gt; 0</code>), move the remaining data to the
	 * front of the buffer.</li>
	 * <li>Reset the value of <code>bIndex</code> to <code>0</code>.</li>
	 * <li>Fill in the buffer after the remaining data (if any) with content
	 * from <code>input</code>.</li>
	 * <li>Update the value of <code>bLength</code> to reflect the new total
	 * content length (old data length + new data length) contained in
	 * <code>buffer</code>.</li>
	 * <li>Recalculate: <code>bEndIndex = bIndex + bLength</code>.</li>
	 * <li>Return the total amount of data read into the buffer (
	 * <code>bLength - amount of data kept</code>).</li>
	 * </ol>
	 * It is expected that this method will be called from custom parse logic
	 * when deemed that the parser does not have enough data in the
	 * <code>buffer</code> to mark a complete {@link IToken}. The implementor
	 * would call this method, respond if needed to any of the adjust index or
	 * length values, and then re-attempt marking the complete token bounds one
	 * more time before failing.
	 * <p/>
	 * Because this method updates the state of <code>hasData</code>, it is safe
	 * to call this method as a mechanism for updating the <code>hasData</code>
	 * state instead of putting that logic in a subclass.
	 * 
	 * @return the amount of new data read into <code>buffer</code>. The
	 *         <code>bLength</code> value will have been updated already to
	 *         reflect the new <code>buffer</code> length.
	 * 
	 * @throws IllegalArgumentException
	 *             if there is a problem with any arguments in the underlying
	 *             call to {@link IInput#read(Object)} or
	 *             {@link IInput#read(Object, int)}.
	 * @throws IOException
	 *             if the underlying {@link IInput} has any problem reading from
	 *             its <code>source</code> during a <code>read</code> operation.
	 */
	protected int refillBuffer() throws IllegalArgumentException, IOException {
		// Check if we can even do a read operation.
		if (input.isEmpty()) {
			// If read buffer is empty too, stop this parser.
			if (bIndex >= bEndIndex)
				stop();

			return 0;
		}

		// Calculate how much data is being kept.
		int keepLength = bEndIndex - bIndex;

		// If needed, move "kept" data to the front of the buffer.
		if (keepLength > 0)
			System.arraycopy(buffer, bIndex, buffer, 0, keepLength);

		// Reset index to point back at the front of the buffer
		bIndex = 0;

		// Fill remainder of buffer beginning after the kept data (if any).
		bLength = input.read(buffer, (keepLength < 0 ? 0 : keepLength));

		// Add our keepLength to the buffer length if we kept anything
		if (keepLength > 0)
			bLength += keepLength;

		// Re-calculate the ending index (exclusive)
		bEndIndex = (bIndex + bLength);

		// Check if we are empty and need to stop.
		if (bLength < 1)
			stop();

		// Return the amount of new data read into the buffer.
		return (bLength - keepLength);
	}

	protected IToken<TT, VT, ST> parseToken() throws ParseException {
		if (input == null)
			throw new ParseException(
					Type.NO_INPUT,
					this,
					"Parser's input has not been set. Use setInput(IInput) to provide input for the parser to process.");

		IToken<TT, VT, ST> token = null;

		// Check if we can parse.
		if (!isStopped()) {
			// Attempt to parse the next token.
			token = parseTokenImpl(bIndex, bEndIndex - bIndex);

			// If we failed, we need to refill the buffer and retry.
			if (token == null) {
				try {
					// Refresh the buffer with new data.
					refillBuffer();
				} catch (IOException e) {
					throw new ParseException(
							Type.IO,
							this,
							"An exception occurred while trying to refill the parser's read buffer before the 2nd attempt at parsing the next token.",
							e);
				}

				// Try a 2nd time to parse the token
				token = parseTokenImpl(bIndex, bEndIndex - bIndex);

				// If we failed again, stop the parser. It's done.
				if (token == null)
					stop();
			}

			// Update the buffer index position if we parsed a token.
			if (token != null)
				bIndex += token.getLength();
		}

		// Return the parsed token to the caller or null if we got nothing.
		return token;
	}

	protected abstract ST createBuffer(int capacity)
			throws IllegalArgumentException;

	protected abstract IToken<TT, VT, ST> parseTokenImpl(int index, int length)
			throws ParseException;
}