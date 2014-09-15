package lexer;

import lexer.abstractLexer.AbstractRule;

/**
 * A Rule for the lexer
 * 
 * @author Joshua Lipstone
 * @param <T>
 *            the type of Object to be placed in the resulting token.
 */
public class Rule<T> extends AbstractRule<Token, Type<T>, T, Lexer> {
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            a regex <tt>String</tt> that contains the <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param type
	 *            the the type for <tt>Token</tt>s matched by this rule
	 */
	public Rule(String pattern, Type<T> type) {
		super(pattern, type);
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
	 */
	public Rule(String pattern, int flags, Type<T> type) {
		super(pattern, flags, type);
	}
}
