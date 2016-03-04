package toberumono.lexer;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.lexer.base.AbstractLexer;
import toberumono.lexer.base.LexerAction;
import toberumono.lexer.base.Lexer;
import toberumono.lexer.util.CommentPatterns;
import toberumono.lexer.util.DefaultIgnorePatterns;
import toberumono.lexer.util.DefaultPattern;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A basic implementation of {@link Lexer}. For most purposes, using this implementation should be more than
 * sufficient.
 * 
 * @author Toberumono
 */
public class BasicLexer extends AbstractLexer<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer> {
	
	/**
	 * Basic constructor for a {@link BasicLexer}
	 * 
	 * @param ignore
	 *            a list of patterns to ignore. The {@link DefaultIgnorePatterns} and {@link CommentPatterns} enums have a
	 *            few common patterns.
	 */
	public BasicLexer(DefaultPattern... ignore) {
		super(ConsCell::new, BasicLanguage::new, ConsType.EMPTY, ignore);
	}
	
	/**
	 * Alternate constructor that allows for the internal maps to be set by whatever called this constructor
	 * 
	 * @param rules
	 *            the {@link Map} to be used to hold the {@link BasicRule Rules}
	 * @param descenders
	 *            the {@link Map} to be used to hold the {@link BasicDescender Descenders}
	 * @param ignores
	 *            the {@link Map} to be used to hold the {@link Pattern Patterns} to be ignored
	 * @param patterns
	 *            the {@link Map} to be used to hold the {@link Pattern Patterns} for the {@link BasicRule Rules} and
	 *            {@link BasicDescender Descenders}
	 * @param ignore
	 *            a list of patterns to ignore. The {@link DefaultIgnorePatterns} and {@link CommentPatterns} enums have a
	 *            few common patterns.
	 */
	public BasicLexer(Map<String, BasicRule> rules, Map<String, BasicDescender> descenders, Map<String, Pattern> ignores,
			Map<Pattern, LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, Matcher>> patterns, DefaultPattern... ignore) {
		super(rules, descenders, ignores, patterns, ConsCell::new, BasicLanguage::new, ConsType.EMPTY, ignore);
	}
}
