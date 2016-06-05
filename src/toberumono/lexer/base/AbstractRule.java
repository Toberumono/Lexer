package toberumono.lexer.base;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;
import toberumono.structures.sexpressions.GenericConsCell;

/**
 * An implementation of the core components of {@link Rule}. Represents the action to take upon seeing input that matches a
 * particular {@link Pattern}.
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
public class AbstractRule<C extends GenericConsCell<C, T>, T extends ConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>>
		implements Rule<C, T, R, D, L>, Cloneable {
	private final Pattern pattern;
	private final LexerAction<C, T, R, D, L, MatchResult> action;
	
	/**
	 * Constructs a new {@link AbstractRule} with the given {@link Pattern} and {@link ConsType}.
	 * 
	 * @param pattern
	 *            the {@link Pattern} that describes tokens that the {@link AbstractRule} can process
	 * @param type
	 *            the {@link ConsType type} of the {@code car} value of the {@link GenericConsCell ConsCells} matched by this
	 *            {@link AbstractRule rule}
	 */
	public AbstractRule(Pattern pattern, T type) {
		this(pattern, (lexer, state, match) -> lexer.getConsCellConstructor().construct(match.group(), type, null, lexer.getEmptyType()));
	}
	
	/**
	 * Constructs a new {@link AbstractRule} with the given {@link Pattern} and {@link LexerAction}.
	 * 
	 * @param pattern
	 *            the {@link Pattern} that describes tokens that the {@link AbstractRule} can process
	 * @param action
	 *            the {@link LexerAction action} to perform on the part of the input matched by the {@link AbstractRule
	 *            rule's} {@link Pattern}
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
	
	@Override
	public R clone() {
		try {
			@SuppressWarnings("unchecked")
			R clone = (R) super.clone();
			return clone;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e); //This shouldn't happen because we are Cloneable
		}
	}
}
