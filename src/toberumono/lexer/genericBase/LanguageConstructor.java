package toberumono.lexer.genericBase;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

@FunctionalInterface
public interface LanguageConstructor<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends GenericRule<C, T, R, D, L>, D extends GenericDescender<C, T, R, D, L>, L extends GenericLexer<C, T, R, D, L>> {
	
	public GenericLanguage<C, T, R, D, L> construct(ConsCellConstructor<T, C> cellConstructor, Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores, Map<Pattern, String> names,
			Map<Pattern, GenericAction<C, T, R, D, L, Matcher>> patterns);
			
	/**
	 * @return an empty token
	 */
	public default GenericLanguage<C, T, R, D, L> construct(ConsCellConstructor<T, C> cellConstructor) {
		return construct(cellConstructor, new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
	}
}
