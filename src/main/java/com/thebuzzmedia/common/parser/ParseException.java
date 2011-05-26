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
 * A specialized {@link RuntimeException} thrown by all parser implementations
 * implementations of The Buzz Media's common-parser library to indicate a
 * problem occurred while parsing.
 * <p/>
 * Exceptions of this type help describe in more detail what problem the parser
 * ({@link #getSource()}) ran into while executing in the form of an
 * {@link Enum}; making it easier for handlers to respond to the result.
 * <p/>
 * The original, underlying exception can be retrieved with {@link #getCause()}
 * and a human-readable message describing the failure can be retrieved with
 * {@link #getMessage()}.
 * 
 * @author Riyad Kalla (software@thebuzzmedia.com)
 */
@SuppressWarnings("rawtypes")
public class ParseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public enum Type {
		UNSPECIFIED, NO_INPUT, INCOMPLETE_INPUT, IO, MALFORMED;
	}

	private Type type;
	private IParser source;

	public ParseException(Type type, IParser source, String message) {
		this(type, source, message, null);
	}

	public ParseException(Type type, IParser source, String message,
			Throwable cause) {
		super(message, cause);

		this.type = type;
		this.source = source;
	}

	@Override
	public String toString() {
		String message = getMessage();
		Throwable cause = getCause();

		// Avoid NPE's from toString'ing nulls with ?: conditionals.
		return this.getClass().getName() + "@" + hashCode() + "[type="
				+ (type == null ? "" : type) + ", source="
				+ (source == null ? "" : source) + ", message="
				+ (message == null ? "" : message) + ", cause="
				+ (cause == null ? "" : cause) + "]";
	}

	public Type getType() {
		return type;
	}

	public Object getSource() {
		return source;
	}
}