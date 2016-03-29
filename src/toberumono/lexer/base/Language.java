package toberumono.lexer.base;

import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.lexer.errors.PatternCollisionException;
import toberumono.lexer.util.DefaultPattern;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;
import toberumono.structures.sexpressions.GenericConsCell;

/**
 * This represents a language that can be used by a {@link Lexer} to tokenize an input {@link String}.<br>
 * <b>Note:</b> all implementations of {@link Language} <i>must</i> implement {@link Cloneable}.
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
public interface Language<C extends GenericConsCell<C, T>, T extends ConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>> extends Cloneable {
	
	/**
	 * Adds a new {@link Rule}
	 * 
	 * @param name
	 *            the name of the {@link Rule}
	 * @param rule
	 *            the {@link Rule}
	 * @throws PatternCollisionException
	 *             if a {@link Pattern} being added is already loaded
	 */
	public void addRule(String name, R rule);
	
	/**
	 * Removes a {@link Rule}
	 * 
	 * @param name
	 *            the name of the {@link Rule} to remove
	 * @return the removed {@link Rule} if a {@link Rule} with that name existed, otherwise {@code null}
	 */
	public R removeRule(String name);
	
	/**
	 * Gets a {@link Rule} by name
	 * 
	 * @param name
	 *            the name of the {@link Rule} to get
	 * @return the {@link Rule} if a {@link Rule} corresponding to that name is loaded, otherwise {@code null}
	 */
	public R getRule(String name);
	
	/**
	 * @return the {@link Rule Rules} in the {@link Language}
	 */
	public Map<String, R> getRules();
	
	/**
	 * Adds a new {@link Descender}.
	 * 
	 * @param name
	 *            the name of the {@link Descender}
	 * @param descender
	 *            the {@link Descender}
	 * @throws PatternCollisionException
	 *             if a {@link Pattern} being added is already loaded
	 */
	public void addDescender(String name, D descender);
	
	/**
	 * Removes a {@link Descender}
	 * 
	 * @param name
	 *            the name of the {@link Descender} to remove
	 * @return the removed {@link Descender} if a {@link Descender} with that name existed, otherwise {@code null}
	 */
	public D removeDescender(String name);
	
	/**
	 * Gets a descender by name.
	 * 
	 * @param name
	 *            the name of the descender to get
	 * @return the descender if a descender corresponding to that name is loaded, otherwise {@code null}
	 */
	public D getDescender(String name);
	
	/**
	 * @return the {@link Descender Descenders} in the {@link Language}
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
	public default void addIgnore(DefaultPattern ignore) {
		addIgnore(ignore.getName(), ignore.getPattern());
	}
	
	/**
	 * Removes an ignored {@link Pattern}
	 * 
	 * @param name
	 *            the name of the ignored {@link Pattern} to remove
	 * @return the removed {@link Pattern} if a {@link Pattern} of that name existed, otherwise {@code null}
	 */
	public Pattern removeIgnore(String name);
	
	/**
	 * Removes the {@link DefaultPattern} from the lexer.
	 * 
	 * @param ignore
	 *            the {@link DefaultPattern} to remove
	 * @return the {@link Pattern} that was being ignored if it was loaded in the lexer, otherwise {@code null}
	 */
	public default Pattern removeIgnore(DefaultPattern ignore) {
		return removeIgnore(ignore.getName());
	}
	
	/**
	 * Gets an ignored {@link Pattern} by name
	 * 
	 * @param name
	 *            the name of the ignored {@link Pattern} to get
	 * @return the ignored {@link Pattern} if one corresponding to that name is loaded, otherwise {@code null}
	 */
	public Pattern getIgnore(String name);
	
	/**
	 * @return the {@link Pattern Patterns} that define input that can be ignored in the {@link Language}
	 */
	public Map<String, Pattern> getIgnores();
	
	/**
	 * @return the names used by the {@link Rule Rules}, {@link Descender Descenders}, and ignoreable {@link Pattern
	 *         Patterns} in the {@link Language}
	 */
	public Map<Pattern, String> getNames();
	
	/**
	 * @return the {@link LexerAction LexerActions} to perform for each {@link Pattern} in the {@link Language}
	 */
	public Map<Pattern, LexerAction<C, T, R, D, L, MatchResult>> getPatterns();
	
	/**
	 * @return a clone of the {@link Language} that is in keeping with the conventions specified by {@link Cloneable}
	 */
	public Language<C, T, R, D, L> clone();
}
