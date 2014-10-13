package lipstone.joshua.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lipstone.joshua.lexer.abstractLexer.AbstractRule;
import lipstone.joshua.lexer.abstractLexer.LexerAction;

/**
 * A Rule for the lexer
 * 
 * @author Joshua Lipstone
 */
public class Rule extends AbstractRule<Token, Type<?>, Lexer> {
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param type
	 *            the type for <tt>Token</tt>s matched by this rule
	 */
	public Rule(Pattern pattern, Type<?> type) {
		super(pattern, type);
	}
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex {@link java.util.regex.Pattern Pattern} for this <tt>Rule</tt> to use.
	 * @param action
	 *            the action to perform on the part of the input matched by this <tt>Rule</tt>
	 */
	public Rule(Pattern pattern, LexerAction<Token, Matcher, Lexer> action) {
		super(pattern, action);
	}
}
