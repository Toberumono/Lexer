package toberumono.lexer.genericBase;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.lexer.errors.EmptyInputException;
import toberumono.lexer.errors.LexerException;
import toberumono.lexer.errors.PatternCollisionException;
import toberumono.lexer.errors.UnbalancedDescenderException;
import toberumono.lexer.errors.UnrecognizedCharacterException;
import toberumono.lexer.util.DefaultIgnorePatterns;
import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * This represents a generic tokenizer that uses a set of user-defined rules to a {@link String} input.<br>
 * While this implementation is designed to work with cons-cell esque cells (e.g. those from Lisp), it can theoretically be
 * modified to work with other structures.
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
public class GenericLexer<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends GenericRule<C, T, R, D, L>, D extends GenericDescender<C, T, R, D, L>, L extends GenericLexer<C, T, R, D, L>> {
	private final Map<String, R> rules, unmodifiableRules;
	private final Map<String, D> descenders, unmodifiableDescenders;
	private final Map<String, Pattern> ignores, unmodifiableIgnores;
	private final Map<Pattern, String> names = new HashMap<>();
	private final Map<Pattern, GenericAction<C, T, R, D, L, Matcher>> patterns, unmodifiablePatterns;
	private final ConsCellConstructor<T, C> cellConstructor;
	protected final T emptyType;
	
	/**
	 * Constructs a {@link GenericLexer} with the provided cell constructor.
	 * 
	 * @param cellConstructor
	 *            a function that takes no arguments and returns a new instance of the class extending
	 *            {@link GenericConsCell}.
	 * @param emptyType
	 *            the <tt>Type</tt> that represents an empty (or null) value in the <tt>ConsCell</tt> type that this
	 *            <tt>Lexer</tt> uses.
	 * @param ignore
	 *            A list of patterns to ignore. The {@link DefaultIgnorePatterns} enum has a few common patterns.
	 * @see DefaultIgnorePatterns
	 */
	public GenericLexer(ConsCellConstructor<T, C> cellConstructor, T emptyType, DefaultPattern... ignore) {
		this(new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), cellConstructor, emptyType, ignore);
	}
	
	/**
	 * Constructs a {@link GenericLexer} using the given maps and cell constructor.
	 * 
	 * @param rules
	 *            the {@link Map} in which to store rules
	 * @param descenders
	 *            the {@link Map} in which to store descenders
	 * @param ignores
	 *            the {@link Map} in which to store ignores
	 * @param patterns
	 *            the {@link Map} in which to store the active patterns
	 * @param cellConstructor
	 *            a function that takes no arguments and returns a new instance of the class extending
	 *            {@link GenericConsCell}.
	 * @param emptyType
	 *            the <tt>Type</tt> that represents an empty (or null) value in the <tt>ConsCell</tt> type that this
	 *            <tt>Lexer</tt> uses.
	 * @param ignore
	 *            A list of patterns to ignore. The {@link DefaultIgnorePatterns} enum has a few common patterns.
	 * @see DefaultIgnorePatterns
	 */
	public GenericLexer(Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores, Map<Pattern, GenericAction<C, T, R, D, L, Matcher>> patterns,
			ConsCellConstructor<T, C> cellConstructor,
			T emptyType, DefaultPattern... ignore) {
		this.rules = rules;
		this.unmodifiableRules = Collections.unmodifiableMap(this.rules);
		this.descenders = descenders;
		this.unmodifiableDescenders = Collections.unmodifiableMap(this.descenders);
		this.ignores = ignores;
		this.unmodifiableIgnores = Collections.unmodifiableMap(this.ignores);
		this.cellConstructor = cellConstructor;
		this.emptyType = emptyType;
		this.patterns = patterns;
		this.unmodifiablePatterns = Collections.unmodifiableMap(this.patterns);
		for (DefaultPattern p : ignore)
			this.addIgnore(p);
	}
	
	/**
	 * Tokenizes a <tt>String</tt>
	 * 
	 * @param input
	 *            the <tt>String</tt> to tokenize
	 * @return the <tt>ConsCell</tt>s in the <tt>String</tt>
	 * @throws LexerException
	 *             so that lexer exceptions can be propagated back to the original caller
	 * @see #lex(LexerState)
	 */
	public C lex(String input) throws LexerException {
		return lex(new LexerState<>(input, 0, null, this));
	}
	
	/**
	 * Processes the given {@link LexerState State}.<br>
	 * Use {@link #lex(String)} to tokenize an input from the beginning.
	 * 
	 * @param state
	 *            the {@link LexerState} to process
	 * @return the <tt>ConsCell</tt>s in the <tt>String</tt>
	 * @throws LexerException
	 *             so that lexer exceptions can be propagated back to the original caller
	 * @see #lex(String)
	 */
	public C lex(LexerState<C, T, R, D, L> state) throws LexerException {
		if (state.getHead() >= state.getInput().length())
			throw new EmptyInputException();
		for (int lim = state.getInput().length(); state.getHead() < lim;) {
			Matcher longest = null;
			GenericAction<C, T, R, D, L, Matcher> match = null;
			for (Pattern p : patterns.keySet()) {
				Matcher m = p.matcher(state.getInput());
				if (m.find(state.getHead()) && m.start() == state.getHead() && (longest == null || m.end() > longest.end() ||
						(state.getDescender() != null && m.end() == longest.end() && p == state.getDescender().close))) {
					longest = m;
					match = patterns.get(p);
				}
			}
			if (longest == null)
				throw new UnrecognizedCharacterException(state.getInput(), state.getHead());
			state.advance(longest);
			if (match == null) //Handle ignores
				continue;
			@SuppressWarnings("unchecked")
			C cell = match.perform((L) this, state, longest);
			if (match instanceof AscentBlock)
				return cell;
			if (cell != null)
				state.appendMatch(cell);
		}
		C out = state.getRoot();
		return out == null ? cellConstructor.construct() : out;
	}
	
	/**
	 * Gets the next {@link GenericConsCell ConsCell} using the given {@link LexerState State}.<br>
	 * This will throw an {@link EmptyInputException} if it encounters a close cell.<br>
	 * If <tt>advance</tt> is {@code true}, then this <i>will</i> modify <tt>state's</tt> head position.
	 * 
	 * @param state
	 *            the {@link LexerState} to use
	 * @param advance
	 *            whether to advance <tt>state's</tt> head position
	 * @return the next {@link GenericConsCell ConsCell} in the input
	 * @throws LexerException
	 *             so that lexer exceptions can be propagated back to the original caller
	 */
	public C getNextConsCell(LexerState<C, T, R, D, L> state, boolean advance) throws LexerException {
		if (state.getHead() >= state.getInput().length())
			throw new EmptyInputException();
		int initial = state.getHead();
		try {
			if (state.getHead() >= state.getInput().length())
				throw new EmptyInputException();
			for (int lim = state.getInput().length(); state.getHead() < lim;) {
				Matcher longest = null;
				GenericAction<C, T, R, D, L, Matcher> match = null;
				for (Pattern p : patterns.keySet()) {
					Matcher m = p.matcher(state.getInput());
					if (m.find(state.getHead()) && m.start() == state.getHead() && (longest == null || m.end() > longest.end() ||
							(state.getDescender() != null && m.end() == longest.end() && p == state.getDescender().close))) {
						longest = m;
						match = patterns.get(p);
					}
				}
				if (longest == null)
					throw new UnrecognizedCharacterException(state.getInput(), state.getHead());
				state.advance(longest);
				if (match == null) //Handle ignores
					continue;
				if (match instanceof AscentBlock) {
					if (longest.pattern() == state.getDescender().close) {
						state.setHead(initial);
						throw new EmptyInputException();
					}
					else
						throw new UnrecognizedCharacterException(state.getInput(), state.getHead());
				}
				@SuppressWarnings("unchecked")
				C cell = match.perform((L) this, state, longest);
				return cell;
			}
			C out = state.getRoot();
			return out == null ? cellConstructor.construct() : out;
		}
		finally {
			if (!advance)
				state.setHead(initial);
		}
	}
	
	/**
	 * Skips over cells that are set to be ignored.<br>
	 * This <i>does</i> modify the passed {@link LexerState}.
	 * 
	 * @return the number of characters that were skipped
	 */
	final int skipIgnores(LexerState<C, T, R, D, L> state) {
		Matcher longest = null;
		GenericAction<C, T, R, D, L, Matcher> match = null;
		int pos = state.getHead();
		while (true) {
			longest = null;
			match = null;
			for (Pattern p : patterns.keySet()) {
				Matcher m = p.matcher(state.getInput());
				if (m.find(pos) && m.start() == pos && (longest == null || m.end() > longest.end())) {
					longest = m;
					match = patterns.get(p);
				}
			}
			if (longest != null && match == null)
				pos = longest.end();
			else
				break;
		}
		int oldHead = state.getHead();
		state.setHead(pos);
		return pos - oldHead;
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
	 * @return an unmodifiable view of the rules map (this view is backed by the internal map and only needs to be retrieved
	 *         once)
	 */
	public Map<String, R> getRules() {
		return unmodifiableRules;
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
	 * @return an unmodifiable view of the descenders map (this view is backed by the internal map and only needs to be
	 *         retrieved once)
	 */
	public Map<String, D> getDescenders() {
		return unmodifiableDescenders;
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
	 * @return an unmodifiable view of the ignores map (this view is backed by the internal map and only needs to be
	 *         retrieved once)
	 */
	public Map<String, Pattern> getIgnores() {
		return unmodifiableIgnores;
	}
	
	/**
	 * The patterns map that is used in the actual lexing loop. This is mainly for internal use.
	 * 
	 * @return an unmodifiable view of the patterns map (this view is backed by the internal map and only needs to be
	 *         retrieved once)
	 */
	public Map<Pattern, GenericAction<C, T, R, D, L, Matcher>> getPatterns() {
		return unmodifiablePatterns;
	}
	
	/**
	 * Tells the lexer to skip over the <tt>Pattern</tt> in the given regex <tt>String</tt>.<br>
	 * This now just forwards to {@link #addIgnore(String, Pattern)}. Use it instead.
	 * 
	 * @param name
	 *            the name with which to reference this ignore pattern
	 * @param ignore
	 *            the <tt>Pattern</tt> to ignore
	 */
	@Deprecated
	public final void ignore(String name, Pattern ignore) {
		addIgnore(name, ignore);
	}
	
	/**
	 * @return the cell constructor being used by this {@link GenericLexer}
	 */
	public final ConsCellConstructor<T, C> getConsCellConstructor() {
		return cellConstructor;
	}
}
