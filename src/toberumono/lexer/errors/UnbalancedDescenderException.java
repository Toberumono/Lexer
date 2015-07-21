package toberumono.lexer.errors;

public class UnbalancedDescenderException extends LexerException {

	public UnbalancedDescenderException(String input, int index) {
		super("Unbalanced descender at " + index + " in " + input);
	}
	
	
}
