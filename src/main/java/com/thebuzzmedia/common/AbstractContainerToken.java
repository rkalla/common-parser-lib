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
package com.thebuzzmedia.common;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainerToken<T> extends AbstractToken<T>
		implements IContainerToken<T> {
	protected List<IToken<T>> tokenList;

	public AbstractContainerToken() {
		// default constructor
	}

	public AbstractContainerToken(T source, int index, int length)
			throws IllegalArgumentException {
		super(source, index, length);
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "@" + hashCode() + "[index=" + index
				+ ", length=" + length + ", source=" + source + ", tokenCount="
				+ getTokenCount() + ", tokenList=" + tokenList + "]";
	}

	public int getTokenCount() {
		return (tokenList == null || tokenList.isEmpty() ? 0 : tokenList.size());
	}

	public void addToken(IToken<T> token) throws IllegalArgumentException {
		if (token == null)
			throw new IllegalArgumentException("token cannot be null");

		if (tokenList == null)
			tokenList = new ArrayList<IToken<T>>(3);

		tokenList.add(token);
	}

	public IToken<T> getToken(int index) {
		if (index < 0 || tokenList == null || index >= tokenList.size())
			throw new IllegalArgumentException("index [" + index
					+ "] must be >= 0 and < getTokenCount() ["
					+ getTokenCount() + "]");

		return tokenList.get(index);
	}
}