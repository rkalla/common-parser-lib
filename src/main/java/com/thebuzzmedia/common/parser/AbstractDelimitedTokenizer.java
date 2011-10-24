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

import com.thebuzzmedia.common.io.IInput;
import com.thebuzzmedia.common.parser.ParseException.Type;

public abstract class AbstractDelimitedTokenizer<IT, DT, TT, VT, ST> extends
		AbstractTokenizer<IT, TT, VT, ST> implements
		IDelimitedTokenizer<IT, DT, TT, VT, ST> {
	protected DT delimiters;
	protected DelimiterMode mode;

	public AbstractDelimitedTokenizer() {
		super(DEFAULT_BUFFER_CAPACITY);
	}

	public AbstractDelimitedTokenizer(int bufferCapacity)
			throws IllegalArgumentException {
		super(bufferCapacity);
	}

	public void reset() {
		super.reset();

		delimiters = null;
		mode = null;
	}

	@Override
	public final void setInput(IInput<IT, ST> input)
			throws IllegalArgumentException, UnsupportedOperationException {
		throw new UnsupportedOperationException(
				"setInput(IInput) is not supported. Please use setInput(IInput, <DT> delimiters, DelimitedMode mode) instead.");
	}

	@Override
	public IToken<TT, VT, ST> nextToken() throws ParseException {
		if (delimiters == null || mode == null)
			throw new ParseException(
					Type.INCOMPLETE_INPUT,
					this,
					"The tokenizer's input has not been completely initialized. Use setInput(IInput, DT, DelimiterMode) to provide the input, delimiters and a mode for the parser.");

		return super.nextToken();
	}

	public DT getDelimiters() {
		return delimiters;
	}

	public DelimiterMode getDelimiterMode() {
		return mode;
	}

	public void setInput(IInput<IT, ST> input, DT delimiters, DelimiterMode mode)
			throws IllegalArgumentException {
		if (delimiters == null)
			throw new IllegalArgumentException("delimiters cannot be null");
		if (mode == null)
			throw new IllegalArgumentException("mode cannot be null");

		// init the input
		super.setInput(input);

		this.delimiters = delimiters;
		this.mode = mode;
	}
}