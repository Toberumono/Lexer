package toberumono.lexer;

import toberumono.lexer.genericBase.GenericLexer;

/**
 * A basic implementation of {@link GenericLexer}. For most purposes, using this implementation should be more than
 * sufficient.
 * 
 * @author Toberumono
 */
public class Lexer extends GenericLexer<Token, Type, Rule, Descender, Lexer> {
	
	/**
	 * Basic constructor for a {@link Lexer}
	 * 
	 * @param ignore
	 *            A list of patterns to ignore. The {@link DefaultIgnorePatterns} enum has a few common patterns.
	 */
	public Lexer(IgnorePattern... ignore) {
		super(Token::new, Type.EMPTY, ignore);
	}
}
