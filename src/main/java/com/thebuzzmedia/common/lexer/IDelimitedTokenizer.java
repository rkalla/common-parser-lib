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

public interface IDelimitedTokenizer<T, DT> extends ITokenizer<T> {
	public enum DelimiterMode {
		MATCH_ANY, MATCH_EXACT;
	}

	public DT getDelimiters();

	public DelimiterMode getDelimiterMode();

	public void setSource(T source, int index, int length, DT delimiters,
			DelimiterMode mode) throws IllegalArgumentException;
}