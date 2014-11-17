package lipstone.joshua.lexer.errors;

/**
 * Root class for all exceptions thrown by this library.
 * 
 * @author Joshua Lipstone
 */
public class LexerException extends Exception {
	
	/**
	 * Constructs a new exception with {@code null} as its detail message. The cause is not initialized, and may subsequently
	 * be initialized by a call to {@link #initCause}.
	 */
	public LexerException() {
		super();
	}
	
	/**
	 * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 *
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
	 */
	public LexerException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * <p>
	 * Note that the detail message associated with {@code cause} is <i>not</i> automatically incorporated in this
	 * exception's detail message.
	 *
	 * @param message
	 *            the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or unknown.)
	 * @since 1.4
	 */
	public LexerException(String message, Throwable cause) {
		super(message, cause);
	}
}
