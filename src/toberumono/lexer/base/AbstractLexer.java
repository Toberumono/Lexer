package toberumono.lexer.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.lexer.errors.EmptyInputException;
import toberumono.lexer.errors.LexerException;
import toberumono.lexer.errors.UnrecognizedCharacterException;
import toberumono.lexer.util.DefaultIgnorePatterns;
import toberumono.lexer.util.DefaultPattern;
import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * An implementation of the core components of {@link Lexer}. This represents a generic tokenizer that uses a set of
 * user-defined rules to tokenize a {@link String} input.<br>
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
		implements Lexer<C, T, R, D, L> {
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
	 *            a {@link LanguageConstructor} that returns a new instance of the type of {@link Language} to be used
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
	 *            a {@link LanguageConstructor} that returns a new instance of the type of {@link Language} to be used
	 * @param emptyType
	 *            the {@code Type} that represents an empty (or null) value in the {@code ConsCell} type that this
	 *            {@code Lexer} uses.
	 * @param ignore
	 *            A list of patterns to ignore. The {@link DefaultIgnorePatterns} enum has a few common patterns.
	 * @see DefaultIgnorePatterns
	 */
	public AbstractLexer(Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores, Map<Pattern, LexerAction<C, T, R, D, L, Matcher>> patterns,
			ConsCellConstructor<T, C> cellConstructor, LanguageConstructor<C, T, R, D, L> languageConstructor, T emptyType, DefaultPattern... ignore) {
		this.cellConstructor = cellConstructor;
		this.emptyType = emptyType;
		this.language = languageConstructor.construct(cellConstructor, rules, descenders, ignores, new HashMap<>(), patterns);
		for (DefaultPattern p : ignore)
			this.addIgnore(p);
	}
	
	@Override
	public C lex(String input) throws LexerException {
		@SuppressWarnings("unchecked") //The Lexer is guaranteed to match L
		LexerState<C, T, R, D, L> state = new LexerState<>(input, 0, null, (L) this);
		return lex(state);
	}
	
	@Override
	public C lex(LexerState<C, T, R, D, L> state) throws LexerException {
		if (state.getHead() >= state.getInput().length())
			throw new EmptyInputException(state);
		for (int lim = state.getInput().length(); state.getHead() < lim;) {
			Matcher longest = null;
			LexerAction<C, T, R, D, L, Matcher> match = null;
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
		return out;
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
				LexerAction<C, T, R, D, L, Matcher> match = null;
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
			return out;
		}
		finally {
			if (!advance)
				state.setHead(initial);
		}
	}
	
	@Override
	public final int skipIgnores(LexerState<C, T, R, D, L> state) {
		int head = state.getHead(), longest = head;
		Collection<Pattern> ignores = state.getLanguage().getIgnores().values(); //Avoids chaining through these functions every time
		for (Matcher m = null;;) { //Because head == longest if the loop wasn't broken, we don't need to assign longest to head here
			for (Pattern p : ignores) {
				m = p.matcher(state.getInput());
				if (m.find(head) && m.start() == head && m.end() > longest)
					longest = m.end();
			}
			if (longest > head)
				head = longest;
			else
				break;
		}
		return head - state.setHead(head);
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
}
