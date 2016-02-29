package toberumono.lexer.errors;

import toberumono.lexer.base.Descender;
import toberumono.lexer.base.Lexer;
import toberumono.lexer.base.LexerState;
import toberumono.lexer.base.Rule;

/**
 * Thrown when a {@link Lexer} encounters and unrecognized character (aka a character that does not match any of the
 * {@link Rule Rules} or {@link Descender Descenders} a {@link Lexer} has been given.
 * 
 * @author Toberumono
 */
public class UnrecognizedCharacterException extends LexerException {
	
	/**
	 * Constructs an {@link UnrecognizedCharacterException} based on the given {@link LexerState state}.
	 * 
	 * @param state
	 *            the {@link LexerState State} of the {@link Lexer} immediately prior to the exception being raised
	 */
	public UnrecognizedCharacterException(LexerState<?, ?, ?, ?, ?> state) {
		this(state.getInput(), state.getHead(), state);
	}
	
	/**
	 * Constructs an {@link UnrecognizedCharacterException} for an unrecognized character in the given input at the given
	 * index.
	 * 
	 * @param input
	 *            the input in which the unrecognized character was encountered
	 * @param index
	 *            the index at which it was encountered
	 * @param state
	 *            the {@link LexerState State} of the {@link Lexer} immediately prior to the exception being raised
	 */
	public UnrecognizedCharacterException(String input, int index, LexerState<?, ?, ?, ?, ?> state) {
		super("Unknown character at " + index + ": " + input.charAt(index) + "\nRemaining Input: " + input.substring(index), state);
	}
}
