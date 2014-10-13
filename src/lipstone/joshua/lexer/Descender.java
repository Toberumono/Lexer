package lipstone.joshua.lexer;

import lipstone.joshua.lexer.abstractLexer.AbstractDescender;
import lipstone.joshua.lexer.abstractLexer.LexerAction;

/**
 * A Descender for the <tt>Lexer</tt> that uses the provided <tt>Token</tt> class
 * 
 * @author Joshua Lipstone
 */
public class Descender extends AbstractDescender<Token, Type<?>, Lexer> {
	
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
	public Descender(String open, String close, Type<Token> type) {
		super(open, close, type);
	}
	
	/**
	 * Constructs a new <tt>Descender</tt> with the given data
	 * 
	 * @param open
	 *            the open symbol for this <tt>Descender</tt>
	 * @param close
	 *            the close symbol for this <tt>Descender</tt>
	 * @param action
	 *            the action to perform on the part of the input between the symbols matched by this <tt>Descender</tt>
	 */
	public Descender(String open, String close, LexerAction<Token, String, Lexer> action) {
		super(open, close, action);
	}
}
