package toberumono.lexer;

import java.util.regex.Pattern;

import toberumono.lexer.genericBase.DescenderAction;
import toberumono.lexer.genericBase.GenericAction;
import toberumono.lexer.genericBase.GenericDescender;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A Descender for the <tt>Lexer</tt> that uses the provided <tt>ConsCell</tt> class
 * 
 * @author Toberumono
 */
public class Descender extends GenericDescender<ConsCell, ConsType, Rule, Descender, Lexer> {
	
	/**
	 * Constructs a new <tt>Descender</tt> with the given data
	 * 
	 * @param open
	 *            the open symbol for this <tt>Descender</tt>
	 * @param close
	 *            the close symbol for this <tt>Descender</tt>
	 * @param type
	 *            the <tt>Type</tt> for the ConsCell returned by this <tt>Descender</tt>
	 */
	public Descender(String open, String close, ConsType type) {
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
	 *            the action to be performed on the <tt>ConsCells</tt> matched within the found descent set
	 */
	public Descender(String open, String close, DescenderAction<Lexer> openAction, GenericAction<ConsCell, ConsType, Rule, Descender, Lexer, ConsCell> closeAction) {
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
	 *            the <tt>Type</tt> for the ConsCell returned by this <tt>Descender</tt>
	 */
	public Descender(Pattern open, Pattern close, ConsType type) {
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
	 *            the action to be performed on the <tt>ConsCells</tt> matched within the found descent set
	 */
	public Descender(Pattern open, Pattern close, DescenderAction<Lexer> openAction, GenericAction<ConsCell, ConsType, Rule, Descender, Lexer, ConsCell> closeAction) {
		super(open, close, openAction, closeAction);
	}
}
