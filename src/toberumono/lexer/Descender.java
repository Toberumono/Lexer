package toberumono.lexer;

import java.util.regex.Pattern;

import toberumono.lexer.genericBase.DescenderAction;
import toberumono.lexer.genericBase.GenericAction;
import toberumono.lexer.genericBase.GenericDescender;

/**
 * A Descender for the <tt>Lexer</tt> that uses the provided <tt>Token</tt> class
 * 
 * @author Toberumono
 */
public class Descender extends GenericDescender<Token, Type, Rule, Descender, Lexer> {
	
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
	public Descender(String open, String close, DescenderAction<Lexer> openAction, GenericAction<Token, Type, Rule, Descender, Lexer, Token> closeAction) {
		super(open, close, openAction, closeAction);
	}
	
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
	public Descender(Pattern open, Pattern close, Type type) {
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
	public Descender(Pattern open, Pattern close, DescenderAction<Lexer> openAction, GenericAction<Token, Type, Rule, Descender, Lexer, Token> closeAction) {
		super(open, close, openAction, closeAction);
	}
}
