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

public abstract class AbstractToken<T> implements IToken<T> {
	protected T source;

	protected int index;
	protected int length;
	
	public AbstractToken() {
		// default constructor
	}

	public AbstractToken(T source, int index, int length)
			throws IllegalArgumentException {
		setValues(source, index, length);
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "@" + hashCode() + "[index=" + index
				+ ", length=" + length + ", source=" + source + "]";
	}

	public T getSource() {
		return source;
	}

	public int getIndex() {
		return index;
	}

	public int getLength() {
		return length;
	}

	protected void setValues(T source, int index, int length)
			throws IllegalArgumentException {
		if (source == null)
			throw new IllegalArgumentException("source cannot be null");
		if (index < 0 || length < 0)
			throw new IllegalArgumentException("index [" + index
					+ "] and length [" + length + "] must be >= 0");

		this.source = source;
		this.index = index;
		this.length = length;
	}
}