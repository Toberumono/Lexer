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
	private final Pattern pattern;
	private final Type<T> type;
	private final Action<T> action;
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            a regex <tt>String</tt> that contains the <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param type
	 *            the the type for <tt>Token</tt>s matched by this rule
	 * @param action
	 *            the <tt>Action</tt> to take on <tt>Token</tt>s matched by this rule
	 */
	public Rule(String pattern, Type<T> type, Action<T> action) {
		this.pattern = pattern.startsWith("\\G") ? Pattern.compile(pattern) : Pattern.compile("\\G" + pattern);
		this.type = type;
		this.action = action;
	}
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            a regex <tt>String</tt> that contains the <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param flags
	 *            the regex flags defined in {@link java.util.regex.Pattern Pattern}
	 * @param type
	 *            the the type for <tt>Token</tt>s matched by this rule
	 * @param action
	 *            the <tt>Action</tt> to take on <tt>Token</tt>s matched by this rule
	 */
	public Rule(String pattern, int flags, Type<T> type, Action<T> action) {
		this.pattern = pattern.startsWith("\\G") ? Pattern.compile(pattern, flags) : Pattern.compile("\\G" + pattern, flags);
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
	
	/**
	 * @return the pattern that this <tt>Rule</tt> matches
	 */
	public Pattern getPattern() {
		return pattern;
	}
}
