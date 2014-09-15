package lexer;

import lexer.abstractLexer.AbstractDescender;

/**
 * A Descender for the <tt>Lexer</tt> that uses the provided <tt>Token</tt> class
 * 
 * @author Joshua Lipstone
 */
public final class Descender extends AbstractDescender<Token, Type<Token>, Lexer> {
	
	public Descender(String open, String close, Type<Token> type) {
		super(open, close, type);
	}
}
