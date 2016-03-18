package toberumono.lexer.base;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.lexer.errors.EmptyInputException;
import toberumono.lexer.errors.LexerException;
import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
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
public interface Lexer<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>>
		extends Language<C, T, R, D, L> {
	
	/**
	 * Tokenizes a {@link String}
	 * 
	 * @param input
	 *            the {@link String} to tokenize
	 * @return the tokens in the {@link String} (wrapped in {@link GenericConsCell ConsCells}) or {@code null} if none were
	 *         found
	 * @throws LexerException
	 *             so that lexer exceptions can be propagated back to the original caller
	 * @see #lex(LexerState)
	 */
	public C lex(String input) throws LexerException;
	
	/**
	 * Tokenizes the remaining {@link LexerState#getInput() input} in the given {@link LexerState}.<br>
	 * Use {@link #lex(String)} to tokenize an input from the beginning.
	 * 
	 * @param state
	 *            the {@link LexerState} to process
	 * @return the tokens in the {@link LexerState LexerState's} {@link LexerState#getInput() input} (wrapped in
	 *         {@link GenericConsCell ConsCells}) or {@code null} if none were found
	 * @throws LexerException
	 *             so that lexer exceptions can be propagated back to the original caller
	 * @see #lex(String)
	 */
	public C lex(LexerState<C, T, R, D, L> state) throws LexerException;
	
	/**
	 * Gets the next token (wrapped in a {@link GenericConsCell ConsCell}) in the {@link LexerState LexerState's}
	 * {@link LexerState#getInput() input}.<br>
	 * This will throw an {@link EmptyInputException} if it encounters a close token.<br>
	 * If {@code advance} is {@code true}, then this <i>will</i> modify {@code state's} head position.
	 * 
	 * @param state
	 *            the {@link LexerState} to use
	 * @param advance
	 *            whether to advance {@code state's} head position
	 * @return the next token in the {@link LexerState LexerState's} {@link LexerState#getInput() input} (wrapped in a
	 *         {@link GenericConsCell ConsCell})
	 * @throws LexerException
	 *             so that lexer exceptions can be propagated back to the original caller
	 * @throws EmptyInputException
	 *             if the next token in the {@link LexerState LexerState's} {@link LexerState#getInput() input} was a close
	 *             token
	 */
	public C getNextConsCell(LexerState<C, T, R, D, L> state, boolean advance) throws LexerException;
	
	/**
	 * Skips over cells that are set to be ignored.<br>
	 * This <i>does</i> modify the passed {@link LexerState}.
	 * 
	 * @param state
	 *            the current {@link LexerState}
	 * @return the number of characters that were skipped
	 */
	public int skipIgnores(LexerState<C, T, R, D, L> state);
	
	/**
	 * @return the cell constructor being used by the {@link Lexer}
	 */
	public ConsCellConstructor<T, C> getConsCellConstructor();
	
	/**
	 * @return the default {@link Language} for the {@link Lexer}
	 */
	public Language<C, T, R, D, L> getLanguage();
	
	/**
	 * @return the the {@link GenericConsType} that represents empty values
	 */
	public T getEmptyType();
	
	/* ************************************************************************************************* */
	/* The default implementation of methods from Language is to forward to the Lexer's primary Language */
	/* ************************************************************************************************* */
	
	@Override
	public default void addRule(String name, R rule) {
		getLanguage().addRule(name, rule);
	}
	
	@Override
	public default R removeRule(String name) {
		return getLanguage().removeRule(name);
	}
	
	@Override
	public default R getRule(String name) {
		return getLanguage().getRule(name);
	}
	
	/**
	 * @return an <i>unmodifiable</i> view of the {@link Rule Rules} in the {@link Language}
	 */
	@Override
	public default Map<String, R> getRules() {
		return Collections.unmodifiableMap(getLanguage().getRules());
	}
	
	@Override
	public default void addDescender(String name, D descender) {
		getLanguage().addDescender(name, descender);
	}
	
	@Override
	public default D removeDescender(String name) {
		return getLanguage().removeDescender(name);
	}
	
	@Override
	public default D getDescender(String name) {
		return getLanguage().getDescender(name);
	}
	
	/**
	 * @return an <i>unmodifiable</i> view of the {@link Descender Descenders} in the {@link Language}
	 */
	@Override
	public default Map<String, D> getDescenders() {
		return Collections.unmodifiableMap(getLanguage().getDescenders());
	}
	
	@Override
	public default void addIgnore(String name, Pattern pattern) {
		getLanguage().addIgnore(name, pattern);
	}
	
	@Override
	public default Pattern removeIgnore(String name) {
		return getLanguage().removeIgnore(name);
	}
	
	@Override
	public default Pattern getIgnore(String name) {
		return getLanguage().getIgnore(name);
	}
	
	/**
	 * @return an <i>unmodifiable</i> view of the {@link Pattern Patterns} that define input that can be ignored in the
	 *         {@link Language}
	 */
	@Override
	public default Map<String, Pattern> getIgnores() {
		return Collections.unmodifiableMap(getLanguage().getIgnores());
	}
	
	/**
	 * @return an <i>unmodifiable</i> view of the names used by the {@link Rule Rules}, {@link Descender Descenders}, and
	 *         ignoreable {@link Pattern Patterns} in the {@link Language}
	 */
	@Override
	public default Map<Pattern, String> getNames() {
		return Collections.unmodifiableMap(getLanguage().getNames());
	}
	
	/**
	 * @return an <i>unmodifiable</i> view of the {@link LexerAction LexerActions} to perform for each {@link Pattern} in the
	 *         {@link Language}
	 */
	@Override
	public default Map<Pattern, LexerAction<C, T, R, D, L, Matcher>> getPatterns() {
		return Collections.unmodifiableMap(getLanguage().getPatterns());
	}
}
