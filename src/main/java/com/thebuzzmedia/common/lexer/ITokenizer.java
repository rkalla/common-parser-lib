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

import com.thebuzzmedia.common.IToken;

public interface ITokenizer<T> {
	public void reset();

	public T getSource();

	public int getIndex();

	public int getLength();

	public boolean isReuseToken();

	public void setReuseToken(boolean reuseToken);

	public void setSource(T source, int index, int length)
			throws IllegalArgumentException;

	public IToken<T> nextToken() throws IllegalStateException;
}