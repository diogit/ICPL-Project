package exceptions;

public class IdentifierAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public IdentifierAlreadyExistsException(String message) {
		super(message);
	}

}
