package toberumono.lexer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.lexer.base.AbstractRule;
import toberumono.lexer.base.LexerAction;
import toberumono.lexer.util.DefaultPattern;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A Rule for the Lexer
 * 
 * @author Toberumono
 */
public class BasicRule extends AbstractRule<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer> {
	
	/**
	 * Constructs a new {@link BasicRule} with the given {@link Pattern} and {@link ConsType}.
	 * 
	 * @param pattern
	 *            the {@link Pattern} that describes tokens that the {@link BasicRule} can process
	 * @param type
	 *            the {@link ConsType type} of the {@code car} value of the {@link ConsCell ConsCells} matched by this
	 *            {@link BasicRule rule}
	 */
	public BasicRule(Pattern pattern, ConsType type) {
		super(pattern, type);
	}
	
	/**
	 * Constructs a new {@link BasicRule} with the given {@link Pattern} and {@link LexerAction}.
	 * 
	 * @param pattern
	 *            the {@link Pattern} that describes tokens that the {@link BasicRule} can process
	 * @param action
	 *            the {@link LexerAction action} to perform on the part of the input matched by the {@link BasicRule
	 *            rule's} {@link Pattern}
	 */
	public BasicRule(Pattern pattern, LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, MatchResult> action) {
		super(pattern, action);
	}
	
	/**
	 * Constructs a new {@link BasicRule} with the given {@link DefaultPattern} and {@link ConsType}.
	 * 
	 * @param pattern
	 *            the {@link DefaultPattern} containing the regex {@link Pattern} that describes tokens that the
	 *            {@link BasicRule} can process
	 * @param type
	 *            the {@link ConsType type} of the {@code car} value of the {@link ConsCell ConsCells} matched by this
	 *            {@link BasicRule rule}
	 */
	public BasicRule(DefaultPattern pattern, ConsType type) {
		super(pattern, type);
	}
	
	/**
	 * Constructs a new {@link BasicRule} with the given {@link DefaultPattern} and {@link LexerAction}.
	 * 
	 * @param pattern
	 *            the {@link DefaultPattern} containing the regex {@link Pattern} that describes tokens that the
	 *            {@link BasicRule} can process
	 * @param action
	 *            the {@link LexerAction action} to perform on the part of the input matched by the {@link BasicRule
	 *            rule's} {@link Pattern}
	 */
	public BasicRule(DefaultPattern pattern, LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, MatchResult> action) {
		super(pattern, action);
	}
}
