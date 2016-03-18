package toberumono.lexer.errors;

import toberumono.lexer.base.Lexer;
import toberumono.lexer.base.LexerState;

/**
 * Thrown when a {@link Lexer} is out of input but is still being queried for more tokens.
 * 
 * @author Toberumono
 */
public class EmptyInputException extends LexerException {
	
	/**
	 * Constructs an {@link EmptyInputException} with the default message.
	 * 
	 * @param state
	 *            the {@link LexerState} of the {@link Lexer} immediately prior to the exception being raised
	 */
	public EmptyInputException(LexerState<?, ?, ?, ?, ?> state) {
		super("The end of the input has been reached.", state);
	}
}
