package toberumono.lexer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.lexer.genericBase.DescenderAction;
import toberumono.lexer.genericBase.DescenderOpenAction;
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
	 * Constructs a new {@link Descender} with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link Descender}
	 * @param close
	 *            the close symbol for this {@link Descender}
	 * @param type
	 *            the <tt>Type</tt> for the ConsCell returned by this {@link Descender}
	 */
	public Descender(String open, String close, ConsType type) {
		super(open, close, type);
	}
	
	/**
	 * Constructs a new {@link Descender} with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link Descender}
	 * @param close
	 *            the close symbol for this {@link Descender}
	 * @param openAction
	 *            the actions to perform upon seeing the open token of this {@link Descender}
	 * @param closeAction
	 *            the action to be performed on the <tt>ConsCells</tt> matched within the found descent set
	 */
	@Deprecated
	public Descender(String open, String close, DescenderAction<Lexer> openAction, GenericAction<ConsCell, ConsType, Rule, Descender, Lexer, ConsCell> closeAction) {
		super(open, close, openAction, closeAction);
	}
	
	/**
	 * Constructs a new {@link Descender} that takes no action when the open symbol is matched with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link Descender}
	 * @param close
	 *            the close symbol for this {@link Descender}
	 * @param closeAction
	 *            the action to be performed on the <tt>ConsCells</tt> matched within the found descent set
	 */
	public Descender(String open, String close, GenericAction<ConsCell, ConsType, Rule, Descender, Lexer, ConsCell> closeAction) {
		super(open, close, closeAction);
	}
	
	/**
	 * Constructs a new {@link Descender} with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link Descender}
	 * @param close
	 *            the close symbol for this {@link Descender}
	 * @param openAction
	 *            the actions to perform upon seeing the open token of this {@link Descender}
	 * @param closeAction
	 *            the action to be performed on the <tt>ConsCells</tt> matched within the found descent set
	 */
	public Descender(String open, String close, DescenderOpenAction<ConsCell, ConsType, Rule, Descender, Lexer, MatchResult> openAction,
			GenericAction<ConsCell, ConsType, Rule, Descender, Lexer, ConsCell> closeAction) {
		super(open, close, openAction, closeAction);
	}
	
	/**
	 * Constructs a new {@link Descender} with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link Descender}
	 * @param close
	 *            the close symbol for this {@link Descender}
	 * @param type
	 *            the <tt>Type</tt> for the ConsCell returned by this {@link Descender}
	 */
	public Descender(Pattern open, Pattern close, ConsType type) {
		super(open, close, type);
	}
	
	/**
	 * Constructs a new {@link Descender} with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link Descender}
	 * @param close
	 *            the close symbol for this {@link Descender}
	 * @param openAction
	 *            the actions to perform upon seeing the open token of this {@link Descender}
	 * @param closeAction
	 *            the action to be performed on the <tt>ConsCells</tt> matched within the found descent set
	 */
	@Deprecated
	public Descender(Pattern open, Pattern close, DescenderAction<Lexer> openAction, GenericAction<ConsCell, ConsType, Rule, Descender, Lexer, ConsCell> closeAction) {
		super(open, close, openAction, closeAction);
	}
	
	/**
	 * Constructs a new {@link Descender} with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link Descender}
	 * @param close
	 *            the close symbol for this {@link Descender}
	 * @param closeAction
	 *            the action to be performed on the <tt>ConsCells</tt> matched within the found descent set
	 */
	public Descender(Pattern open, Pattern close, GenericAction<ConsCell, ConsType, Rule, Descender, Lexer, ConsCell> closeAction) {
		super(open, close, closeAction);
	}
	
	/**
	 * Constructs a new {@link Descender} with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link Descender}
	 * @param close
	 *            the close symbol for this {@link Descender}
	 * @param openAction
	 *            the actions to perform upon seeing the open token of this {@link Descender}
	 * @param closeAction
	 *            the action to be performed on the <tt>ConsCells</tt> matched within the found descent set
	 */
	public Descender(Pattern open, Pattern close, DescenderOpenAction<ConsCell, ConsType, Rule, Descender, Lexer, MatchResult> openAction,
			GenericAction<ConsCell, ConsType, Rule, Descender, Lexer, ConsCell> closeAction) {
		super(open, close, openAction, closeAction);
	}
}
