package toberumono.lexer.genericBase;

import toberumono.lexer.errors.LexerException;

/**
 * Represents an action to apply to a matched part of an input.
 * 
 * @author Toberumono
 * @param <To>
 *            the subclass of {@link GenericToken} to use
 * @param <Ty>
 *            the subclass of {@link GenericType} to use
 * @param <L>
 *            the subclass of {@link GenericLexer} to use
 * @param <T>
 *            the type of the matched data
 */
@FunctionalInterface
public interface GenericAction<To extends GenericToken<Ty, To>, Ty extends GenericType, R extends GenericRule<To, Ty, R, D, L>, D extends GenericDescender<To, Ty, R, D, L>, L extends GenericLexer<To, Ty, R, D, L>, T> {
	/**
	 * Performs the action
	 * 
	 * @param lexer
	 *            the lexer that matched the value
	 * @param state
	 *            the lexer's state
	 * @param match
	 *            the value that was matched
	 * @return an instance of {@link GenericToken}
	 * @throws LexerException
	 *             if an error occurs
	 */
	public To perform(L lexer, LexerState<To, Ty, R, D, L> state, T match) throws LexerException;
}
