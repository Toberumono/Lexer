package toberumono.lexer.base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A functional interface that represents the constructors for a {@link Language}.
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
@FunctionalInterface
public interface LanguageConstructor<C extends ConsCell, T extends ConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>> {
	
	/**
	 * Constructs a {@link Language} with the given data maps<br>
	 * <b>Note:</b> the {@link Map Maps} are <i>not</i> copied in the constructor<br>
	 * 
	 * @param rules
	 *            a {@link Map} containing the {@link Rule Rules}
	 * @param descenders
	 *            a {@link Map} containing the {@link Descender Descender}
	 * @param ignores
	 *            a {@link Map} containing the {@link Pattern Patterns} to ignore
	 * @param names
	 *            a {@link Map} containing the names that are in use
	 * @param patterns
	 *            a {@link Map} that maps {@link Pattern Patterns} to their associated {@link LexerAction} actions
	 * @return a {@link Language} with the given data
	 */
	public Language<C, T, R, D, L> construct(Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores, Map<Pattern, String> names,
			Map<Pattern, LexerAction<C, T, R, D, L, MatchResult>> patterns);
	
	/**
	 * Constructs an empty {@link Language}
	 * 
	 * @return an empty {@link Language}
	 */
	public default Language<C, T, R, D, L> construct() {
		return construct(new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
	}
}
