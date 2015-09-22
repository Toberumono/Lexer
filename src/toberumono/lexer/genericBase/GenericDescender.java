package toberumono.lexer.genericBase;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * Represents the action to take upon seeing a particular descent-start cell.
 * 
 * @author Toberumono
 * @param <C>
 *            the implementation of {@link GenericConsCell} to be used
 * @param <T>
 *            the implementation of {@link GenericConsType} to be used
 * @param <R>
 *            the implementation of {@link GenericRule} to be used
 * @param <D>
 *            the implementation of {@link GenericDescender} to be used
 * @param <L>
 *            the implementation of {@link GenericLexer} to be used
 */
public class GenericDescender<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends GenericRule<C, T, R, D, L>, D extends GenericDescender<C, T, R, D, L>, L extends GenericLexer<C, T, R, D, L>> {
	protected final Pattern open, close;
	protected final GenericAction<C, T, R, D, L, C> closeAction;
	protected final DescenderOpenAction<C, T, R, D, L, MatchResult> openAction;
	
	/**
	 * Constructs a {@link GenericDescender} that corresponds to the given type with the given open and close tokens.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param type
	 *            the {@link GenericConsType type} to be associated with the {@link GenericDescender}
	 */
	public GenericDescender(String open, String close, T type) {
		this(Pattern.compile(open, Pattern.LITERAL), Pattern.compile(close, Pattern.LITERAL), (lexer, state, match) -> {} ,
				(lexer, state, match) -> ((ConsCellConstructor<T, C>) lexer.getConsCellConstructor()).construct(match, type, null, lexer.emptyType));
	}
	
	/**
	 * Constructs a {@link GenericDescender} with the given open and close tokens and open and close actions.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param openAction
	 *            the {@link DescenderAction} to perform when the open token is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link GenericAction} to perform when the close token is encountered (prior to ascending)
	 */
	@Deprecated
	public GenericDescender(String open, String close, DescenderAction<L> openAction, GenericAction<C, T, R, D, L, C> closeAction) {
		this(Pattern.compile(open, Pattern.LITERAL), Pattern.compile(close, Pattern.LITERAL), openAction, closeAction);
	}
	
	/**
	 * Constructs a {@link GenericDescender} that takes no action when the open token is matched with the given open and
	 * close tokens and close action.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param closeAction
	 *            the {@link GenericAction} to perform when the close token is encountered (prior to ascending)
	 */
	public GenericDescender(String open, String close, GenericAction<C, T, R, D, L, C> closeAction) {
		this(Pattern.compile(open, Pattern.LITERAL), Pattern.compile(close, Pattern.LITERAL), (lexer, state, match) -> {} , closeAction);
	}
	
	/**
	 * Constructs a {@link GenericDescender} with the given open and close tokens and open and close actions.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param openAction
	 *            the {@link DescenderOpenAction} to perform when the open token is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link GenericAction} to perform when the close token is encountered (prior to ascending)
	 */
	public GenericDescender(String open, String close, DescenderOpenAction<C, T, R, D, L, MatchResult> openAction, GenericAction<C, T, R, D, L, C> closeAction) {
		this(Pattern.compile(open, Pattern.LITERAL), Pattern.compile(close, Pattern.LITERAL), openAction, closeAction);
	}
	
	/**
	 * Constructs a {@link GenericDescender} that corresponds to the given type with the given open and close tokens.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param type
	 *            the {@link GenericConsType type} to be associated with the {@link GenericDescender}
	 */
	public GenericDescender(Pattern open, Pattern close, T type) {
		this(open, close, (lexer, state, match) -> {} , (lexer, state, match) -> ((ConsCellConstructor<T, C>) lexer.getConsCellConstructor()).construct(match, type, null, lexer.emptyType));
	}
	
	/**
	 * Constructs a {@link GenericDescender} with the given open and close tokens and open and close actions.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param openAction
	 *            the {@link DescenderAction} to perform when the open token is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link GenericAction} to perform when the close token is encountered (prior to ascending)
	 */
	@Deprecated
	public GenericDescender(Pattern open, Pattern close, DescenderAction<L> openAction, GenericAction<C, T, R, D, L, C> closeAction) {
		this(open, close, DescenderOpenAction.wrapOldAction(openAction), closeAction);
	}
	
	/**
	 * Constructs a {@link GenericDescender} that takes no action when the open token is matched with the given open and
	 * close tokens and close action.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param closeAction
	 *            the {@link GenericAction} to perform when the close token is encountered (prior to ascending)
	 */
	public GenericDescender(Pattern open, Pattern close, GenericAction<C, T, R, D, L, C> closeAction) {
		this(open, close, (lexer, state, match) -> {} , closeAction);
	}
	
	/**
	 * Constructs a {@link GenericDescender} with the given open and close tokens and open and close actions.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param openAction
	 *            the {@link DescenderOpenAction} to perform when the open token is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link GenericAction} to perform when the close token is encountered (prior to ascending)
	 */
	public GenericDescender(Pattern open, Pattern close, DescenderOpenAction<C, T, R, D, L, MatchResult> openAction, GenericAction<C, T, R, D, L, C> closeAction) {
		this.open = open;
		this.close = close;
		this.openAction = openAction;
		this.closeAction = closeAction;
	}
}
