package toberumono.lexer.errors;

/**
 * Thrown when the lexer is out of input but is still being queried for more tokens.
 * 
 * @author Toberumono
 */
public class EmptyInputException extends LexerException {
	
	/**
	 * Constructs an {@link EmptyInputException} with the default message.
	 */
	public EmptyInputException() {
		super("There is no more input in the lexer");
	}
}
