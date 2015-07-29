package toberumono.lexer.genericBase;

import toberumono.lexer.errors.LexerException;

/**
 * Represents an action to perform when the open token for a {@link GenericDescender} is found.
 * 
 * @author Toberumono
 * @param <L>
 *            the subclass of {@link GenericLexer} to use
 */
@FunctionalInterface
public interface DescenderAction<L> {
	/**
	 * Performs the action associated with the descender
	 * @param lexer
	 *            the {@link GenericLexer Lexer} that is requesting the action be performed
	 * @throws LexerException
	 *             if an error occurs
	 */
	public void perform(L lexer) throws LexerException;
}
