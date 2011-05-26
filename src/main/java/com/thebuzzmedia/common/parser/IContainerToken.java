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
 * Used to define a {@link IToken} that can contain other {@link IToken}s.
 * <p/>
 * Utilizing this type of token can be useful when parsing hierarchical data.
 * For example a "book" parser may create {@link IContainerToken}s for every
 * chapter found in the book, then every chapter-token would contain individual
 * {@link IToken}s for each paragraph.
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
public interface IContainerToken<TT, VT, ST> extends IToken<TT, VT, ST> {
	/**
	 * Enum used to specify the different "modes" this container can operate in
	 * with regards to child tokens being added that may or may not fall outside
	 * the container's current bounds. This setting dictates how the container
	 * will respond to such a situation.
	 */
	public enum BoundsMode {
		/**
		 * Used to specify that an {@link IContainerToken} is of fixed-bounds.
		 * Any child token being added to this container that has an
		 * <code>index</code> or <code>length</code> value that falls outside of
		 * the bounds of this container, will be rejected.
		 */
		FIXED,
		/**
		 * Used to specify that an {@link IContainerToken} should adjust its
		 * bounds (<code>index</code> and <code>length</code>) to fit any child
		 * token that is added to this container. The bounds of the parent
		 * container is dictated by the bounds of the contained tokens.
		 */
		FIT_TO_CHILD
	}

	/**
	 * Used to get the current bounds mode this container operates in when child
	 * tokens are added to it.
	 * 
	 * @return the current bounds mode this container operates in when child
	 *         tokens are added to it.
	 */
	public BoundsMode getBoundsMode();

	/**
	 * Used to get the current number of child tokens contained by this token.
	 * 
	 * @return the current number of child tokens contained by this token.
	 */
	public int getTokenCount();

	/**
	 * Used to add a child token to this container.
	 * <p/>
	 * The current {@link #getBoundsMode()} value will dictate how this
	 * container handles adding or rejecting the child token depending on if the
	 * child token's bounds violate this container's requirements.
	 * 
	 * @param token
	 *            The token to add to this container.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>token</code> is <code>null</code> or if adding the
	 *             token would violate the bounds of this container based on the
	 *             current {@link #getBoundsMode()} value.
	 */
	public void addToken(IToken<TT, VT, ST> token)
			throws IllegalArgumentException;

	/**
	 * Used to get the child token from the given index.
	 * 
	 * @param index
	 *            The index of the child token to return.
	 * 
	 * @return the child token from the given index.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>index</code> is &lt; 0 or &gt;=
	 *             {@link #getTokenCount()}.
	 */
	public IToken<TT, VT, ST> getToken(int index)
			throws IllegalArgumentException;
}