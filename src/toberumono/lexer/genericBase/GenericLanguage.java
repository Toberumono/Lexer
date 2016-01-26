package toberumono.lexer.genericBase;

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
 * This represents a language that can be used by a {@link GenericLexer} to tokenize an input {@link String}
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
public class GenericLanguage<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends GenericRule<C, T, R, D, L>, D extends GenericDescender<C, T, R, D, L>, L extends GenericLexer<C, T, R, D, L>> {
	private final Map<String, R> rules;
	private final Map<String, D> descenders;
	private final Map<String, Pattern> ignores;
	private final Map<Pattern, String> names;
	private final Map<Pattern, GenericAction<C, T, R, D, L, Matcher>> patterns;
	private final ConsCellConstructor<T, C> cellConstructor;
	
	/**
	 * Constructs an empty {@link GenericLanguage} with the given {@link ConsCellConstructor}
	 * 
	 * @param cellConstructor
	 *            the {@link ConsCellConstructor} to be used
	 */
	public GenericLanguage(ConsCellConstructor<T, C> cellConstructor) {
		this(cellConstructor, new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
	}
	
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
	 */
	public GenericLanguage(ConsCellConstructor<T, C> cellConstructor, Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores, Map<Pattern, String> names,
			Map<Pattern, GenericAction<C, T, R, D, L, Matcher>> patterns) {
		this.cellConstructor = cellConstructor;
		this.rules = rules;
		this.descenders = descenders;
		this.ignores = ignores;
		this.names = names;
		this.patterns = patterns;
	}
	
	/**
	 * Adds a new rule
	 * 
	 * @param name
	 *            the name of the rule
	 * @param rule
	 *            the rule
	 * @throws PatternCollisionException
	 *             if a {@link Pattern} being added is already loaded
	 */
	public synchronized void addRule(String name, R rule) {
		if (names.containsKey(rule.pattern))
			throw new PatternCollisionException(rule.pattern, names.get(rule.pattern));
		rules.put(name, rule);
		names.put(rule.pattern, name + "::rule");
		patterns.put(rule.pattern, rule.action::perform);
	}
	
	/**
	 * Removes a rule
	 * 
	 * @param name
	 *            the name of the rule to remove
	 * @return the removed rule if a rule of that name existed, otherwise null
	 */
	public synchronized R removeRule(String name) {
		R out = rules.remove(name);
		if (out == null)
			return out;
		patterns.remove(out.pattern);
		names.remove(out.pattern);
		return out;
	}
	
	/**
	 * Gets a rule by name
	 * 
	 * @param name
	 *            the name of the rule to get
	 * @return the rule if a rule corresponding to that name is loaded, otherwise null
	 */
	public R getRule(String name) {
		return rules.get(name);
	}
	
	/**
	 * @return the {@link GenericRule Rules} in the {@link GenericLanguage Language}
	 */
	public Map<String, R> getRules() {
		return rules;
	}
	
	/**
	 * Adds a new {@link GenericDescender Descender}.
	 * 
	 * @param name
	 *            the name of the {@link GenericDescender Descender}
	 * @param descender
	 *            the {@link GenericDescender Descender}
	 * @throws PatternCollisionException
	 *             if a {@link Pattern} being added is already loaded
	 */
	public synchronized void addDescender(String name, D descender) {
		if (names.containsKey(descender.open))
			throw new PatternCollisionException(descender.open, names.get(descender.open));
		if (names.containsKey(descender.close))
			throw new PatternCollisionException(descender.close, names.get(descender.close));
		descenders.put(name, descender);
		names.put(descender.open, name + "::descender.open");
		names.put(descender.close, name + "::descender.close");
		patterns.put(descender.open, (lexer, state, match) -> {
			if (descender.close.matcher(match.group()).matches() && state.getDescender() == descender) //This allows descenders with the same open and close patterns to work.
				return descender.closeAction.perform(lexer, state, state.getRoot());
			descender.openAction.perform(lexer, state, match);
			LexerState<C, T, R, D, L> descended = state.descend(descender);
			C out = ((GenericLexer<C, T, R, D, L>) lexer).lex(descended);
			state.setHead(descended.getHead());
			return out;
		});
		patterns.put(descender.close, (AscentBlock<C, T, R, D, L>) (lexer, state, match) -> {
			if (state.getDescender() != descender)
				throw new UnbalancedDescenderException(state.getInput(), state.getHead());
			C root = state.getRoot();
			return descender.closeAction.perform(lexer, state, root == null ? cellConstructor.construct() : root);
		});
	}
	
	/**
	 * Removes a {@link GenericDescender Descender}
	 * 
	 * @param name
	 *            the name of the {@link GenericDescender Descender} to remove
	 * @return the removed {@link GenericDescender Descender} if a {@link GenericDescender Descender} of that name existed,
	 *         otherwise {@code null}
	 */
	public synchronized D removeDescender(String name) {
		D out = descenders.remove(name);
		if (out == null)
			return out;
		patterns.remove(out.open);
		patterns.remove(out.close);
		names.remove(out.open);
		names.remove(out.close);
		return out;
	}
	
	/**
	 * Gets a descender by name.
	 * 
	 * @param name
	 *            the name of the descender to get
	 * @return the descender if a descender corresponding to that name is loaded, otherwise null
	 */
	public D getDescender(String name) {
		return descenders.get(name);
	}
	
	/**
	 * @return the {@link GenericDescender Descender} in the {@link GenericLanguage Language}
	 */
	public Map<String, D> getDescenders() {
		return descenders;
	}
	
	/**
	 * Adds a new {@link Pattern} that the lexer should recognize but not do anything with (in other words, ignore).
	 * 
	 * @param name
	 *            the name with which to reference this ignore {@link Pattern}
	 * @param pattern
	 *            the {@link Pattern} to ignore
	 * @throws PatternCollisionException
	 *             if a {@link Pattern} being added is already loaded
	 */
	public synchronized void addIgnore(String name, Pattern pattern) {
		if (names.containsKey(pattern))
			throw new PatternCollisionException(pattern, names.get(pattern));
		ignores.put(name, pattern);
		names.put(pattern, name + "::ignore");
		patterns.put(pattern, null);
	}
	
	/**
	 * Adds the {@link DefaultPattern} to the lexer.
	 * 
	 * @param ignore
	 *            the {@link DefaultPattern} to add
	 * @throws PatternCollisionException
	 *             if a {@link Pattern} being added is already loaded
	 */
	public void addIgnore(DefaultPattern ignore) {
		addIgnore(ignore.getName(), ignore.getPattern());
	}
	
	/**
	 * Removes an ignored {@link Pattern}
	 * 
	 * @param name
	 *            the name of the ignored {@link Pattern} to remove
	 * @return the removed {@link Pattern} if a {@link Pattern} of that name existed, otherwise null
	 */
	public synchronized Pattern removeIgnore(String name) {
		Pattern out = ignores.remove(name);
		if (out == null)
			return out;
		patterns.remove(out);
		names.remove(out);
		return out;
	}
	
	/**
	 * Removes the {@link DefaultPattern} from the lexer.
	 * 
	 * @param ignore
	 *            the {@link DefaultPattern} to remove
	 * @return the {@link Pattern} that was being ignored if it was loaded in the lexer, otherwise null
	 */
	public Pattern removeIgnore(DefaultPattern ignore) {
		return removeIgnore(ignore.getName());
	}
	
	/**
	 * Gets an ignored {@link Pattern} by name
	 * 
	 * @param name
	 *            the name of the ignored {@link Pattern} to get
	 * @return the ignored {@link Pattern} if one corresponding to that name is loaded, otherwise null
	 */
	public Pattern getIgnore(String name) {
		return ignores.get(name);
	}
	
	/**
	 * @return the map containing pattern that are ignored in the {@link GenericLanguage Language}
	 */
	public Map<String, Pattern> getIgnores() {
		return ignores;
	}
	
	/**
	 * @return the names used by {@link GenericRule Rules}, {@link GenericDescender Descenders}, and ignores in the
	 *         {@link GenericLanguage Language}
	 */
	public Map<Pattern, String> getNames() {
		return names;
	}
	
	/**
	 * @return the a mapping {@link Pattern} to {@link GenericAction} to perform when the {@link Pattern} is matched
	 */
	public Map<Pattern, GenericAction<C, T, R, D, L, Matcher>> getPatterns() {
		return patterns;
	}
}
