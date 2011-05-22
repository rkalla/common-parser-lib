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

public abstract class AbstractDelimitedTokenizer<IT, TT, VT, DT> extends
		AbstractTokenizer<IT, TT, VT> implements
		IDelimitedTokenizer<IT, TT, VT, IT, DT> {
	protected DT delimiters;
	protected DelimiterMode mode;

	public void reset() {
		super.reset();

		delimiters = null;
		mode = null;
	}

	public DT getDelimiters() {
		return delimiters;
	}

	public DelimiterMode getDelimiterMode() {
		return mode;
	}

	public final void setInput(IT input) throws IllegalArgumentException {
		throw new UnsupportedOperationException(
				"setInput(<IT>) is unsupported for IDelimitedTokenizer instances. Please use the setInput(<IT>, <DT>, DelimiterMode) method instead.");
	}

	public final void setInput(IT input, int index, int length)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException(
				"setInput(<IT>, int, int) is unsupported for IDelimitedTokenizer instances. Please use the setInput(<SI>, int, int, <DT>, DelimiterMode) method instead.");
	}
}