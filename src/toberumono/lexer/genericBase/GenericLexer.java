package toberumono.lexer.genericBase;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.lexer.DefaultIgnorePatterns;
import toberumono.lexer.IgnorePattern;
import toberumono.lexer.errors.EmptyInputException;
import toberumono.lexer.errors.LexerException;
import toberumono.lexer.errors.PatternCollisionException;
import toberumono.lexer.errors.UnbalancedDescenderException;
import toberumono.lexer.errors.UnrecognizedCharacterException;

/**
 * This represents a generic tokenizer that uses a set of user-defined rules to a {@link String} input.<br>
 * While this implementation is designed to work with cons-cell esque tokens (e.g. those from Lisp), it can theoretically be
 * modified to work with other structures.
 * 
 * @author Toberumono
 * @param <To>
 *            the implementation of {@link GenericToken} to be used
 * @param <Ty>
 *            the implementation of {@link GenericType} to be used
 * @param <R>
 *            the implementation of {@link GenericRule} to be used
 * @param <D>
 *            the implementation of {@link GenericDescender} to be used
 * @param <L>
 *            the implementation of {@link GenericLexer} to be used
 */
public class GenericLexer<To extends GenericToken<Ty, To>, Ty extends GenericType, R extends GenericRule<To, Ty, R, D, L>, D extends GenericDescender<To, Ty, R, D, L>, L extends GenericLexer<To, Ty, R, D, L>> {
	private final Map<String, R> rules, unmodifiableRules;
	private final Map<String, D> descenders, unmodifiableDescenders;
	private final Map<String, Pattern> ignores, unmodifiableIgnores;
	private final Map<Pattern, String> names = new HashMap<>();
	private final Map<Pattern, LogicBlock<To, Ty, R, D, L>> patterns = new LinkedHashMap<>(), unmodifiablePatterns = Collections.unmodifiableMap(patterns);
	private final TokenConstructor<Ty, To> tokenConstructor;
	protected final Ty emptyType;
	
	/**
	 * Constructs a {@link GenericLexer} with the provided token constructor.
	 * 
	 * @param tokenConstructor
	 *            a function that takes no arguments and returns a new instance of the class extending {@link GenericToken}.
	 * @param emptyType
	 *            the <tt>Type</tt> that represents an empty (or null) value in the <tt>Token</tt> type that this
	 *            <tt>Lexer</tt> uses.
	 * @param ignore
	 *            A list of patterns to ignore. The {@link DefaultIgnorePatterns} enum has a few common patterns.
	 * @see DefaultIgnorePatterns
	 */
	public GenericLexer(TokenConstructor<Ty, To> tokenConstructor, Ty emptyType, IgnorePattern... ignore) {
		this(new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), tokenConstructor, emptyType, ignore);
	}
	
	/**
	 * Constructs a {@link GenericLexer} using the given maps and token constructor.
	 * 
	 * @param rules
	 *            the {@link Map} in which to store rules
	 * @param descenders
	 *            the {@link Map} in which to store descenders
	 * @param ignores
	 *            the {@link Map} in which to store ignores
	 * @param tokenConstructor
	 *            a function that takes no arguments and returns a new instance of the class extending {@link GenericToken}.
	 * @param emptyType
	 *            the <tt>Type</tt> that represents an empty (or null) value in the <tt>Token</tt> type that this
	 *            <tt>Lexer</tt> uses.
	 * @param ignore
	 *            A list of patterns to ignore. The {@link DefaultIgnorePatterns} enum has a few common patterns.
	 * @see DefaultIgnorePatterns
	 */
	public GenericLexer(Map<String, R> rules, Map<String, D> descenders, Map<String, Pattern> ignores, TokenConstructor<Ty, To> tokenConstructor, Ty emptyType, IgnorePattern... ignore) {
		this.rules = rules;
		this.unmodifiableRules = Collections.unmodifiableMap(this.rules);
		this.descenders = descenders;
		this.unmodifiableDescenders = Collections.unmodifiableMap(this.descenders);
		this.ignores = ignores;
		this.unmodifiableIgnores = Collections.unmodifiableMap(this.ignores);
		this.tokenConstructor = tokenConstructor;
		this.emptyType = emptyType;
		for (IgnorePattern p : ignore)
			this.addIgnore(p);
	}
	
	/**
	 * Tokenizes a <tt>String</tt>
	 * 
	 * @param input
	 *            the <tt>String</tt> to tokenize
	 * @return the <tt>Token</tt>s in the <tt>String</tt>
	 * @throws LexerException
	 *             so that lexer exceptions can be propagated back to the original caller
	 * @see #lex(LexerState)
	 */
	public To lex(String input) throws LexerException {
		return lex(new LexerState<>(input, 0, null, this));
	}
	
	/**
	 * Processes the given {@link LexerState State}.<br>
	 * Use {@link #lex(String)} to tokenize an input from the beginning.
	 * 
	 * @param state
	 *            the {@link LexerState} to process
	 * @return the <tt>Token</tt>s in the <tt>String</tt>
	 * @throws LexerException
	 *             so that lexer exceptions can be propagated back to the original caller
	 * @see #lex(String)
	 */
	public To lex(LexerState<To, Ty, R, D, L> state) throws LexerException {
		if (state.getHead() >= state.getInput().length())
			throw new EmptyInputException();
		for (int lim = state.getInput().length(); state.getHead() < lim;) {
			Matcher longest = null;
			LogicBlock<To, Ty, R, D, L> match = null;
			for (Pattern p : patterns.keySet()) {
				Matcher m = p.matcher(state.getInput());
				if (m.find(state.getHead()) && m.start() == state.getHead() && (longest == null || m.end() > longest.end())) {
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
			To token = match.handle((L) this, state, longest);
			if (match instanceof AscentBlock)
				return token;
			if (token != null)
				state.appendMatch(token);
		}
		To out = state.getRoot();
		return out == null ? tokenConstructor.construct() : out;
	}
	
	/**
	 * Gets the next {@link GenericToken Token} using the given {@link LexerState State}.<br>
	 * This will throw an {@link EmptyInputException} if it encounters a close token.<br>
	 * If <tt>advance</tt> is {@code true}, then this <i>will</i> modify <tt>state's</tt> head position.
	 * 
	 * @param state
	 *            the {@link LexerState} to use
	 * @param advance
	 *            whether to advance <tt>state's</tt> head position
	 * @return the next {@link GenericToken Token} in the input
	 * @throws LexerException
	 *             so that lexer exceptions can be propagated back to the original caller
	 */
	public To getNextToken(LexerState<To, Ty, R, D, L> state, boolean advance) throws LexerException {
		if (state.getHead() >= state.getInput().length())
			throw new EmptyInputException();
		int initial = state.getHead();
		try {
			if (state.getHead() >= state.getInput().length())
				throw new EmptyInputException();
			for (int lim = state.getInput().length(); state.getHead() < lim;) {
				Matcher longest = null;
				LogicBlock<To, Ty, R, D, L> match = null;
				for (Pattern p : patterns.keySet()) {
					Matcher m = p.matcher(state.getInput());
					if (m.find(state.getHead()) && m.start() == state.getHead() && (longest == null || m.end() > longest.end())) {
						longest = m;
						match = patterns.get(p);
					}
				}
				if (longest == null)
					throw new UnrecognizedCharacterException(state.getInput(), state.getHead());
				state.advance(longest);
				if (match == null) //Handle ignores
					continue;
				if (match instanceof AscentBlock)
					throw new EmptyInputException();
				@SuppressWarnings("unchecked")
				To token = match.handle((L) this, state, longest);
				return token;
			}
			To out = state.getRoot();
			return out == null ? tokenConstructor.construct() : out;
		}
		finally {
			if (!advance)
				state.setHead(initial);
		}
	}
	
	/**
	 * Skips over tokens that are set to be ignored.<br>
	 * This <i>does</i> modify the passed {@link LexerState}.
	 * 
	 * @return the number of characters that were skipped
	 */
	final int skipIgnores(LexerState<To, Ty, R, D, L> state) {
		Matcher longest = null;
		LogicBlock<To, Ty, R, D, L> match = null;
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
		patterns.put(rule.pattern, (lexer, state, match) -> rule.action.perform(lexer, state, match));
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
			descender.openAction.apply(lexer, state);
			LexerState<To, Ty, R, D, L> descended = state.descend(descender);
			To out = ((GenericLexer<To, Ty, R, D, L>) lexer).lex(descended);
			state.setHead(descended.getHead());
			return out;
		});
		patterns.put(descender.close, ((AscentBlock<To, Ty, R, D, L>) (lexer, state, match) -> {
			if (state.getDescender() != descender)
				throw new UnbalancedDescenderException(state.getInput(), state.getHead());
			To root = state.getRoot();
			return descender.closeAction.perform(lexer, state, root == null ? tokenConstructor.construct() : root);
		}));
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
	 * Adds the {@link IgnorePattern} to the lexer.
	 * 
	 * @param ignore
	 *            the {@link IgnorePattern} to add
	 * @throws PatternCollisionException
	 *             if a {@link Pattern} being added is already loaded
	 */
	public void addIgnore(IgnorePattern ignore) {
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
	 * Removes the {@link IgnorePattern} from the lexer.
	 * 
	 * @param ignore
	 *            the {@link IgnorePattern} to remove
	 * @return the {@link Pattern} that was being ignored if it was loaded in the lexer, otherwise null
	 */
	public Pattern removeIgnore(IgnorePattern ignore) {
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
	public Map<Pattern, LogicBlock<To, Ty, R, D, L>> getPatterns() {
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
	 * @return the token constructor being used by this {@link GenericLexer}
	 */
	public final TokenConstructor<Ty, To> getTokenConstructor() {
		return tokenConstructor;
	}
}
