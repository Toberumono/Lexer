package lipstone.joshua.lexer;

import lipstone.joshua.lexer.abstractLexer.AbstractDescender;
import lipstone.joshua.lexer.abstractLexer.DescenderAction;
import lipstone.joshua.lexer.abstractLexer.LexerAction;

/**
 * A Descender for the <tt>Lexer</tt> that uses the provided <tt>Token</tt> class
 * 
 * @author Joshua Lipstone
 */
public class Descender extends AbstractDescender<Token, Type, Lexer> {
	
	/**
	 * Constructs a new <tt>Descender</tt> with the given data
	 * 
	 * @param open
	 *            the open symbol for this <tt>Descender</tt>
	 * @param close
	 *            the close symbol for this <tt>Descender</tt>
	 * @param type
	 *            the <tt>Type</tt> for the Token returned by this <tt>Descender</tt>
	 */
	public Descender(String open, String close, Type type) {
		super(open, close, type);
	}
	
	/**
	 * Constructs a new <tt>Descender</tt> with the given data
	 * 
	 * @param open
	 *            the open symbol for this <tt>Descender</tt>
	 * @param close
	 *            the close symbol for this <tt>Descender</tt>
	 * @param openAction
	 *            the actions to perform upon seeing the open token of this <tt>Descender</tt>
	 * @param closeAction
	 *            the action to be performed on the <tt>Tokens</tt> matched within the found descent set
	 */
	public Descender(String open, String close, DescenderAction<Lexer> openAction, LexerAction<Token, Token, Lexer> closeAction) {
		super(open, close, openAction, closeAction);
	}
}
