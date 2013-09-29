package lexer;

/**
 * Used to add a custom action in between finding the token in the lexer and storing it.
 * 
 * @author Joshua Lipstone
 * @param <T>
 *            the desired output type
 */
public interface Action<T> {
	
	/**
	 * @param input
	 *            the token in the <tt>String</tt>
	 * @param lexer
	 *            the lexer that found the token
	 * @return the result of applying the Action on the token
	 */
	public T action(String input, Lexer lexer);
}
