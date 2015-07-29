package toberumono.lexer.errors;

/**
 * Thrown when an unbalanced descender is encountered. (That is to say either an open or close descender token without a
 * counterpart.
 * 
 * @author Toberumono
 */
public class UnbalancedDescenderException extends LexerException {
	
	/**
	 * Constructs an {@link UnbalancedDescenderException} for a descender in the given input at the given index.
	 * 
	 * @param input
	 *            the input in which the unbalanced descender was found
	 * @param index
	 *            the index at which it was found
	 */
	public UnbalancedDescenderException(String input, int index) {
		super("Unbalanced descender at " + index + " in " + input);
	}
}
