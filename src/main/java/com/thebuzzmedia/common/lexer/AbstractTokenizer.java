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

import com.thebuzzmedia.common.util.ArrayUtils;

public abstract class AbstractTokenizer<T> implements ITokenizer<T> {
	protected boolean moreTokens;

	protected int index;
	protected int length;
	protected int endIndex;

	protected int tsIndex;
	protected int teIndex;
	protected int[] tBounds;

	protected T source;
	protected T delimiters;
	protected DelimiterType type;

	public AbstractTokenizer() {
		tBounds = new int[2];
	}

	public void reset() {
		moreTokens = false;

		index = ArrayUtils.INVALID_INDEX;
		length = 0;
		endIndex = ArrayUtils.INVALID_INDEX;

		tsIndex = ArrayUtils.INVALID_INDEX;
		teIndex = ArrayUtils.INVALID_INDEX;
		tBounds[0] = ArrayUtils.INVALID_INDEX;
		tBounds[1] = ArrayUtils.INVALID_INDEX;

		source = null;
		delimiters = null;
		type = null;
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

	public T getDelimiters() {
		return delimiters;
	}

	public DelimiterType getDelimiterType() {
		return type;
	}

	public boolean hasMoreTokens() {
		return moreTokens;
	}
}