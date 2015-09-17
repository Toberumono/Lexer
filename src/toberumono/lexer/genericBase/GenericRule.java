package toberumono.lexer.genericBase;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.structures.sexps.GenericConsCell;
import toberumono.structures.sexps.GenericConsType;

/**
 * Represents the action to take upon seeing input that matches a particular {@link java.util.regex.Pattern Pattern}.
 * 
 * @author Toberumono
 * @param <C>
 *            the implementation of {@link GenericConsCell} to be used
 * @param <T>
 *            the implementation of {@link GenericConsType} to be used
 * @param <R>
 *            the implementation of {@link GenericRule} to be used
 * @param <D>
 *            the implementation of {@link GenericDescender} to be used
 * @param <L>
 *            the implementation of {@link GenericLexer} to be used
 */
public class GenericRule<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends GenericRule<C, T, R, D, L>, D extends GenericDescender<C, T, R, D, L>, L extends GenericLexer<C, T, R, D, L>> {
	protected final Pattern pattern;
	protected final GenericAction<C, T, R, D, L, MatchResult> action;
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param type
	 *            the type for <tt>ConsCell</tt>s matched by this rule
	 */
	public GenericRule(Pattern pattern, T type) {
		this(pattern, (lexer, state, match) -> lexer.getConsCellConstructor().construct(match.group(), type, null, lexer.emptyType));
	}
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex {@link java.util.regex.Pattern Pattern} for this <tt>Rule</tt> to use.
	 * @param action
	 *            the action to perform on the part of the input matched by this <tt>Rule</tt>
	 */
	public GenericRule(Pattern pattern, GenericAction<C, T, R, D, L, MatchResult> action) {
		this.pattern = pattern;
		this.action = action;
	}
}
