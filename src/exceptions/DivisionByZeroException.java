package exceptions;

public class DivisionByZeroException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public DivisionByZeroException(String message){
		super(message);
	}

}
