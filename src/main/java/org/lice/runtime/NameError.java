package org.lice.runtime;

public class NameError extends RuntimeException {
	public NameError() {
	}

	public NameError(String message) {
		super(message);
	}

	public NameError(String message, Throwable cause) {
		super(message, cause);
	}

	public NameError(Throwable cause) {
		super(cause);
	}

	public NameError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}