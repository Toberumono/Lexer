package toberumono.lexer.errors;

/**
 * Thrown when the lexer encounters and unrecognized character (aka a character that does not match any of the rules or
 * descenders the lexer has been given.
 * 
 * @author Toberumono
 */
public class UnrecognizedCharacterException extends LexerException {
	
	/**
	 * Constructs an {@link UnrecognizedCharacterException} for an unrecognized character in the given input at the given
	 * index.
	 * 
	 * @param input
	 *            the input in which the unrecognized character was encountered
	 * @param index
	 *            the index at which it was encountered
	 */
	public UnrecognizedCharacterException(String input, int index) {
		super("Unknown character at " + index + ": " + input.charAt(index) + "\nRemaining Input: " + input.substring(index));
	}
}
