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


public abstract class AbstractCallbackParser<IT, TT, VT, ST> extends
		AbstractParser<IT, TT, VT, ST> implements
		ICallbackParser<IT, TT, VT, ST> {
	public void parse(ICallback<IT, TT, VT, ST> callback)
			throws IllegalArgumentException, ParseException {
		// Loop until stopped
		while (!isStopped()) {
			// Attempt to parse a token.
			IToken<TT, VT, ST> token = parseToken();

			/*
			 * If we parsed a token, deliver it to the callback. If we didn't,
			 * parseToken has correctly updated our stop state, and on our next
			 * cycle around we will bail.
			 */
			if (token != null)
				callback.tokenParsed(token, this);
		}
	}
}