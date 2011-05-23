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
import java.io.InputStream;

public abstract class AbstractStreamParser<TT> extends
		AbstractParser<InputStream, TT, byte[], byte[]> implements
		IStreamParser<TT> {
	public AbstractStreamParser() throws IllegalArgumentException {
		this(DEFAULT_BUFFER_CAPACITY);
	}

	public AbstractStreamParser(int bufferCapacity)
			throws IllegalArgumentException {
		this(bufferCapacity, 1);
	}

	public AbstractStreamParser(int bufferCapacity, int bufferRefillThreshold)
			throws IllegalArgumentException {
		super(bufferCapacity, bufferRefillThreshold);
	}

	@Override
	protected byte[] createBuffer(final int bufferCapacity)
			throws IllegalArgumentException {
		if (bufferCapacity < 1)
			throw new IllegalArgumentException("bufferCapacity ["
					+ bufferCapacity + "] must be >= 1");

		return new byte[bufferCapacity];
	}

	@Override
	protected int readFromInput(final InputStream input, final byte[] buffer,
			final int offset, final int length) throws IOException {
		return input.read(buffer, offset, length);
	}

	public void parse(InputStream input, IStreamParserCallback<TT> callback)
			throws IllegalArgumentException, IOException, ParserException {
		super.parse(input, callback);
	}
}