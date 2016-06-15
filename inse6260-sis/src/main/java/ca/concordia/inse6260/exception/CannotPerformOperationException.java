package ca.concordia.inse6260.exception;

public class CannotPerformOperationException extends RuntimeException {

	private static final long serialVersionUID = 1381302986984943495L;

	public CannotPerformOperationException() {
		super();
	}

	public CannotPerformOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotPerformOperationException(String message) {
		super(message);
	}

	
}
