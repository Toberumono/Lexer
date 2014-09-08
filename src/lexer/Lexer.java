package lexer;

import lexer.abstractLexer.AbstractLexer;

public class Lexer extends AbstractLexer<Token, Type<?>, Rule<?>, Descender, Lexer> {
	
	/**
	 * Basic constructor for a Lexer
	 */
	public Lexer() {
		super();
	}

	@Override
	public Token makeNewToken() {
		return new Token();
	}
}
