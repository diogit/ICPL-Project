package exceptions;

public class IncorrectNumberOfArgumentsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public IncorrectNumberOfArgumentsException(String message) {
		super(message);
	}

}
