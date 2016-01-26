package toberumono.lexer.genericBase;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * A functional interface that represents the constructors for a {@link GenericLanguage}.
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
@FunctionalInterface
public interface LanguageConstructor<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends GenericRule<C, T, R, D, L>, D extends GenericDescender<C, T, R, D, L>, L extends GenericLexer<C, T, R, D, L>> {
	
	/**
	 * Constructs a {@link GenericLanguage} with the given {@link ConsCellConstructor} and data maps. Note that the
	 * {@link Map Maps} are <i>not</i> copied in the constructor
	 * 
	 * @param cellConstructor
	 *            the {@link ConsCellConstructor} to be used
	 * @param rules
	 *            a {@link Map} containing the {@link GenericRule Rules}
	 * @param descenders
	 *            a {@link Map} containing the {@link GenericDescender Descender}
	 * @param ignores
	 *            a {@link Map} containing the {@link Pattern Patterns} to ignore
	 * @param names
	 *            a {@link Map} containing the names that are in use
	 * @param patterns
	 *            a {@link Map} that maps {@link Pattern Patterns} to their associated {@link GenericAction} actions
	 * @return a {@link GenericLanguage} with the given data
	 */
	public GenericLanguage<C, T, R, D, L> construct(ConsCellConstructor<T, C> cellConstructor, Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores,
			Map<Pattern, String> names, Map<Pattern, GenericAction<C, T, R, D, L, Matcher>> patterns);
			
	/**
	 * Constructs an empty {@link GenericLanguage} with the given {@link ConsCellConstructor}
	 * 
	 * @param cellConstructor
	 *            the {@link ConsCellConstructor} to be used
	 * @return an empty {@link GenericLanguage}
	 */
	public default GenericLanguage<C, T, R, D, L> construct(ConsCellConstructor<T, C> cellConstructor) {
		return construct(cellConstructor, new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
	}
}
