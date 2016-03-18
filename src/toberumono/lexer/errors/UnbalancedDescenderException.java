package toberumono.lexer.errors;

import toberumono.lexer.base.Descender;
import toberumono.lexer.base.Lexer;
import toberumono.lexer.base.LexerState;

/**
 * Thrown when an unbalanced {@link Descender} token is encountered. (That is to say either an open or close
 * {@link Descender} token without a counterpart.
 * 
 * @author Toberumono
 */
public class UnbalancedDescenderException extends LexerException {

	/**
	 * Constructs an {@link UnbalancedDescenderException} based on the given {@link LexerState}.
	 * 
	 * @param state
	 *            the {@link LexerState} of the {@link Lexer} immediately prior to the exception being raised
	 */
	public UnbalancedDescenderException(LexerState<?, ?, ?, ?, ?> state) {
		this(state.getInput(), state.getHead(), state);
	}
	
	/**
	 * Constructs an {@link UnbalancedDescenderException} for a {@link Descender} token in the given input at the given
	 * index.
	 * 
	 * @param input
	 *            the input in which the unbalanced descender was found
	 * @param index
	 *            the index at which it was found
	 * @param state
	 *            the {@link LexerState} of the {@link Lexer} immediately prior to the exception being raised
	 */
	public UnbalancedDescenderException(String input, int index, LexerState<?, ?, ?, ?, ?> state) {
		super("Unbalanced descender at " + index + " in " + input, state);
	}
}
