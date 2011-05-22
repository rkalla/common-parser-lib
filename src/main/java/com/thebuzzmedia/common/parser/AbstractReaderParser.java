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
import java.io.Reader;

public abstract class AbstractReaderParser<TT> extends
		AbstractParser<Reader, TT, char[], char[]> implements IReaderParser<TT> {
	public AbstractReaderParser() throws IllegalArgumentException {
		this(DEFAULT_BUFFER_SIZE);
	}

	public AbstractReaderParser(int bufferSize) throws IllegalArgumentException {
		this(bufferSize, 1);
	}

	public AbstractReaderParser(int bufferSize, int bufferRefillThreshold)
			throws IllegalArgumentException {
		super(bufferSize, bufferRefillThreshold);
		buffer = new char[this.bufferSize];
	}

	protected int readInput(char[] buffer, int offset, int length)
			throws IOException {
		return input.read(buffer, offset, length);
	}
}