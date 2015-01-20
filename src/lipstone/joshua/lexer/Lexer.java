package lipstone.joshua.lexer;

import lipstone.joshua.lexer.genericBase.GenericLexer;

public class Lexer extends GenericLexer<Token, Type, Rule, Descender, Lexer> {
	
	/**
	 * Basic constructor for a Lexer
	 */
	public Lexer() {
		super(Token::new, Type.EMPTY);
	}
	
	/**
	 * Basic constructor for a Lexer
	 * 
	 * @param ignoreSpace
	 *            whether to ignore spaces in an input by default
	 */
	public Lexer(boolean ignoreSpace) {
		super(Token::new, Type.EMPTY, ignoreSpace);
	}
}
