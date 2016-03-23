package toberumono.lexer.base;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
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
public interface Rule<C extends ConsCell, T extends ConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>> {
	
	/**
	 * @return the {@link Pattern} that identifies tokens to which the {@link Rule} applies
	 */
	public Pattern getPattern();
	
	/**
	 * @return the {@link LexerAction} that the {@link Rule} performs
	 */
	public LexerAction<C, T, R, D, L, MatchResult> getAction();
}
