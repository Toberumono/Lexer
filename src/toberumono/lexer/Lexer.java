package toberumono.lexer;

import java.util.Map;
import java.util.regex.Pattern;

import toberumono.lexer.genericBase.GenericLexer;
import toberumono.lexer.genericBase.IgnorePattern;
import toberumono.lexer.genericBase.LogicBlock;
import toberumono.lexer.util.CommentPatterns;
import toberumono.lexer.util.DefaultIgnorePatterns;

/**
 * A basic implementation of {@link GenericLexer}. For most purposes, using this implementation should be more than
 * sufficient.
 * 
 * @author Toberumono
 */
public class Lexer extends GenericLexer<ConsCell, Type, Rule, Descender, Lexer> {
	
	/**
	 * Basic constructor for a {@link Lexer}
	 * 
	 * @param ignore
	 *            A list of patterns to ignore. The {@link DefaultIgnorePatterns} and {@link CommentPatterns} enums have a few common patterns.
	 */
	public Lexer(IgnorePattern... ignore) {
		super(ConsCell::new, Type.EMPTY, ignore);
	}
	
	public Lexer(Map<String, Rule> rules, Map<String, Descender> descenders, Map<String, Pattern> ignores, Map<Pattern, LogicBlock<ConsCell, Type, Rule, Descender, Lexer>> patterns, IgnorePattern... ignore) {
		super(rules, descenders, ignores, patterns, ConsCell::new, Type.EMPTY, ignore);
	}
}
