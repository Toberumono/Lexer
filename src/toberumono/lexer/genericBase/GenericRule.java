package toberumono.lexer.genericBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the action to take upon seeing input that matches a particular {@link java.util.regex.Pattern Pattern}.
 * 
 * @author Toberumono
 * @param <To>
 *            the implementation of {@link GenericToken} to use
 * @param <Ty>
 *            the implementation of {@link GenericType} to use
 * @param <L>
 *            the implementation of {@link GenericLexer} to use
 */
public abstract class GenericRule<To extends GenericToken<Ty, To>, Ty extends GenericType, L extends GenericLexer<To, Ty, ?, ?, L>> {
	protected final Pattern pattern;
	protected final GenericAction<To, Matcher, L> action;
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param type
	 *            the type for <tt>Token</tt>s matched by this rule
	 */
	public GenericRule(Pattern pattern, Ty type) {
		this(pattern, (match, lexer) -> ((TokenConstructor<Ty, To>) lexer.getTokenConstructor()).makeNewToken(match.group(), type, null, lexer.emptyType));
	}
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex {@link java.util.regex.Pattern Pattern} for this <tt>Rule</tt> to use.
	 * @param action
	 *            the action to perform on the part of the input matched by this <tt>Rule</tt>
	 */
	public GenericRule(Pattern pattern, GenericAction<To, Matcher, L> action) {
		this.pattern = pattern;
		this.action = action;
	}
}
