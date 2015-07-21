package toberumono.lexer.errors;

public class EmptyInputException extends LexerException {

	public EmptyInputException() {
		super("There is no more input in the lexer");
	}
}
