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

public abstract class AbstractTokenizer<IT, TT, VT, ST> implements
		ITokenizer<IT, TT, VT, ST> {
	protected boolean reuseToken;

	protected int index;
	protected int length;

	protected IT input;

	public void reset() {
		index = ArrayUtils.INVALID_INDEX;
		length = 0;

		input = null;
	}

	public int getIndex() {
		return index;
	}

	public int getLength() {
		return length;
	}

	public IT getInput() {
		return input;
	}

	public boolean isReuseToken() {
		return reuseToken;
	}

	public void setReuseToken(boolean reuseToken) {
		this.reuseToken = reuseToken;
	}
}