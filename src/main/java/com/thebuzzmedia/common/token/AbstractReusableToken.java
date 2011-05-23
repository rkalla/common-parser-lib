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

public abstract class AbstractReusableToken<TT, VT, ST> extends
		AbstractToken<TT, VT, ST> {
	public void setValue(ST source, int index, int length)
			throws IllegalArgumentException {
		setValue(null, source, index, length);
	}

	protected void setValue(TT type, ST source, int index, int length)
			throws IllegalArgumentException {
		if (source == null)
			throw new IllegalArgumentException("source cannot be null");
		if (index < 0 || length < 0)
			throw new IllegalArgumentException("index [" + index
					+ "] and length [" + length + "] must be >= 0");

		this.type = type;
		this.source = source;
		this.index = index;
		this.length = length;
	}
}