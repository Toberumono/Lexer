package lexer.errors;

public class UnrecognizedCharacterException extends LexerException {
	
	public UnrecognizedCharacterException(String input, int head) {
		super("Unknown character at " + head  + ": " + input.charAt(head) + "\nRemaining Input: " + input.substring(head));
	}
}
