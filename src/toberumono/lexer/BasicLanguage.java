package toberumono.lexer;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.lexer.base.AbstractDescender;
import toberumono.lexer.base.AbstractLanguage;
import toberumono.lexer.base.AbstractRule;
import toberumono.lexer.base.Action;
import toberumono.lexer.base.Lexer;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.ConsType;

/**
 * This represents a language that can be used by a {@link Lexer} to tokenize an input {@link String}
 * 
 * @author Toberumono
 */
public class BasicLanguage extends AbstractLanguage<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer> {

	/**
	 * Constructs an empty {@link AbstractLanguage} with the given {@link ConsCellConstructor}
	 * 
	 * @param cellConstructor
	 *            the {@link ConsCellConstructor} to be used
	 */
	public BasicLanguage(ConsCellConstructor<ConsType, ConsCell> cellConstructor) {
		super(cellConstructor);
	}
	
	/**
	 * Constructs a {@link BasicLanguage} with the given {@link ConsCellConstructor} and data maps. Note that the {@link Map
	 * Maps} are <i>not</i> copied in the constructor
	 * 
	 * @param cellConstructor
	 *            the {@link ConsCellConstructor} to be used
	 * @param rules
	 *            a {@link Map} containing the {@link AbstractRule Rules}
	 * @param descenders
	 *            a {@link Map} containing the {@link AbstractDescender Descender}
	 * @param ignores
	 *            a {@link Map} containing the {@link Pattern Patterns} to ignore
	 * @param names
	 *            a {@link Map} containing the names that are in use
	 * @param patterns
	 *            a {@link Map} that maps {@link Pattern Patterns} to their associated {@link Action} actions
	 */
	public BasicLanguage(ConsCellConstructor<ConsType, ConsCell> cellConstructor, Map<String, BasicRule> rules, Map<String, BasicDescender> descenders, Map<String, Pattern> ignores,
			Map<Pattern, String> names, Map<Pattern, Action<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, Matcher>> patterns) {
		super(cellConstructor, rules, descenders, ignores, names, patterns);
	}
	
}
