package lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lexer.errors.LexerException;

/**
 * A Rule for the lexer
 * 
 * @author Joshua Lipstone
 * @param <T>
 *            the type of Object to be placed in the resulting token.
 */
public final class Rule<T> {
	final Pattern pattern;
	private final Type<T> type;
	private final Action<T> action;
	
	public Rule(Pattern pattern, Type<T> type, Action<T> action) {
		this.pattern = pattern;
		this.type = type;
		this.action = action;
	}
	
	/**
	 * Apply the <tt>Action</tt> associated with this <tt>Rule</tt>
	 * 
	 * @param match
	 * @param lexer
	 * @return the resulting value for a representative <tt>Token</tt>
	 * @throws LexerException 
	 */
	final Token apply(Matcher match, Lexer lexer) throws LexerException {
		return action == null ? new Token((T) match.group(), type) : action.action(match, lexer, type);
	}
	
	public final Type<T> getType() {
		return type;
	}
}
