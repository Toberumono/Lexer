package toberumono.lexer.errors;

import toberumono.lexer.base.Descender;
import toberumono.lexer.base.Lexer;
import toberumono.lexer.base.LexerState;
import toberumono.lexer.base.Rule;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;
import toberumono.structures.sexpressions.GenericConsCell;

/**
 * Root class for all exceptions thrown by this library.
 * 
 * @author Toberumono
 */
public class LexerException extends RuntimeException {
	private final LexerState<?, ?, ?, ?, ?> state; //We have to use wildcards because Exceptions cannot have generic parameters
	
	/**
	 * Constructs a new exception with {@code null} as its detail message. The cause is not initialized, and may subsequently
	 * be initialized by a call to {@link #initCause}.
	 * 
	 * @param state
	 *            the {@link LexerState} of the {@link Lexer} immediately prior to the exception being raised
	 */
	public LexerException(LexerState<?, ?, ?, ?, ?> state) {
		super();
		this.state = state.copy();
	}
	
	/**
	 * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 *
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
	 * @param state
	 *            the {@link LexerState} of the {@link Lexer} immediately prior to the exception being raised
	 */
	public LexerException(String message, LexerState<?, ?, ?, ?, ?> state) {
		super(message);
		this.state = state.copy();
	}
	
	/**
	 * Constructs a new message with the specified {@code cause} and its detail message (if {@code cause != null}).<br>
	 * This is mostly useful for re-throwing exceptions as {@link LexerException LexerExceptions}.
	 * 
	 * @param cause
	 *            the reason that this exception is being thrown
	 * @param state
	 *            the {@link LexerState} of the {@link Lexer} immediately prior to the exception being raised
	 */
	public LexerException(Throwable cause, LexerState<?, ?, ?, ?, ?> state) {
		super(cause);
		this.state = state.copy();
	}
	
	/**
	 * Constructs a new exception with the specified detail message and cause.<br>
	 * Note that the detail message associated with {@code cause} is <i>not</i> automatically incorporated in this
	 * exception's detail message.
	 *
	 * @param message
	 *            the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the {@link #getCause()} method). (A {@code null} value is
	 *            permitted, and indicates that the cause is nonexistent or unknown.)
	 * @param state
	 *            the {@link LexerState} of the {@link Lexer} immediately prior to the exception being raised
	 */
	public LexerException(String message, Throwable cause, LexerState<?, ?, ?, ?, ?> state) {
		super(message, cause);
		this.state = state.copy();
	}
	
	/**
	 * This method does use an unsafe type-cast; however, no issues will occur provided that the {@link Lexer} is one that
	 * threw the exception in the first place.
	 * 
	 * @param <C>
	 *            the implementation of {@link ConsCell} in use
	 * @param <T>
	 *            the implementation of {@link ConsType} in use
	 * @param <R>
	 *            the implementation of {@link Rule} in use
	 * @param <D>
	 *            the implementation of {@link Descender} in use
	 * @param <L>
	 *            the implementation of {@link Lexer} in use
	 * @return the {@link LexerState} of the {@link Lexer} immediately prior to the exception being raised
	 */
	@SuppressWarnings("unchecked")
	public <C extends GenericConsCell<C, T>, T extends ConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>> LexerState<C, T, R, D, L>
			getState() {
		return (LexerState<C, T, R, D, L>) state;
	}
}
