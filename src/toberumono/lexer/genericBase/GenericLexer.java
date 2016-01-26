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
import toberumono.lexer.errors.UnrecognizedCharacterException;
import toberumono.lexer.util.DefaultIgnorePatterns;
import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * This represents a generic tokenizer that uses a set of user-defined rules to tokenize a {@link String} input.<br>
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
	private final GenericLanguage<C, T, R, D, L> language;
	private final ConsCellConstructor<T, C> cellConstructor;
	protected final T emptyType;
	
	/**
	 * Constructs a {@link GenericLexer} with the provided cell constructor.
	 * 
	 * @param cellConstructor
	 *            a function that takes no arguments and returns a new instance of the class extending
	 *            {@link GenericConsCell}
	 * @param emptyType
	 *            the {@code Type} that represents an empty (or null) value in the {@code ConsCell} type that this
	 *            {@code Lexer} uses.
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
	 *            {@link GenericConsCell}
	 * @param emptyType
	 *            the {@code Type} that represents an empty (or null) value in the {@code ConsCell} type that this
	 *            {@code Lexer} uses.
	 * @param ignore
	 *            A list of patterns to ignore. The {@link DefaultIgnorePatterns} enum has a few common patterns.
	 * @see DefaultIgnorePatterns
	 */
	public GenericLexer(Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores, Map<Pattern, GenericAction<C, T, R, D, L, Matcher>> patterns,
			ConsCellConstructor<T, C> cellConstructor, T emptyType, DefaultPattern... ignore) {
		this(rules, descenders, ignores, patterns, cellConstructor, GenericLanguage<C, T, R, D, L>::new, emptyType, ignore);
	}
	
	/**
	 * Constructs a {@link GenericLexer} with the provided cell constructor.
	 * 
	 * @param cellConstructor
	 *            a function that takes no arguments and returns a new instance of the class extending
	 *            {@link GenericConsCell}
	 * @param languageConstructor
	 *            a {@link LanguageConstructor} that returns a new instance of the type of {@link GenericLanguage Language} to be used
	 * @param emptyType
	 *            the {@code Type} that represents an empty (or null) value in the {@code ConsCell} type that this
	 *            {@code Lexer} uses.
	 * @param ignore
	 *            A list of patterns to ignore. The {@link DefaultIgnorePatterns} enum has a few common patterns.
	 * @see DefaultIgnorePatterns
	 */
	public GenericLexer(ConsCellConstructor<T, C> cellConstructor, LanguageConstructor<C, T, R, D, L> languageConstructor, T emptyType, DefaultPattern... ignore) {
		this(new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), cellConstructor, languageConstructor, emptyType, ignore);
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
	 *            {@link GenericConsCell}
	 * @param languageConstructor
	 *            a {@link LanguageConstructor} that returns a new instance of the type of {@link GenericLanguage Language} to be used
	 * @param emptyType
	 *            the {@code Type} that represents an empty (or null) value in the {@code ConsCell} type that this
	 *            {@code Lexer} uses.
	 * @param ignore
	 *            A list of patterns to ignore. The {@link DefaultIgnorePatterns} enum has a few common patterns.
	 * @see DefaultIgnorePatterns
	 */
	public GenericLexer(Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores, Map<Pattern, GenericAction<C, T, R, D, L, Matcher>> patterns,
			ConsCellConstructor<T, C> cellConstructor, LanguageConstructor<C, T, R, D, L> languageConstructor, T emptyType, DefaultPattern... ignore) {
		this.cellConstructor = cellConstructor;
		this.emptyType = emptyType;
		this.language = languageConstructor.construct(cellConstructor, rules, descenders, ignores, new HashMap<>(), patterns);
		for (DefaultPattern p : ignore)
			this.addIgnore(p);
	}
	
	/**
	 * Tokenizes a {@code String}
	 * 
	 * @param input
	 *            the {@code String} to tokenize
	 * @return the {@code ConsCell}s in the {@code String}
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
	 * @return the {@code ConsCell}s in the {@code String}
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
			for (Pattern p : state.getLanguage().getPatterns().keySet()) {
				Matcher m = p.matcher(state.getInput());
				if (m.find(state.getHead()) && m.start() == state.getHead() && (longest == null || m.end() > longest.end() ||
						(state.getDescender() != null && m.end() == longest.end() && p == state.getDescender().close))) {
					longest = m;
					match = state.getLanguage().getPatterns().get(p);
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
	 * If {@code advance} is {@code true}, then this <i>will</i> modify {@code state's} head position.
	 * 
	 * @param state
	 *            the {@link LexerState} to use
	 * @param advance
	 *            whether to advance {@code state's} head position
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
				for (Pattern p : state.getLanguage().getPatterns().keySet()) {
					Matcher m = p.matcher(state.getInput());
					if (m.find(state.getHead()) && m.start() == state.getHead() && (longest == null || m.end() > longest.end() ||
							(state.getDescender() != null && m.end() == longest.end() && p == state.getDescender().close))) {
						longest = m;
						match = state.getLanguage().getPatterns().get(p);
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
			for (Pattern p : state.getLanguage().getPatterns().keySet()) {
				Matcher m = p.matcher(state.getInput());
				if (m.find(pos) && m.start() == pos && (longest == null || m.end() > longest.end())) {
					longest = m;
					match = state.getLanguage().getPatterns().get(p);
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
		language.addRule(name, rule);
	}
	
	/**
	 * Removes a rule
	 * 
	 * @param name
	 *            the name of the rule to remove
	 * @return the removed rule if a rule of that name existed, otherwise null
	 */
	public synchronized R removeRule(String name) {
		return language.removeRule(name);
	}
	
	/**
	 * Gets a rule by name
	 * 
	 * @param name
	 *            the name of the rule to get
	 * @return the rule if a rule corresponding to that name is loaded, otherwise null
	 */
	public R getRule(String name) {
		return language.getRule(name);
	}
	
	/**
	 * @return an unmodifiable view of the rules map (this view is backed by the internal map and only needs to be retrieved
	 *         once)
	 */
	public Map<String, R> getRules() {
		return Collections.unmodifiableMap(language.getRules());
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
		language.addDescender(name, descender);
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
		return language.removeDescender(name);
	}
	
	/**
	 * Gets a descender by name.
	 * 
	 * @param name
	 *            the name of the descender to get
	 * @return the descender if a descender corresponding to that name is loaded, otherwise null
	 */
	public D getDescender(String name) {
		return language.getDescender(name);
	}
	
	/**
	 * @return an unmodifiable view of the descenders map (this view is backed by the internal map and only needs to be
	 *         retrieved once)
	 */
	public Map<String, D> getDescenders() {
		return Collections.unmodifiableMap(language.getDescenders());
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
		language.addIgnore(name, pattern);
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
		language.addIgnore(ignore.getName(), ignore.getPattern());
	}
	
	/**
	 * Removes an ignored {@link Pattern}
	 * 
	 * @param name
	 *            the name of the ignored {@link Pattern} to remove
	 * @return the removed {@link Pattern} if a {@link Pattern} of that name existed, otherwise null
	 */
	public synchronized Pattern removeIgnore(String name) {
		return language.removeIgnore(name);
	}
	
	/**
	 * Removes the {@link DefaultPattern} from the lexer.
	 * 
	 * @param ignore
	 *            the {@link DefaultPattern} to remove
	 * @return the {@link Pattern} that was being ignored if it was loaded in the lexer, otherwise null
	 */
	public Pattern removeIgnore(DefaultPattern ignore) {
		return language.removeIgnore(ignore);
	}
	
	/**
	 * Gets an ignored {@link Pattern} by name
	 * 
	 * @param name
	 *            the name of the ignored {@link Pattern} to get
	 * @return the ignored {@link Pattern} if one corresponding to that name is loaded, otherwise null
	 */
	public Pattern getIgnore(String name) {
		return language.getIgnore(name);
	}
	
	/**
	 * @return an unmodifiable view of the ignores map (this view is backed by the internal map and only needs to be
	 *         retrieved once)
	 */
	public Map<String, Pattern> getIgnores() {
		return Collections.unmodifiableMap(language.getIgnores());
	}
	
	/**
	 * The patterns map that is used in the actual lexing loop. This is mainly for internal use.
	 * 
	 * @return an unmodifiable view of the patterns map (this view is backed by the internal map and only needs to be
	 *         retrieved once)
	 */
	public Map<Pattern, GenericAction<C, T, R, D, L, Matcher>> getPatterns() {
		return Collections.unmodifiableMap(language.getPatterns());
	}
	
	/**
	 * Tells the lexer to skip over the {@link Pattern} in the given regex {@code String}.<br>
	 * This now just forwards to {@link #addIgnore(String, Pattern)}. Use it instead.
	 * 
	 * @param name
	 *            the name with which to reference this ignore pattern
	 * @param ignore
	 *            the {@link Pattern} to ignore
	 */
	@Deprecated
	public final void ignore(String name, Pattern ignore) {
		language.addIgnore(name, ignore);
	}
	
	/**
	 * @return the cell constructor being used by this {@link GenericLexer}
	 */
	public final ConsCellConstructor<T, C> getConsCellConstructor() {
		return cellConstructor;
	}
	
	/**
	 * @return the default {@link GenericLanguage Language} for this {@link GenericLexer}
	 */
	public GenericLanguage<C, T, R, D, L> getLanguage() {
		return language;
	}
}
