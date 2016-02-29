package toberumono.lexer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.lexer.base.AbstractDescender;
import toberumono.lexer.base.Action;
import toberumono.lexer.base.DescenderOpenAction;
import toberumono.lexer.base.Lexer;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A Descender for the {@link Lexer} that uses the provided {@code ConsCell} class
 * 
 * @author Toberumono
 */
public class BasicDescender extends AbstractDescender<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer> {
	
	/**
	 * Constructs a new {@link BasicDescender} with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link BasicDescender}
	 * @param close
	 *            the close symbol for this {@link BasicDescender}
	 * @param type
	 *            the {@code Type} for the ConsCell returned by this {@link BasicDescender}
	 */
	public BasicDescender(String open, String close, ConsType type) {
		super(open, close, type);
	}
	
	/**
	 * Constructs a new {@link BasicDescender} that takes no action when the open symbol is matched with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link BasicDescender}
	 * @param close
	 *            the close symbol for this {@link BasicDescender}
	 * @param closeAction
	 *            the action to be performed on the {@code ConsCells} matched within the found descent set
	 */
	public BasicDescender(String open, String close, Action<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, ConsCell> closeAction) {
		super(open, close, closeAction);
	}
	
	/**
	 * Constructs a new {@link BasicDescender} with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link BasicDescender}
	 * @param close
	 *            the close symbol for this {@link BasicDescender}
	 * @param openAction
	 *            the actions to perform upon seeing the open token of this {@link BasicDescender}
	 * @param closeAction
	 *            the action to be performed on the {@code ConsCells} matched within the found descent set
	 */
	public BasicDescender(String open, String close, DescenderOpenAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, MatchResult> openAction,
			Action<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, ConsCell> closeAction) {
		super(open, close, openAction, closeAction);
	}
	
	/**
	 * Constructs a new {@link BasicDescender} with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link BasicDescender}
	 * @param close
	 *            the close symbol for this {@link BasicDescender}
	 * @param type
	 *            the {@code Type} for the ConsCell returned by this {@link BasicDescender}
	 */
	public BasicDescender(Pattern open, Pattern close, ConsType type) {
		super(open, close, type);
	}
	
	/**
	 * Constructs a new {@link BasicDescender} with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link BasicDescender}
	 * @param close
	 *            the close symbol for this {@link BasicDescender}
	 * @param closeAction
	 *            the action to be performed on the {@code ConsCells} matched within the found descent set
	 */
	public BasicDescender(Pattern open, Pattern close, Action<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, ConsCell> closeAction) {
		super(open, close, closeAction);
	}
	
	/**
	 * Constructs a new {@link BasicDescender} with the given data
	 * 
	 * @param open
	 *            the open symbol for this {@link BasicDescender}
	 * @param close
	 *            the close symbol for this {@link BasicDescender}
	 * @param openAction
	 *            the actions to perform upon seeing the open token of this {@link BasicDescender}
	 * @param closeAction
	 *            the action to be performed on the {@code ConsCells} matched within the found descent set
	 */
	public BasicDescender(Pattern open, Pattern close, DescenderOpenAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, MatchResult> openAction,
			Action<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, ConsCell> closeAction) {
		super(open, close, openAction, closeAction);
	}
}
