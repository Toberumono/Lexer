package toberumono.lexer.base;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * An implementation of the core components of {@link Rule}.
 * Represents the action to take upon seeing input that matches a particular {@link Pattern}.
 * 
 * @author Toberumono
 * @param <C>
 *            the implementation of {@link ConsCell} to be used
 * @param <T>
 *            the implementation of {@link ConsType} to be used
 * @param <R>
 *            the implementation of {@link Rule} to be used
 * @param <D>
 *            the implementation of {@link Descender} to be used
 * @param <L>
 *            the implementation of {@link Lexer} to be used
 */
public class AbstractRule<C extends ConsCell, T extends ConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>>
		implements Rule<C, T, R, D, L> {
	private final Pattern pattern;
	private final LexerAction<C, T, R, D, L, MatchResult> action;
	
	/**
	 * Constructs a new {@link Rule} with the given data
	 * 
	 * @param pattern
	 *            the regex {@link Pattern} for this {@link Rule} to use.
	 * @param type
	 *            the type for {@code ConsCell}s matched by this rule
	 */
	public AbstractRule(Pattern pattern, T type) {
		this(pattern, (lexer, state, match) -> lexer.getConsCellConstructor().construct(match.group(), type, null, lexer.getEmptyType()));
	}
	
	/**
	 * Constructs a new {@link Rule} with the given data
	 * 
	 * @param pattern
	 *            the regex {@link Pattern} for this {@link Rule} to use.
	 * @param action
	 *            the action to perform on the part of the input matched by this {@link Rule}
	 */
	public AbstractRule(Pattern pattern, LexerAction<C, T, R, D, L, MatchResult> action) {
		this.pattern = pattern;
		this.action = action;
	}
	
	@Override
	public Pattern getPattern() {
		return pattern;
	}
	
	@Override
	public LexerAction<C, T, R, D, L, MatchResult> getAction() {
		return action;
	}
}
