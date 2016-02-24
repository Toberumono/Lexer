package toberumono.lexer.base;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * Represents the action to take upon seeing input that matches a particular {@link Pattern}.
 * 
 * @author Toberumono
 * @param <C>
 *            the implementation of {@link GenericConsCell} to be used
 * @param <T>
 *            the implementation of {@link GenericConsType} to be used
 * @param <R>
 *            the implementation of {@link Rule} to be used
 * @param <D>
 *            the implementation of {@link Descender} to be used
 * @param <L>
 *            the implementation of {@link Lexer} to be used
 */
public class AbstractRule<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends AbstractRule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>>
		implements Rule<C, T, R, D, L> {
	private final Pattern pattern;
	private final Action<C, T, R, D, L, MatchResult> action;
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param type
	 *            the type for <tt>ConsCell</tt>s matched by this rule
	 */
	public AbstractRule(Pattern pattern, T type) {
		this(pattern, (lexer, state, match) -> lexer.getConsCellConstructor().construct(match.group(), type, null, lexer.getEmptyType()));
	}
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex {@link Pattern} for this <tt>Rule</tt> to use.
	 * @param action
	 *            the action to perform on the part of the input matched by this <tt>Rule</tt>
	 */
	public AbstractRule(Pattern pattern, Action<C, T, R, D, L, MatchResult> action) {
		this.pattern = pattern;
		this.action = action;
	}
	
	@Override
	public Pattern getPattern() {
		return pattern;
	}
	
	@Override
	public Action<C, T, R, D, L, MatchResult> getAction() {
		return action;
	}
}