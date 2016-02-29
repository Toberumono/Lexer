package toberumono.lexer.errors;

import java.util.regex.Pattern;

import toberumono.lexer.base.Descender;
import toberumono.lexer.base.Lexer;
import toberumono.lexer.base.Rule;

/**
 * This is thrown when there is an attempt to add a {@link Rule}, {@link Descender}, or ignore to a {@link Lexer} that
 * contains that {@link Pattern}.
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
