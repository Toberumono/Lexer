package toberumono.lexer;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.lexer.genericBase.GenericAction;
import toberumono.lexer.genericBase.GenericLexer;
import toberumono.lexer.genericBase.DefaultPattern;
import toberumono.lexer.util.CommentPatterns;
import toberumono.lexer.util.DefaultIgnorePatterns;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A basic implementation of {@link GenericLexer}. For most purposes, using this implementation should be more than
 * sufficient.
 * 
 * @author Toberumono
 */
public class Lexer extends GenericLexer<ConsCell, ConsType, Rule, Descender, Lexer> {
	
	/**
	 * Basic constructor for a {@link Lexer}
	 * 
	 * @param ignore
	 *            a list of patterns to ignore. The {@link DefaultIgnorePatterns} and {@link CommentPatterns} enums have a
	 *            few common patterns.
	 */
	public Lexer(DefaultPattern... ignore) {
		super(ConsCell::new, ConsType.EMPTY, ignore);
	}
	
	/**
	 * Alternate constructor that allows for the internal maps to be set by whatever called this constructor
	 * 
	 * @param rules
	 *            the {@link Map} to be used to hold the {@link Rule Rules}
	 * @param descenders
	 *            the {@link Map} to be used to hold the {@link Descender Descenders}
	 * @param ignores
	 *            the {@link Map} to be used to hold the {@link Pattern Patterns} to be ignored
	 * @param patterns
	 *            the {@link Map} to be used to hold the {@link Pattern Patterns} for the {@link Rule Rules} and
	 *            {@link Descender Descenders}
	 * @param ignore
	 *            a list of patterns to ignore. The {@link DefaultIgnorePatterns} and {@link CommentPatterns} enums have a
	 *            few common patterns.
	 */
	public Lexer(Map<String, Rule> rules, Map<String, Descender> descenders, Map<String, Pattern> ignores,
			Map<Pattern, GenericAction<ConsCell, ConsType, Rule, Descender, Lexer, Matcher>> patterns, DefaultPattern... ignore) {
		super(rules, descenders, ignores, patterns, ConsCell::new, ConsType.EMPTY, ignore);
	}
}
