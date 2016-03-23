package toberumono.lexer.base;

import toberumono.lexer.errors.LexerException;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * Represents an action to apply to a matched part of an input.
 * 
 * @author Toberumono
 * @param <C>
 *            the implementation of {@link ConsCell} to be used
 * @param <T>
 *            the implementation of {@link ConsType} to be used
 * @param <R>
 *            the implementation of {@link Rule} to be used
 * @param <D>
 *            the implementation of {@link Descender} to be used
 * @param <L>
 *            the implementation of {@link Lexer} to be used
 * @param <O>
 *            the type of the matched data
 */
@FunctionalInterface
public interface DescenderOpenAction<C extends ConsCell, T extends ConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>, O> {
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
}
