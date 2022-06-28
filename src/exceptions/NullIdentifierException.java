package exceptions;

public class NullIdentifierException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NullIdentifierException(String message){
		super(message);
	}

}
