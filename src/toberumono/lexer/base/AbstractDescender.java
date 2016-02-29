package toberumono.lexer.base;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * An implementation of the core components of {@link Descender}.
 * Represents the action to take upon seeing a particular descent-start cell.
 * 
 * @author Toberumono
 * @param <C>
 *            the implementation of {@link GenericConsCell} to be used
 * @param <T>
 *            the implementation of {@link GenericConsType} to be used
 * @param <R>
 *            the implementation of {@link Rule} to be used
 * @param <D>
 *            the implementation of {@link Descender} to be used
 * @param <L>
 *            the implementation of {@link Lexer} to be used
 */
public class AbstractDescender<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>>
		implements Descender<C, T, R, D, L> {
	private final Pattern open, close;
	private final Action<C, T, R, D, L, C> closeAction;
	private final DescenderOpenAction<C, T, R, D, L, MatchResult> openAction;
	
	/**
	 * Constructs an {@link AbstractDescender} that corresponds to the given type with the given open and close tokens.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param type
	 *            the {@link GenericConsType type} to be associated with the {@link Descender}
	 */
	public AbstractDescender(String open, String close, T type) {
		this(Pattern.compile(open, Pattern.LITERAL), Pattern.compile(close, Pattern.LITERAL), (lexer, state, match) -> {},
				(lexer, state, match) -> ((ConsCellConstructor<T, C>) lexer.getConsCellConstructor()).construct(match, type, null, lexer.getEmptyType()));
	}
	
	/**
	 * Constructs an {@link AbstractDescender} that takes no action when the open token is matched with the given open and
	 * close tokens and close action.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param closeAction
	 *            the {@link Action} to perform when the close token is encountered (prior to ascending)
	 */
	public AbstractDescender(String open, String close, Action<C, T, R, D, L, C> closeAction) {
		this(Pattern.compile(open, Pattern.LITERAL), Pattern.compile(close, Pattern.LITERAL), (lexer, state, match) -> {}, closeAction);
	}
	
	/**
	 * Constructs a {@link AbstractDescender} with the given open and close tokens and open and close actions.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param openAction
	 *            the {@link DescenderOpenAction} to perform when the open token is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link Action} to perform when the close token is encountered (prior to ascending)
	 */
	public AbstractDescender(String open, String close, DescenderOpenAction<C, T, R, D, L, MatchResult> openAction, Action<C, T, R, D, L, C> closeAction) {
		this(Pattern.compile(open, Pattern.LITERAL), Pattern.compile(close, Pattern.LITERAL), openAction, closeAction);
	}
	
	/**
	 * Constructs a {@link AbstractDescender} that corresponds to the given type with the given open and close tokens.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param type
	 *            the {@link GenericConsType type} to be associated with the {@link Descender}
	 */
	public AbstractDescender(Pattern open, Pattern close, T type) {
		this(open, close, (lexer, state, match) -> {}, (lexer, state, match) -> ((ConsCellConstructor<T, C>) lexer.getConsCellConstructor()).construct(match, type, null, lexer.getEmptyType()));
	}
	
	/**
	 * Constructs a {@link AbstractDescender} that takes no action when the open token is matched with the given open and
	 * close tokens and close action.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param closeAction
	 *            the {@link Action} to perform when the close token is encountered (prior to ascending)
	 */
	public AbstractDescender(Pattern open, Pattern close, Action<C, T, R, D, L, C> closeAction) {
		this(open, close, (lexer, state, match) -> {}, closeAction);
	}
	
	/**
	 * Constructs a {@link AbstractDescender} with the given open and close tokens and open and close actions.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param openAction
	 *            the {@link DescenderOpenAction} to perform when the open token is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link Action} to perform when the close token is encountered (prior to ascending)
	 */
	public AbstractDescender(Pattern open, Pattern close, DescenderOpenAction<C, T, R, D, L, MatchResult> openAction, Action<C, T, R, D, L, C> closeAction) {
		this.open = open;
		this.close = close;
		this.openAction = openAction;
		this.closeAction = closeAction;
	}
	
	@Override
	public Pattern getOpenPattern() {
		return open;
	}
	
	@Override
	public Pattern getClosePattern() {
		return close;
	}
	
	@Override
	public Action<C, T, R, D, L, C> getCloseAction() {
		return closeAction;
	}
	
	@Override
	public DescenderOpenAction<C, T, R, D, L, MatchResult> getOpenAction() {
		return openAction;
	}
}
