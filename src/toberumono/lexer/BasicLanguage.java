package toberumono.lexer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.lexer.base.AbstractLanguage;
import toberumono.lexer.base.Descender;
import toberumono.lexer.base.Lexer;
import toberumono.lexer.base.LexerAction;
import toberumono.lexer.base.Rule;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * This represents a language that can be used by a {@link Lexer} to tokenize an input {@link String}
 * 
 * @author Toberumono
 */
public class BasicLanguage extends AbstractLanguage<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer> {
	
	/**
	 * Constructs an empty {@link BasicLanguage}
	 */
	public BasicLanguage() {
		super();
	}
	
	/**
	 * Constructs an {@link BasicLanguage} with the given data {@link Map Maps}<br>
	 * This is provided as a convenience constructor with a faster cloning method for instances of {@link BasicLanguage} that
	 * use instances of {@link HashMap} for the internal {@link Map Maps}
	 * 
	 * @param rules
	 *            a {@link Map} containing the {@link Rule Rules}
	 * @param descenders
	 *            a {@link Map} containing the {@link Descender Descenders}
	 * @param ignores
	 *            a {@link Map} containing the {@link Pattern Patterns} to ignore
	 * @param names
	 *            a {@link Map} containing the names that are in use
	 * @param patterns
	 *            a {@link Map} that maps {@link Pattern Patterns} to their associated {@link LexerAction LexerActions}
	 */
	public BasicLanguage(HashMap<String, BasicRule> rules, HashMap<String, BasicDescender> descenders, HashMap<String, Pattern> ignores, HashMap<Pattern, String> names,
			HashMap<Pattern, LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, Matcher>> patterns) {
		super(rules, descenders, ignores, names, patterns);
	}
	
	/**
	 * Constructs an {@link BasicLanguage} with the given data {@link Map Maps}<br>
	 * <b>Note:</b> the {@link Map Maps} are <i>not</i> copied in the constructor<br>
	 * <b>Note:</b> if any of the {@link Map Maps} do not meet the requirements specified in {@link #clone()}, the
	 * {@link #BasicLanguage(Map, Map, Map, Map, Map, BiFunction)} constructor should be used instead
	 * 
	 * @param rules
	 *            a {@link Map} containing the {@link Rule Rules}
	 * @param descenders
	 *            a {@link Map} containing the {@link Descender Descenders}
	 * @param ignores
	 *            a {@link Map} containing the {@link Pattern Patterns} to ignore
	 * @param names
	 *            a {@link Map} containing the names that are in use
	 * @param patterns
	 *            a {@link Map} that maps {@link Pattern Patterns} to their associated {@link LexerAction LexerActions}
	 */
	public BasicLanguage(Map<String, BasicRule> rules, Map<String, BasicDescender> descenders, Map<String, Pattern> ignores, Map<Pattern, String> names,
			Map<Pattern, LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, Matcher>> patterns) {
		super(rules, descenders, ignores, names, patterns);
	}
	
	/**
	 * Constructs an {@link BasicLanguage} with the given data {@link Map Maps}<br>
	 * <b>Note:</b> the {@link Map Maps} are <i>not</i> copied in the constructor<br>
	 * <b>Requirements for the {@code cloner} {@link BiFunction}</b>
	 * <ol>
	 * <li>The {@link BiFunction BiFunction's} first argument is the {@link Map} being cloned</li>
	 * <li>The {@link BiFunction BiFunction's} second argument is the name of the {@link Map} being cloned (i.e. rules,
	 * descenders, etc.)</li>
	 * <li>The {@link BiFunction} <i>must</i> return a {@link Map} with the same types as the input {@link Map} for casting
	 * purposes</li>
	 * </ol>
	 * 
	 * @param rules
	 *            a {@link Map} containing the {@link Rule Rules}
	 * @param descenders
	 *            a {@link Map} containing the {@link Descender Descenders}
	 * @param ignores
	 *            a {@link Map} containing the {@link Pattern Patterns} to ignore
	 * @param names
	 *            a {@link Map} containing the names that are in use
	 * @param patterns
	 *            a {@link Map} that maps {@link Pattern Patterns} to their associated {@link LexerAction LexerActions}
	 * @param cloner
	 *            the {@link BiFunction} to be used to clone the internal {@link Map Maps}. It must meet the requirements
	 *            specified above
	 */
	public BasicLanguage(Map<String, BasicRule> rules, Map<String, BasicDescender> descenders, Map<String, Pattern> ignores, Map<Pattern, String> names,
			Map<Pattern, LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, Matcher>> patterns, BiFunction<Map<?, ?>, String, Map<?, ?>> cloner) {
		super(rules, descenders, ignores, names, patterns, cloner);
	}
}
