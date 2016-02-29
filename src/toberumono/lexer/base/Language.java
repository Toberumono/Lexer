package toberumono.lexer.base;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.lexer.errors.PatternCollisionException;
import toberumono.lexer.util.DefaultPattern;
import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;


/**
 * This represents a language that can be used by a {@link Lexer} to tokenize an input {@link String}
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
public interface Language<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>> {
	
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
	void addRule(String name, R rule);
	
	/**
	 * Removes a rule
	 * 
	 * @param name
	 *            the name of the rule to remove
	 * @return the removed rule if a rule of that name existed, otherwise {@code null}
	 */
	R removeRule(String name);
	
	/**
	 * Gets a rule by name
	 * 
	 * @param name
	 *            the name of the rule to get
	 * @return the rule if a rule corresponding to that name is loaded, otherwise {@code null}
	 */
	R getRule(String name);
	
	/**
	 * @return the {@link Rule Rules} in the {@link Language}
	 */
	Map<String, R> getRules();
	
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
	void addDescender(String name, D descender);
	
	/**
	 * Removes a {@link Descender}
	 * 
	 * @param name
	 *            the name of the {@link Descender} to remove
	 * @return the removed {@link Descender} if a {@link Descender} of that name existed, otherwise {@code null}
	 */
	D removeDescender(String name);
	
	/**
	 * Gets a descender by name.
	 * 
	 * @param name
	 *            the name of the descender to get
	 * @return the descender if a descender corresponding to that name is loaded, otherwise {@code null}
	 */
	D getDescender(String name);
	
	/**
	 * @return the {@link Descender} in the {@link Language}
	 */
	Map<String, D> getDescenders();
	
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
	void addIgnore(String name, Pattern pattern);
	
	/**
	 * Adds the {@link DefaultPattern} to the lexer.
	 * 
	 * @param ignore
	 *            the {@link DefaultPattern} to add
	 * @throws PatternCollisionException
	 *             if a {@link Pattern} being added is already loaded
	 */
	void addIgnore(DefaultPattern ignore);
	
	/**
	 * Removes an ignored {@link Pattern}
	 * 
	 * @param name
	 *            the name of the ignored {@link Pattern} to remove
	 * @return the removed {@link Pattern} if a {@link Pattern} of that name existed, otherwise {@code null}
	 */
	Pattern removeIgnore(String name);
	
	/**
	 * Removes the {@link DefaultPattern} from the lexer.
	 * 
	 * @param ignore
	 *            the {@link DefaultPattern} to remove
	 * @return the {@link Pattern} that was being ignored if it was loaded in the lexer, otherwise {@code null}
	 */
	Pattern removeIgnore(DefaultPattern ignore);
	
	/**
	 * Gets an ignored {@link Pattern} by name
	 * 
	 * @param name
	 *            the name of the ignored {@link Pattern} to get
	 * @return the ignored {@link Pattern} if one corresponding to that name is loaded, otherwise {@code null}
	 */
	Pattern getIgnore(String name);
	
	/**
	 * @return the map containing pattern that are ignored in the {@link Language}
	 */
	Map<String, Pattern> getIgnores();
	
	/**
	 * @return the names used by {@link Rule Rules}, {@link Descender Descenders}, and ignores in the
	 *         {@link Language}
	 */
	Map<Pattern, String> getNames();
	
	/**
	 * @return the a mapping {@link Pattern} to {@link Action} to perform when the {@link Pattern} is matched
	 */
	Map<Pattern, Action<C, T, R, D, L, Matcher>> getPatterns();
	
}
