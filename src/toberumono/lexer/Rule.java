package toberumono.lexer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.lexer.genericBase.GenericAction;
import toberumono.lexer.genericBase.GenericRule;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A Rule for the Lexer
 * 
 * @author Toberumono
 */
public class Rule extends GenericRule<ConsCell, ConsType, Rule, Descender, Lexer> {
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param type
	 *            the type for <tt>ConsCell</tt>s matched by this rule
	 */
	public Rule(Pattern pattern, ConsType type) {
		super(pattern, type);
	}
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            the regex {@link java.util.regex.Pattern Pattern} for this <tt>Rule</tt> to use.
	 * @param action
	 *            the action to perform on the part of the input matched by this <tt>Rule</tt>
	 */
	public Rule(Pattern pattern, GenericAction<ConsCell, ConsType, Rule, Descender, Lexer, MatchResult> action) {
		super(pattern, action);
	}
}
