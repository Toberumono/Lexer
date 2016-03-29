package toberumono.lexer.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.lexer.errors.PatternCollisionException;
import toberumono.lexer.errors.UnbalancedDescenderException;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;
import toberumono.structures.sexpressions.GenericConsCell;

/**
 * An implementation of the core components of {@link Language}. This represents a language that can be used by a
 * {@link Lexer} to tokenize an input {@link String}
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
public abstract class AbstractLanguage<C extends GenericConsCell<C, T>, T extends ConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>>
		implements Language<C, T, R, D, L>, Cloneable {
	private Map<String, R> rules;
	private Map<String, D> descenders;
	private Map<String, Pattern> ignores;
	private Map<Pattern, String> names;
	private Map<Pattern, LexerAction<C, T, R, D, L, MatchResult>> patterns;
	private final BiFunction<Map<?, ?>, String, Map<?, ?>> cloner;
	
	/**
	 * Constructs an empty {@link AbstractLanguage}
	 */
	public AbstractLanguage() {
		this(new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), (m, e) -> (Map<?, ?>) ((LinkedHashMap<?, ?>) m).clone());
	}
	
	/**
	 * Constructs an {@link AbstractLanguage} with the given data {@link Map Maps}<br>
	 * This is provided as a convenience constructor with a faster cloning method for instances of {@link AbstractLanguage}
	 * that use instances of {@link HashMap} for the internal {@link Map Maps}
	 * 
	 * @param rules
	 *            a {@link Map} containing the {@link Rule Rules}
	 * @param descenders
	 *            a {@link Map} containing the {@link Descender Descenders}
	 * @param ignores
	 *            a {@link Map} containing the {@link Pattern Patterns} to ignore
	 * @param names
	 *            a {@link Map} containing the names that are in use
	 * @param patterns
	 *            a {@link Map} that maps {@link Pattern Patterns} to their associated {@link LexerAction LexerActions}
	 */
	public AbstractLanguage(HashMap<String, R> rules, HashMap<String, D> descenders, HashMap<String, Pattern> ignores, HashMap<Pattern, String> names,
			HashMap<Pattern, LexerAction<C, T, R, D, L, MatchResult>> patterns) {
		this(rules, descenders, ignores, names, patterns, (m, e) -> (Map<?, ?>) ((HashMap<?, ?>) m).clone());
	}
	
	/**
	 * Constructs an {@link AbstractLanguage} with the given data {@link Map Maps}<br>
	 * <b>Note:</b> the {@link Map Maps} are <i>not</i> copied in the constructor<br>
	 * <b>Note:</b> if any of the {@link Map Maps} do not meet the requirements specified in {@link #clone()}, the
	 * {@link #AbstractLanguage(Map, Map, Map, Map, Map, BiFunction)} constructor should be used instead
	 * 
	 * @param rules
	 *            a {@link Map} containing the {@link Rule Rules}
	 * @param descenders
	 *            a {@link Map} containing the {@link Descender Descenders}
	 * @param ignores
	 *            a {@link Map} containing the {@link Pattern Patterns} to ignore
	 * @param names
	 *            a {@link Map} containing the names that are in use
	 * @param patterns
	 *            a {@link Map} that maps {@link Pattern Patterns} to their associated {@link LexerAction LexerActions}
	 */
	public AbstractLanguage(Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores, Map<Pattern, String> names,
			Map<Pattern, LexerAction<C, T, R, D, L, MatchResult>> patterns) {
		this(rules, descenders, ignores, names, patterns, AbstractLanguage::tryClone);
	}
	
	/**
	 * Constructs an {@link AbstractLanguage} with the given data {@link Map Maps}<br>
	 * <b>Note:</b> the {@link Map Maps} are <i>not</i> copied in the constructor<br>
	 * <b>Requirements for the {@code cloner} {@link BiFunction}</b>
	 * <ol>
	 * <li>The {@link BiFunction BiFunction's} first argument is the {@link Map} being cloned</li>
	 * <li>The {@link BiFunction BiFunction's} second argument is the name of the {@link Map} being cloned (i.e. rules,
	 * descenders, etc.)</li>
	 * <li>The {@link BiFunction} <i>must</i> return a {@link Map} with the same types as the input {@link Map} for casting
	 * purposes</li>
	 * </ol>
	 * 
	 * @param rules
	 *            a {@link Map} containing the {@link Rule Rules}
	 * @param descenders
	 *            a {@link Map} containing the {@link Descender Descenders}
	 * @param ignores
	 *            a {@link Map} containing the {@link Pattern Patterns} to ignore
	 * @param names
	 *            a {@link Map} containing the names that are in use
	 * @param patterns
	 *            a {@link Map} that maps {@link Pattern Patterns} to their associated {@link LexerAction LexerActions}
	 * @param cloner
	 *            the {@link BiFunction} to be used to clone the internal {@link Map Maps}. It must meet the requirements
	 *            specified above
	 */
	public AbstractLanguage(Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores, Map<Pattern, String> names,
			Map<Pattern, LexerAction<C, T, R, D, L, MatchResult>> patterns, BiFunction<Map<?, ?>, String, Map<?, ?>> cloner) {
		this.rules = Objects.requireNonNull(rules, "The rules map cannot be null.");
		this.descenders = Objects.requireNonNull(descenders, "The descenders map cannot be null.");
		this.ignores = Objects.requireNonNull(ignores, "The ignores map cannot be null.");
		this.names = Objects.requireNonNull(names, "The names map cannot be null.");
		this.patterns = Objects.requireNonNull(patterns, "The patterns map cannot be null.");
		this.cloner = cloner;
	}
	
	@Override
	public synchronized void addRule(String name, R rule) {
		if (names.containsKey(rule.getPattern()))
			throw new PatternCollisionException(rule.getPattern(), names.get(rule.getPattern()));
		rules.put(name, rule);
		names.put(rule.getPattern(), name + "::rule");
		patterns.put(rule.getPattern(), rule.getAction());
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
			return descender.getCloseAction().perform(lexer, state, state.getRoot());
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
	public synchronized Pattern removeIgnore(String name) {
		Pattern out = ignores.remove(name);
		if (out == null)
			return out;
		patterns.remove(out);
		names.remove(out);
		return out;
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
	public Map<Pattern, LexerAction<C, T, R, D, L, MatchResult>> getPatterns() {
		return patterns;
	}
	
	/**
	 * <b>Note:</b> If any the internal {@link Map Maps} do not meet at least one of the following requirements, this method
	 * will fail.
	 * <ol>
	 * <li>Implements {@link Cloneable} and has a publicly-visible {@code clone} method</li>
	 * <li>Has a publicly-visible copy constructor (a constructor that takes an instance of the class as an argument and
	 * produces a shallow copy of the given instance)</li>
	 * </ol>
	 * {@inheritDoc}
	 * 
	 * @throws InternalError
	 *             if any of the internal {@link Map Maps} do not meet at least one of the listed requirements
	 */
	@Override
	public AbstractLanguage<C, T, R, D, L> clone() {
		return clone(cloner);
	}
	
	/**
	 * Attempts to clone the {@link Language} using the given {@link BiFunction} to clone the internal maps<br>
	 * <b>Requirements for the {@code cloner} {@link BiFunction}</b>
	 * <ol>
	 * <li>The {@link BiFunction BiFunction's} first argument is the {@link Map} being cloned</li>
	 * <li>The {@link BiFunction BiFunction's} second argument is the name of the {@link Map} being cloned (i.e. rules,
	 * descenders, etc.)</li>
	 * <li>The {@link BiFunction} <i>must</i> return a {@link Map} with the same types as the input {@link Map} for casting
	 * purposes</li>
	 * </ol>
	 * 
	 * @param cloner
	 *            the {@link BiFunction} to be used to clone the internal {@link Map Maps}. It must meet the requirements
	 *            specified above
	 * @return a clone of the {@link Language} with logically separate internal {@link Map Maps}
	 */
	@SuppressWarnings("unchecked")
	public AbstractLanguage<C, T, R, D, L> clone(BiFunction<Map<?, ?>, String, Map<?, ?>> cloner) {
		try {
			AbstractLanguage<C, T, R, D, L> clone = (AbstractLanguage<C, T, R, D, L>) super.clone();
			clone.rules = (Map<String, R>) cloner.apply(clone.rules, "rules");
			clone.descenders = (Map<String, D>) cloner.apply(clone.descenders, "descenders");
			clone.ignores = (Map<String, Pattern>) cloner.apply(clone.ignores, "ignores");
			clone.names = (Map<Pattern, String>) cloner.apply(clone.names, "names");
			clone.patterns = (Map<Pattern, LexerAction<C, T, R, D, L, MatchResult>>) cloner.apply(clone.patterns, "patterns");
			return clone;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e); //This shouldn't happen because we are Cloneable
		}
	}
	
	private static Map<?, ?> tryClone(Map<?, ?> map, String fieldName) {
		if (map == null)
			return map;
		if (map instanceof HashMap)
			return (Map<?, ?>) ((HashMap<?, ?>) map).clone();
		if (map instanceof Cloneable) //If it is cloneable, try to call the clone method
			try {
				Method clone = map.getClass().getMethod("clone");
				clone.setAccessible(true);
				return (Map<?, ?>) clone.invoke(map);
			}
			catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException
					| IllegalArgumentException | ExceptionInInitializerError e) {/* This is not going to print anything because if the clone method isn't found, we just won't call it. */}
		try {
			Constructor<?> copy = map.getClass().getConstructor(map.getClass()); //Otherwise, check if it has a public copy constructor
			copy.setAccessible(true);
			return (Map<?, ?>) copy.newInstance(map);
		}
		catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ExceptionInInitializerError e) {/* This is not going to print anything because if the copy constructor isn't found, we just won't call it. */}
		throw new InternalError("Unable to clone the " + fieldName + " map.");
	}
}
