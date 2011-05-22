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
package com.thebuzzmedia.common.token;

import java.util.ArrayList;
import java.util.List;

import com.thebuzzmedia.common.util.ArrayUtils;

public abstract class AbstractContainerToken<TT, VT, ST> extends
		AbstractToken<TT, VT, ST> implements IContainerToken<TT, VT, ST> {
	protected BoundsMode mode = BoundsMode.FIT_TO_CHILD;
	protected List<IToken<TT, VT, ST>> tokenList;

	public AbstractContainerToken() {
		// default constructor
	}

	public AbstractContainerToken(ST source, int index, int length)
			throws IllegalArgumentException {
		this(null, source, index, length, null);
	}

	public AbstractContainerToken(TT type, ST source, int index, int length,
			BoundsMode mode) throws IllegalArgumentException {
		setValue(type, source, index, length,
				(mode == null ? BoundsMode.FIT_TO_CHILD : mode));
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "@" + hashCode() + "[type="
				+ (type == null ? "" : type) + ", index=" + index + ", length="
				+ length + ", source=" + (source == null ? "" : source)
				+ ", boundsMode=" + mode + ", tokenCount=" + getTokenCount()
				+ ", tokenList=" + (tokenList == null ? "" : tokenList) + "]";
	}

	public BoundsMode getBoundsMode() {
		return mode;
	}

	public int getTokenCount() {
		return (tokenList == null ? 0 : tokenList.size());
	}

	public void addToken(IToken<TT, VT, ST> token)
			throws IllegalArgumentException {
		if (token == null)
			throw new IllegalArgumentException("token cannot be null");

		int tIndex = token.getIndex();
		int tLength = tIndex + token.getLength();
		int endIndex = (index + length);

		// Sanity check
		if (tIndex < 0)
			throw new IllegalArgumentException("token.getIndex() [" + tIndex
					+ "] must be >= 0");

		switch (mode) {
		case FIT_TO_CHILD:
			// Adjust index if necessary
			if ((index == ArrayUtils.INVALID_INDEX) || (tIndex < index))
				index = tIndex;
			// Adjust length if necessary
			if (tLength > length)
				length = tLength;
			break;

		case FIXED:
			// Check index
			if ((tIndex < index) || (tIndex >= endIndex))
				throw new IllegalArgumentException("token.getIndex() ["
						+ tIndex + "] must be >= getIndex() [" + index
						+ "] and < (getIndex() + getLength()) [" + (endIndex)
						+ "]");
			// Check length
			if (tLength > endIndex)
				throw new IllegalArgumentException(
						"(token.getIndex() + token.getLength()) [" + tLength
								+ "] must be <= (getIndex() + getLength()) ["
								+ (endIndex) + "]");
			break;
		}

		// Avoid creating two disjointed lists, block on this.
		synchronized (this) {
			// init the list if this is our first add
			if (tokenList == null)
				tokenList = new ArrayList<IToken<TT, VT, ST>>(2);
		}

		tokenList.add(token);
	}

	public IToken<TT, VT, ST> getToken(int index) {
		if (index < 0 || tokenList == null || index >= tokenList.size())
			throw new IllegalArgumentException("index [" + index
					+ "] must be >= 0 and < getTokenCount() ["
					+ getTokenCount() + "]");

		return tokenList.get(index);
	}

	protected void setValue(TT type, ST source, int index, int length,
			BoundsMode mode) throws IllegalArgumentException {
		if (source == null)
			throw new IllegalArgumentException("source cannot be null");
		if (index < 0 || length < 0)
			throw new IllegalArgumentException("index [" + index
					+ "] and length [" + length + "] must be >= 0");
		if (mode == null)
			throw new IllegalArgumentException("mode cannot be null");

		super.setValue(type, source, index, length);
		this.mode = mode;
	}
}