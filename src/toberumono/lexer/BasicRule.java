package toberumono.lexer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.lexer.base.AbstractRule;
import toberumono.lexer.base.LexerAction;
import toberumono.lexer.base.Rule;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A Rule for the Lexer
 * 
 * @author Toberumono
 */
public class BasicRule extends AbstractRule<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer> {
	
	/**
	 * Constructs a new {@link Rule} with the given data
	 * 
	 * @param pattern
	 *            the regex {@link Pattern} for this {@link Rule} to use.
	 * @param type
	 *            the type for {@code ConsCell}s matched by this rule
	 */
	public BasicRule(Pattern pattern, ConsType type) {
		super(pattern, type);
	}
	
	/**
	 * Constructs a new {@link Rule} with the given data
	 * 
	 * @param pattern
	 *            the regex {@link Pattern} for this {@link Rule} to use.
	 * @param action
	 *            the action to perform on the part of the input matched by this {@link Rule}
	 */
	public BasicRule(Pattern pattern, LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, MatchResult> action) {
		super(pattern, action);
	}
}
