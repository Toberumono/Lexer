package toberumono.lexer;

import java.util.regex.Pattern;

/**
 * A superinterface to allow quick extension of default patterns.
 * 
 * @author Toberumono
 */
public interface IgnorePattern {
	
	/**
	 * @return the name of the {@link IgnorePattern}
	 */
	public String getName();
	
	/**
	 * @return the {@link Pattern} that the {@link IgnorePattern} represents
	 */
	public Pattern getPattern();
}
