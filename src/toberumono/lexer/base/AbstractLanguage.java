package toberumono.lexer.base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.lexer.errors.PatternCollisionException;
import toberumono.lexer.errors.UnbalancedDescenderException;
import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * An implementation of the core components of {@link Language}.
 * This represents a language that can be used by a {@link Lexer} to tokenize an input {@link String}
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
public abstract class AbstractLanguage<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>> implements Language<C, T, R, D, L> {
	private final Map<String, R> rules;
	private final Map<String, D> descenders;
	private final Map<String, Pattern> ignores;
	private final Map<Pattern, String> names;
	private final Map<Pattern, Action<C, T, R, D, L, Matcher>> patterns;
	private final ConsCellConstructor<T, C> cellConstructor;
	
	/**
	 * Constructs an empty {@link AbstractLanguage} with the given {@link ConsCellConstructor}
	 * 
	 * @param cellConstructor
	 *            the {@link ConsCellConstructor} to be used
	 */
	public AbstractLanguage(ConsCellConstructor<T, C> cellConstructor) {
		this(cellConstructor, new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
	}
	
	/**
	 * Constructs an {@link AbstractLanguage} with the given {@link ConsCellConstructor} and data maps. Note that the
	 * {@link Map Maps} are <i>not</i> copied in the constructor
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
	 *            a {@link Map} that maps {@link Pattern Patterns} to their associated {@link Action} actions
	 */
	public AbstractLanguage(ConsCellConstructor<T, C> cellConstructor, Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores, Map<Pattern, String> names,
			Map<Pattern, Action<C, T, R, D, L, Matcher>> patterns) {
		this.cellConstructor = cellConstructor;
		this.rules = rules;
		this.descenders = descenders;
		this.ignores = ignores;
		this.names = names;
		this.patterns = patterns;
	}
	
	@Override
	public synchronized void addRule(String name, R rule) {
		if (names.containsKey(rule.getPattern()))
			throw new PatternCollisionException(rule.getPattern(), names.get(rule.getPattern()));
		rules.put(name, rule);
		names.put(rule.getPattern(), name + "::rule");
		patterns.put(rule.getPattern(), rule.getAction()::perform);
	}
	
	@Override
	public synchronized R removeRule(String name) {
		R out = rules.remove(name);
		if (out == null)
			return out;
		patterns.remove(out.getPattern());
		names.remove(out.getPattern());
		return out;
	}
	
	@Override
	public R getRule(String name) {
		return rules.get(name);
	}
	
	@Override
	public Map<String, R> getRules() {
		return rules;
	}
	
	@Override
	public synchronized void addDescender(String name, D descender) {
		if (names.containsKey(descender.getOpenPattern()))
			throw new PatternCollisionException(descender.getOpenPattern(), names.get(descender.getOpenPattern()));
		if (names.containsKey(descender.getClosePattern()))
			throw new PatternCollisionException(descender.getClosePattern(), names.get(descender.getClosePattern()));
		descenders.put(name, descender);
		names.put(descender.getOpenPattern(), name + "::descender.open");
		names.put(descender.getClosePattern(), name + "::descender.close");
		patterns.put(descender.getOpenPattern(), (lexer, state, match) -> {
			if (descender.getClosePattern().matcher(match.group()).matches() && state.getDescender() == descender) //This allows descenders with the same open and close patterns to work.
				return descender.getCloseAction().perform(lexer, state, state.getRoot());
			descender.getOpenAction().perform(lexer, state, match);
			LexerState<C, T, R, D, L> descended = state.descend(descender);
			C out = ((Lexer<C, T, R, D, L>) lexer).lex(descended);
			state.setHead(descended.getHead());
			return out;
		});
		patterns.put(descender.getClosePattern(), (AscentBlock<C, T, R, D, L>) (lexer, state, match) -> {
			if (state.getDescender() != descender)
				throw new UnbalancedDescenderException(state);
			return descender.getCloseAction().perform(lexer, state, state.getRoot() == null ? cellConstructor.construct() : state.getRoot());
		});
	}
	
	@Override
	public synchronized D removeDescender(String name) {
		D out = descenders.remove(name);
		if (out == null)
			return out;
		patterns.remove(out.getOpenPattern());
		patterns.remove(out.getClosePattern());
		names.remove(out.getOpenPattern());
		names.remove(out.getClosePattern());
		return out;
	}
	
	@Override
	public D getDescender(String name) {
		return descenders.get(name);
	}
	
	@Override
	public Map<String, D> getDescenders() {
		return descenders;
	}
	
	@Override
	public synchronized void addIgnore(String name, Pattern pattern) {
		if (names.containsKey(pattern))
			throw new PatternCollisionException(pattern, names.get(pattern));
		ignores.put(name, pattern);
		names.put(pattern, name + "::ignore");
		patterns.put(pattern, null);
	}
	
	@Override
	public void addIgnore(DefaultPattern ignore) {
		addIgnore(ignore.getName(), ignore.getPattern());
	}
	
	@Override
	public synchronized Pattern removeIgnore(String name) {
		Pattern out = ignores.remove(name);
		if (out == null)
			return out;
		patterns.remove(out);
		names.remove(out);
		return out;
	}
	
	@Override
	public Pattern removeIgnore(DefaultPattern ignore) {
		return removeIgnore(ignore.getName());
	}
	
	@Override
	public Pattern getIgnore(String name) {
		return ignores.get(name);
	}
	
	@Override
	public Map<String, Pattern> getIgnores() {
		return ignores;
	}
	
	@Override
	public Map<Pattern, String> getNames() {
		return names;
	}
	
	@Override
	public Map<Pattern, Action<C, T, R, D, L, Matcher>> getPatterns() {
		return patterns;
	}
}
