package toberumono.lexer.base;

import toberumono.lexer.errors.LexerException;
import toberumono.utils.functions.ExceptedBiConsumer;

/**
 * Represents an action to perform when the open token for a {@link AbstractDescender} is found.
 * 
 * @author Toberumono
 * @param <L>
 *            the subclass of {@link AbstractLexer} to use
 */
@FunctionalInterface
public interface DescenderAction<L extends Lexer<?, ?, ?, ?, L>> extends ExceptedBiConsumer<L, LexerState<?, ?, ?, ?, L>> {
	
	/**
	 * Performs the action associated with the descender.
	 * 
	 * @param lexer
	 *            the {@link AbstractLexer Lexer} that is requesting the action be performed
	 * @param state
	 *            the current {@link LexerState State} of the {@link AbstractLexer Lexer} that is requesting the action to be
	 *            performed
	 * @throws LexerException
	 *             if an error occurs
	 */
	@Override
	public void accept(L lexer, LexerState<?, ?, ?, ?, L> state) throws LexerException;
	
	/**
	 * Performs the action associated with the descender.<br>
	 * Forwards to {@link #accept(Lexer, LexerState)}
	 * 
	 * @param lexer
	 *            the {@link AbstractLexer Lexer} that is requesting the action be performed
	 * @param state
	 *            the current {@link LexerState State} of the {@link AbstractLexer Lexer} that is requesting the action to be
	 *            performed
	 * @throws LexerException
	 *             if an error occurs
	 */
	@Override
	@Deprecated
	public default void apply(L lexer, LexerState<?, ?, ?, ?, L> state) throws Exception {
		accept(lexer, state);
	}
}
