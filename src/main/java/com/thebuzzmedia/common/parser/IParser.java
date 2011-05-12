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

/**
 * 
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * 
 * @param <IT>
 *            The type of the input source processed by the parser.
 * @param <T>
 *            The type of the <code>source</code> the token is used to describe.
 */
public interface IParser<IT, T> {
	public static final String BUFFER_SIZE_PROPERTY_NAME = "tbm.common.parser.bufferSize";

	public static final int BUFFER_SIZE = Integer.getInteger(
			BUFFER_SIZE_PROPERTY_NAME, 32768);
	
	public static final int MIN_BUFFER_SIZE = 256;

	public void stop() throws UnsupportedOperationException;

	public void reset();
	
	public void parse(IT input, IParserCallback<IT, T> callback)
			throws IllegalArgumentException, IOException, ParserException;
}