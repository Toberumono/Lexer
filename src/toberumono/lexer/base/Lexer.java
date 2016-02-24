package toberumono.lexer.base;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.lexer.errors.EmptyInputException;
import toberumono.lexer.errors.LexerException;
import toberumono.lexer.errors.PatternCollisionException;
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
public interface Lexer<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>> {
	
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
	public C lex(String input) throws LexerException;
	
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
	public C lex(LexerState<C, T, R, D, L> state) throws LexerException;
	
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
	 * Adds a new rule
	 * 
	 * @param name
	 *            the name of the rule
	 * @param rule
	 *            the rule
	 * @throws PatternCollisionException
	 *             if a {@link Pattern} being added is already loaded
	 */
	public void addRule(String name, R rule);
	
	/**
	 * Removes a rule
	 * 
	 * @param name
	 *            the name of the rule to remove
	 * @return the removed rule if a rule of that name existed, otherwise null
	 */
	public R removeRule(String name);
	
	/**
	 * Gets a rule by name
	 * 
	 * @param name
	 *            the name of the rule to get
	 * @return the rule if a rule corresponding to that name is loaded, otherwise null
	 */
	public R getRule(String name);
	
	/**
	 * @return an unmodifiable view of the rules map (this view is backed by the internal map and only needs to be retrieved
	 *         once)
	 */
	public Map<String, R> getRules();
	
	/**
	 * Adds a new {@link AbstractDescender Descender}.
	 * 
	 * @param name
	 *            the name of the {@link AbstractDescender Descender}
	 * @param descender
	 *            the {@link AbstractDescender Descender}
	 * @throws PatternCollisionException
	 *             if a {@link Pattern} being added is already loaded
	 */
	public void addDescender(String name, D descender);
	
	/**
	 * Removes a {@link AbstractDescender Descender}
	 * 
	 * @param name
	 *            the name of the {@link AbstractDescender Descender} to remove
	 * @return the removed {@link AbstractDescender Descender} if a {@link AbstractDescender Descender} of that name existed,
	 *         otherwise {@code null}
	 */
	public D removeDescender(String name);
	
	/**
	 * Gets a descender by name.
	 * 
	 * @param name
	 *            the name of the descender to get
	 * @return the descender if a descender corresponding to that name is loaded, otherwise null
	 */
	public D getDescender(String name);
	
	/**
	 * @return an unmodifiable view of the descenders map (this view is backed by the internal map and only needs to be
	 *         retrieved once)
	 */
	public Map<String, D> getDescenders();
	
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
	public void addIgnore(String name, Pattern pattern);
	
	/**
	 * Adds the {@link DefaultPattern} to the lexer.
	 * 
	 * @param ignore
	 *            the {@link DefaultPattern} to add
	 * @throws PatternCollisionException
	 *             if a {@link Pattern} being added is already loaded
	 */
	public void addIgnore(DefaultPattern ignore);
	
	/**
	 * Removes an ignored {@link Pattern}
	 * 
	 * @param name
	 *            the name of the ignored {@link Pattern} to remove
	 * @return the removed {@link Pattern} if a {@link Pattern} of that name existed, otherwise null
	 */
	public Pattern removeIgnore(String name);
	
	/**
	 * Removes the {@link DefaultPattern} from the lexer.
	 * 
	 * @param ignore
	 *            the {@link DefaultPattern} to remove
	 * @return the {@link Pattern} that was being ignored if it was loaded in the lexer, otherwise null
	 */
	public Pattern removeIgnore(DefaultPattern ignore);
	
	/**
	 * Gets an ignored {@link Pattern} by name
	 * 
	 * @param name
	 *            the name of the ignored {@link Pattern} to get
	 * @return the ignored {@link Pattern} if one corresponding to that name is loaded, otherwise null
	 */
	public Pattern getIgnore(String name);
	
	/**
	 * @return an unmodifiable view of the ignores map (this view is backed by the internal map and only needs to be
	 *         retrieved once)
	 */
	public Map<String, Pattern> getIgnores();
	
	/**
	 * The patterns map that is used in the actual lexing loop. This is mainly for internal use.
	 * 
	 * @return an unmodifiable view of the patterns map (this view is backed by the internal map and only needs to be
	 *         retrieved once)
	 */
	public Map<Pattern, Action<C, T, R, D, L, Matcher>> getPatterns();
	
	/**
	 * @return the cell constructor being used by this {@link AbstractLexer}
	 */
	public ConsCellConstructor<T, C> getConsCellConstructor();
	
	/**
	 * @return the default {@link AbstractLanguage Language} for this {@link AbstractLexer}
	 */
	public Language<C, T, R, D, L> getLanguage();
	
	/**
	 * @return the the {@link GenericConsType} that represents empty values
	 */
	public T getEmptyType();
}
