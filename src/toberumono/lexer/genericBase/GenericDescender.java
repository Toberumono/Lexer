package toberumono.lexer.genericBase;

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
	protected final DescenderAction<L> openAction;
	
	/**
	 * Constructs a {@link GenericDescender} that corresponds to the given type with the given open and close cells.
	 * 
	 * @param open
	 *            the open cell
	 * @param close
	 *            the close cell
	 * @param type
	 *            the {@link GenericConsType type} to be associated with the {@link GenericDescender}
	 */
	public GenericDescender(String open, String close, T type) {
		this(Pattern.compile(open, Pattern.LITERAL), Pattern.compile(close, Pattern.LITERAL), (lexer, state) -> {} ,
				(lexer, state, match) -> ((ConsCellConstructor<T, C>) lexer.getConsCellConstructor()).construct(match, type, null, lexer.emptyType));
	}
	
	/**
	 * Constructs a {@link GenericDescender} with the given open and close cells and open and close actions.
	 * 
	 * @param open
	 *            the open cell
	 * @param close
	 *            the close cell
	 * @param openAction
	 *            the {@link DescenderAction} to perform when the open cell is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link GenericAction} to perform when the close cell is encountered (prior to ascending)
	 */
	public GenericDescender(String open, String close, DescenderAction<L> openAction, GenericAction<C, T, R, D, L, C> closeAction) {
		this(Pattern.compile(open, Pattern.LITERAL), Pattern.compile(close, Pattern.LITERAL), openAction, closeAction);
	}
	
	/**
	 * Constructs a {@link GenericDescender} that corresponds to the given type with the given open and close cells.
	 * 
	 * @param open
	 *            the open cell
	 * @param close
	 *            the close cell
	 * @param type
	 *            the {@link GenericConsType type} to be associated with the {@link GenericDescender}
	 */
	public GenericDescender(Pattern open, Pattern close, T type) {
		this(open, close, (lexer, state) -> {} , (lexer, state, match) -> ((ConsCellConstructor<T, C>) lexer.getConsCellConstructor()).construct(match, type, null, lexer.emptyType));
	}
	
	/**
	 * Constructs a {@link GenericDescender} with the given open and close cells and open and close actions.
	 * 
	 * @param open
	 *            the open cell
	 * @param close
	 *            the close cell
	 * @param openAction
	 *            the {@link DescenderAction} to perform when the open cell is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link GenericAction} to perform when the close cell is encountered (prior to ascending)
	 */
	public GenericDescender(Pattern open, Pattern close, DescenderAction<L> openAction, GenericAction<C, T, R, D, L, C> closeAction) {
		this.open = open;
		this.close = close;
		this.openAction = openAction;
		this.closeAction = closeAction;
	}
}
