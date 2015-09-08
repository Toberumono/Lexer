package toberumono.lexer.errors;

import java.util.regex.Pattern;

/**
 * This is thrown when there is an attempt to add a rule, descender, or ignore to a lexer that contains an preexisting
 * pattern.
 * 
 * @author Toberumono
 */
public class PatternCollisionException extends RuntimeException {
	
	/**
	 * Constructs a {@link PatternCollisionException} using the given pattern and owner.
	 * 
	 * @param pattern
	 *            the colliding {@link Pattern}
	 * @param owner
	 *            the owner of the {@link Pattern}
	 */
	public PatternCollisionException(Pattern pattern, String owner) {
		super("The Pattern, " + pattern.toString() + ", is already mapped to " + owner + ".");
	}
}
