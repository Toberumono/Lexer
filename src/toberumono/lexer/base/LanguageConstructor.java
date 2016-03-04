package toberumono.lexer.base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * A functional interface that represents the constructors for a {@link Language}.
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
@FunctionalInterface
public interface LanguageConstructor<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>> {
	
	/**
	 * Constructs a {@link Language} with the given {@link ConsCellConstructor} and data maps. Note that the
	 * {@link Map Maps} are <i>not</i> copied.
	 * 
	 * @param cellConstructor
	 *            the {@link ConsCellConstructor} to be used
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
	public Language<C, T, R, D, L> construct(ConsCellConstructor<T, C> cellConstructor, Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores,
			Map<Pattern, String> names, Map<Pattern, LexerAction<C, T, R, D, L, Matcher>> patterns);
			
	/**
	 * Constructs an empty {@link Language} with the given {@link ConsCellConstructor}
	 * 
	 * @param cellConstructor
	 *            the {@link ConsCellConstructor} to be used
	 * @return an empty {@link Language}
	 */
	public default Language<C, T, R, D, L> construct(ConsCellConstructor<T, C> cellConstructor) {
		return construct(cellConstructor, new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
	}
}
