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

public interface ITokenizer<T> {
	public enum DelimiterType {
		MATCH_ANY, MATCH_EXACT;
	}

	public void reset();

	public int getIndex();

	public int getLength();

	public T getSource();

	public T getDelimiters();

	public DelimiterType getDelimiterType();

	public boolean hasMoreTokens();

	public IToken<T> nextToken() throws IllegalStateException;

	public int[] nextTokenBounds() throws IllegalStateException;

	public void setSource(T source, T delimiters, DelimiterType type, int index)
			throws IllegalArgumentException;

	public void setSource(T source, T delimiters, DelimiterType type,
			int index, int length) throws IllegalArgumentException;
}