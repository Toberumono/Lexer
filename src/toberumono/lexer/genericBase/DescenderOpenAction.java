package toberumono.lexer.genericBase;

import toberumono.lexer.errors.LexerException;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * Represents an action to apply to a matched part of an input.
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
 * @param <O>
 *            the type of the matched data
 */
@FunctionalInterface
public interface DescenderOpenAction<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends GenericRule<C, T, R, D, L>, D extends GenericDescender<C, T, R, D, L>, L extends GenericLexer<C, T, R, D, L>, O> {
	/**
	 * Performs the action
	 * 
	 * @param lexer
	 *            the lexer that matched the value
	 * @param state
	 *            the lexer's state
	 * @param match
	 *            the value that was matched
	 * @throws LexerException
	 *             if an error occurs
	 */
	public void perform(L lexer, LexerState<C, T, R, D, L> state, O match) throws LexerException;
	
	/**
	 * Wraps old {@link DescenderAction DescenderActions} to work with the new system.<br>
	 * This is for backwards compatibility <i>only</i>.
	 * 
	 * @param action
	 *            the {@link DescenderAction} to wrap
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
	 * @param <O>
	 *            the type of the matched data
	 * @return wrapped action
	 */
	@Deprecated
	public static <
			C extends GenericConsCell<T, C>, T extends GenericConsType, R extends GenericRule<C, T, R, D, L>, D extends GenericDescender<C, T, R, D, L>, L extends GenericLexer<C, T, R, D, L>, O>
			DescenderOpenAction<C, T, R, D, L, O> wrapOldAction(DescenderAction<L> action) {
		return (lexer, state, match) -> action.accept(lexer, state);
	}
}
