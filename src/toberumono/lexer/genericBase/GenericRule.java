package toberumono.lexer.genericBase;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Represents the action to take upon seeing input that matches a particular {@link java.util.regex.Pattern Pattern}.
 * 
 * @author Toberumono
 * @param <To>
 *            the implementation of {@link GenericToken} to be used
 * @param <Ty>
 *            the implementation of {@link GenericType} to be used
 * @param <R>
 *            the implementation of {@link GenericRule} to be used
 * @param <D>
 *            the implementation of {@link GenericDescender} to be used
 * @param <L>
 *            the implementation of {@link GenericLexer} to be used
 */
public class GenericRule<To extends GenericToken<Ty, To>, Ty extends GenericType, R extends GenericRule<To, Ty, R, D, L>, D extends GenericDescender<To, Ty, R, D, L>, L extends GenericLexer<To, Ty, R, D, L>> {
	protected final Pattern pattern;
	protected final GenericAction<To, Ty, R, D, L, MatchResult> action;
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param type
	 *            the type for <tt>Token</tt>s matched by this rule
	 */
	public GenericRule(Pattern pattern, Ty type) {
		this(pattern, (lexer, state, match) -> ((TokenConstructor<Ty, To>) lexer.getTokenConstructor()).construct(match.group(), type, null, lexer.emptyType));
	}
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex {@link java.util.regex.Pattern Pattern} for this <tt>Rule</tt> to use.
	 * @param action
	 *            the action to perform on the part of the input matched by this <tt>Rule</tt>
	 */
	public GenericRule(Pattern pattern, GenericAction<To, Ty, R, D, L, MatchResult> action) {
		this.pattern = pattern;
		this.action = action;
	}
}
