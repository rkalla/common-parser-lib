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

/**
 * Used to define the fundamental marking mechanism used by all parsing
 * mechanisms provided in this library.
 * <p/>
 * Tokens are a standard way to encapsulate information about a range of data,
 * from a given <code>source</code> and provide a convenient mechanism by which
 * the value represented by the token can be retrieved.
 * <p/>
 * It is common for tokens to carry an additional bit of information about
 * themselves in the form of a "type", especially in more complex parsing
 * implementations. If this functionality is not needed, simply use {@link Void}
 * for the &ltTT&gt; type.
 * 
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * 
 * @param <TT>
 *            The type of the token, if necessary. If not needed, simply use a
 *            type of {@link Void}.
 *            <p/>
 *            It is common in more complex parsing implementations to have
 *            strongly-typed tokens that indicate the type of data they wrap
 *            (e.g. specific language constructs). This functionality is built
 *            directly into {@link IToken} so it is available if needed.
 * @param <VT>
 *            The type of the <code>value</code>, extracted from the
 *            <code>source</code>, that this token returns (e.g.
 *            <code>String</code>, <code>int</code>, <code>char[]</code>, etc).
 * @param <ST>
 *            The type of the <code>source</code> that this token pulls its
 *            value from (e.g. <code>StringBuilder</code>,
 *            <code>InputStream</code>, <code>byte[]</code>, etc).
 */
public interface IToken<TT, VT, ST> {
	/**
	 * Used to get the type of this token.
	 * <p/>
	 * Typing of the tokens isn't always necessary, but utilizing types like
	 * {@link Enum}-based values can make processing {@link IToken} instances in
	 * switch-blocks much more convenient.
	 * <p/>
	 * If a token type is not needed, {@link Void} can be specified for the
	 * &ltTT&gt; type and any implementations of this method can simply return
	 * <code>null</code>.
	 * 
	 * @return the type of this token.
	 */
	public TT getType();

	/**
	 * Used to get the value represented by this token and its bounds from the
	 * wrapped <code>source</code>.
	 * 
	 * @return the value represented by this token and its bounds from the
	 *         wrapped <code>source</code>.
	 */
	public VT getValue();

	/**
	 * Used to get the source that this token's <code>value</code> will come
	 * from.
	 * 
	 * @return the source that this token's <code>value</code> will come from.
	 */
	public ST getSource();

	/**
	 * Used to get the index, or offset, within the <code>source</code> that the
	 * value marked by this token begins.
	 * 
	 * @return the index, or offset, within the <code>source</code> that the
	 *         value marked by this token begins.
	 */
	public int getIndex();

	/**
	 * Used to get the length of content marked within the <code>source</code>
	 * (starting at {@link #getIndex()}), that represents this token's
	 * <code>value</code>.
	 * 
	 * @return the length of content marked within the <code>source</code>
	 *         (starting at {@link #getIndex()}), that represents this token's
	 *         <code>value</code>.
	 */
	public int getLength();
}