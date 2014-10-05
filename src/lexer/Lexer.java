package lexer;

import lexer.abstractLexer.AbstractLexer;

public class Lexer extends AbstractLexer<Token, Type<?>, Rule<?>, Descender, Lexer> {
	
	/**
	 * Basic constructor for a Lexer
	 */
	public Lexer() {
		super(Token::new);
	}
	
	/**
	 * Basic constructor for a lexer
	 * 
	 * @param ignoreSpace
	 *            whether to ignore spaces in an input by default
	 */
	public Lexer(boolean ignoreSpace) {
		super(Token::new, ignoreSpace);
	}
}
