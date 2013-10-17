package lexer;

import java.util.regex.Matcher;

import lexer.errors.LexerException;

/**
 * Used to add a custom action in between finding the token in the lexer and storing it.
 * 
 * @author Joshua Lipstone
 */
public interface Action<T> {
	
	/**
	 * @param match
	 *            the token in the <tt>String</tt>
	 * @param lexer
	 *            the lexer that found the token
	 * @return the result of applying the Action on the token
	 */
	public Token action(Matcher match, Lexer lexer, Type<T> type) throws LexerException;
}
