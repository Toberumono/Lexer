package toberumono.lexer.genericBase;

import toberumono.lexer.errors.LexerException;

/**
 * Represents an action to apply to a matched part of an input.
 * 
 * @author Toberumono
 * @param <To>
 *            the subclass of {@link GenericToken} to use
 * @param <M>
 *            the type appropriate for the match data
 * @param <L>
 *            the subclass of {@link GenericLexer} to use
 */
@FunctionalInterface
public interface GenericAction<To, M, L> {
	/**
	 * Performs the action
	 * @param match the value that was matched
	 * @param lexer the lexer that matched the value
	 * @return an instance of {@link GenericToken}
	 * @throws LexerException
	 *             if an error occurs
	 */
	public To perform(M match, L lexer) throws LexerException;
}
