package lexer;

import lexer.errors.LexerException;

//Descender class for the lexer
public final class Descender {
	final String open, close;
	private final Type<Token> type;
	private final Action<Token> action;
	
	public Descender(String open, String close, Type<Token> type, Action<Token> action) {
		this.open = open;
		this.close = close;
		this.type = new Type<Token>(type, open, close);
		this.action = action;
	}
	
	/**
	 * Apply the <tt>Action</tt> associated with this <tt>Descender</tt>
	 * 
	 * @param match
	 * @param lexer
	 * @return the resulting value for a representative <tt>Token</tt>
	 * @throws LexerException
	 */
	final Token apply(String match, Lexer lexer) throws LexerException {
		return action == null ? lexer.lex(match) : action.action(match, lexer);
	}
	
	public final Type<Token> getType() {
		return type;
	}
}