package toberumono.lexer.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.lexer.errors.EmptyInputException;
import toberumono.lexer.errors.LexerException;
import toberumono.lexer.errors.UnrecognizedCharacterException;
import toberumono.lexer.util.DefaultIgnorePatterns;
import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * An implementation of the core components of {@link Lexer}.
 * This represents a generic tokenizer that uses a set of user-defined rules to tokenize a {@link String} input.<br>
 * While this implementation is designed to work with cons-cell esque tokens (e.g. those from Lisp), it can theoretically be
 * modified to work with other structures.
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
public class AbstractLexer<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>>
		implements Lexer<C, T, R, D, L>, Language<C, T, R, D, L> {
	private final Language<C, T, R, D, L> language;
	private final ConsCellConstructor<T, C> cellConstructor;
	private final T emptyType;
	
	/**
	 * Constructs an {@link AbstractLexer} with the provided cell constructor.
	 * 
	 * @param cellConstructor
	 *            a function that takes no arguments and returns a new instance of the class extending
	 *            {@link GenericConsCell}
	 * @param languageConstructor
	 *            a {@link LanguageConstructor} that returns a new instance of the type of {@link Language}
	 *            to be used
	 * @param emptyType
	 *            the {@code Type} that represents an empty (or null) value in the {@code ConsCell} type that this
	 *            {@code Lexer} uses.
	 * @param ignore
	 *            A list of patterns to ignore. The {@link DefaultIgnorePatterns} enum has a few common patterns.
	 * @see DefaultIgnorePatterns
	 */
	public AbstractLexer(ConsCellConstructor<T, C> cellConstructor, LanguageConstructor<C, T, R, D, L> languageConstructor, T emptyType, DefaultPattern... ignore) {
		this(new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), cellConstructor, languageConstructor, emptyType, ignore);
	}
	
	/**
	 * Constructs a {@link AbstractLexer} using the given maps and cell constructor.
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
	 *            a {@link LanguageConstructor} that returns a new instance of the type of {@link Language}
	 *            to be used
	 * @param emptyType
	 *            the {@code Type} that represents an empty (or null) value in the {@code ConsCell} type that this
	 *            {@code Lexer} uses.
	 * @param ignore
	 *            A list of patterns to ignore. The {@link DefaultIgnorePatterns} enum has a few common patterns.
	 * @see DefaultIgnorePatterns
	 */
	public AbstractLexer(Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores, Map<Pattern, Action<C, T, R, D, L, Matcher>> patterns,
			ConsCellConstructor<T, C> cellConstructor, LanguageConstructor<C, T, R, D, L> languageConstructor, T emptyType, DefaultPattern... ignore) {
		this.cellConstructor = cellConstructor;
		this.emptyType = emptyType;
		this.language = languageConstructor.construct(cellConstructor, rules, descenders, ignores, new HashMap<>(), patterns);
		for (DefaultPattern p : ignore)
			this.addIgnore(p);
	}
	
	@Override
	public C lex(String input) throws LexerException {
		return lex(new LexerState<>(input, 0, null, this));
	}
	
	@Override
	public C lex(LexerState<C, T, R, D, L> state) throws LexerException {
		if (state.getHead() >= state.getInput().length())
			throw new EmptyInputException(state);
		for (int lim = state.getInput().length(); state.getHead() < lim;) {
			Matcher longest = null;
			Action<C, T, R, D, L, Matcher> match = null;
			for (Pattern p : state.getLanguage().getPatterns().keySet()) {
				Matcher m = p.matcher(state.getInput());
				if (m.find(state.getHead()) && m.start() == state.getHead() && (longest == null || m.end() > longest.end() ||
						(state.getDescender() != null && m.end() == longest.end() && p == state.getDescender().getClosePattern()))) {
					longest = m;
					match = state.getLanguage().getPatterns().get(p);
				}
			}
			if (longest == null)
				throw new UnrecognizedCharacterException(state);
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
	
	@Override
	public C getNextConsCell(LexerState<C, T, R, D, L> state, boolean advance) throws LexerException {
		if (state.getHead() >= state.getInput().length())
			throw new EmptyInputException(state);
		int initial = state.getHead();
		try {
			if (state.getHead() >= state.getInput().length())
				throw new EmptyInputException(state);
			for (int lim = state.getInput().length(); state.getHead() < lim;) {
				Matcher longest = null;
				Action<C, T, R, D, L, Matcher> match = null;
				for (Pattern p : state.getLanguage().getPatterns().keySet()) {
					Matcher m = p.matcher(state.getInput());
					if (m.find(state.getHead()) && m.start() == state.getHead() && (longest == null || m.end() > longest.end() ||
							(state.getDescender() != null && m.end() == longest.end() && p == state.getDescender().getClosePattern()))) {
						longest = m;
						match = state.getLanguage().getPatterns().get(p);
					}
				}
				if (longest == null)
					throw new UnrecognizedCharacterException(state);
				state.advance(longest);
				if (match == null) //Handle ignores
					continue;
				if (match instanceof AscentBlock) {
					if (longest.pattern() == state.getDescender().getClosePattern()) {
						state.setHead(initial);
						throw new EmptyInputException(state);
					}
					else
						throw new UnrecognizedCharacterException(state);
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
	
	@Override
	public final int skipIgnores(LexerState<C, T, R, D, L> state) {
		Matcher longest = null;
		Action<C, T, R, D, L, Matcher> match = null;
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
	
	@Override
	public void addRule(String name, R rule) {
		language.addRule(name, rule);
	}
	
	@Override
	public R removeRule(String name) {
		return language.removeRule(name);
	}
	
	@Override
	public R getRule(String name) {
		return language.getRule(name);
	}
	
	@Override
	public Map<String, R> getRules() {
		return Collections.unmodifiableMap(language.getRules());
	}
	
	@Override
	public void addDescender(String name, D descender) {
		language.addDescender(name, descender);
	}
	
	@Override
	public D removeDescender(String name) {
		return language.removeDescender(name);
	}
	
	@Override
	public D getDescender(String name) {
		return language.getDescender(name);
	}
	
	@Override
	public Map<String, D> getDescenders() {
		return Collections.unmodifiableMap(language.getDescenders());
	}
	
	@Override
	public void addIgnore(String name, Pattern pattern) {
		language.addIgnore(name, pattern);
	}
	
	@Override
	public void addIgnore(DefaultPattern ignore) {
		language.addIgnore(ignore.getName(), ignore.getPattern());
	}
	
	@Override
	public Pattern removeIgnore(String name) {
		return language.removeIgnore(name);
	}
	
	@Override
	public Pattern removeIgnore(DefaultPattern ignore) {
		return language.removeIgnore(ignore);
	}
	
	@Override
	public Pattern getIgnore(String name) {
		return language.getIgnore(name);
	}
	
	@Override
	public Map<String, Pattern> getIgnores() {
		return Collections.unmodifiableMap(language.getIgnores());
	}
	
	@Override
	public Map<Pattern, Action<C, T, R, D, L, Matcher>> getPatterns() {
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
	
	@Override
	public final ConsCellConstructor<T, C> getConsCellConstructor() {
		return cellConstructor;
	}
	
	@Override
	public Language<C, T, R, D, L> getLanguage() {
		return language;
	}
	
	@Override
	public T getEmptyType() {
		return emptyType;
	}

	@Override
	public Map<Pattern, String> getNames() {
		return language.getNames();
	}
}
