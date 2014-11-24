package lipstone.joshua.lexer.abstractLexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the action to take upon seeing input that matches a particular {@link java.util.regex.Pattern Pattern}.
 * 
 * @author Joshua Lipstone
 * @param <To>
 *            the subclass of {@link AbstractToken} to use
 * @param <Ty>
 *            the subclass of {@link AbstractType} to use
 * @param <L>
 *            the subclass of {@link AbstractLexer} to use
 */
public abstract class AbstractRule<To extends AbstractToken<Ty, To>, Ty extends AbstractType, L extends AbstractLexer<To, Ty, ?, ?, L>> {
	protected final Pattern pattern;
	protected final LexerAction<To, Matcher, L> action;
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param type
	 *            the type for <tt>Token</tt>s matched by this rule
	 */
	public AbstractRule(Pattern pattern, Ty type) {
		this(pattern, (match, lexer) -> {
			return ((TokenConstructor<Ty, To>) lexer.getTokenConstructor()).makeNewToken(match.group(), type, null, lexer.emptyType);
		});
	}
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex {@link java.util.regex.Pattern Pattern} for this <tt>Rule</tt> to use.
	 * @param action
	 *            the action to perform on the part of the input matched by this <tt>Rule</tt>
	 */
	public AbstractRule(Pattern pattern, LexerAction<To, Matcher, L> action) {
		this.pattern = pattern;
		this.action = action;
	}
}
