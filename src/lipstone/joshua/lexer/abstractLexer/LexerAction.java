package lipstone.joshua.lexer.abstractLexer;

import lipstone.joshua.lexer.errors.LexerException;

/**
 * Represents an action to apply to a matched part of an input.
 * 
 * @author Joshua Lipstone
 * @param <To>
 *            the subclass of {@link AbstractToken} to use
 * @param <M>
 *            the type appropriate for the match data
 * @param <L>
 *            the subclass of {@link AbstractLexer} to use
 */
@FunctionalInterface
public interface LexerAction<To, M, L> {
	public To perform(M match, L lexer) throws LexerException;
}
