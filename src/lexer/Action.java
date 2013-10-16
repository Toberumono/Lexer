package lexer;

import java.util.regex.Matcher;

/**
 * Used to add a custom action in between finding the token in the lexer and storing it.
 * 
 * @author Joshua Lipstone
 */
public interface Action<T> {
	
	/**
	 * @param token
	 *            the token in the <tt>String</tt>
	 * @param lexer
	 *            the lexer that found the token
	 * @return the result of applying the Action on the token
	 */
	public Token action(Matcher token, Lexer lexer, Type<T> type);
}
