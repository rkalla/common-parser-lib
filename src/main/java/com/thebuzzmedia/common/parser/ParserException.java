package com.thebuzzmedia.common.parser;

public class ParserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ParserException(String reason) {
		super(reason);
	}

	public ParserException(String reason, Throwable originalException) {
		super(reason, originalException);
	}
}