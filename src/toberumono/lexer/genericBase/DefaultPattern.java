package toberumono.lexer.genericBase;

import java.util.regex.Pattern;

/**
 * An interface to allow quick extension of default patterns.
 * 
 * @author Toberumono
 */
public interface DefaultPattern {
	
	/**
	 * @return the name of the {@link DefaultPattern}
	 */
	public String getName();
	
	/**
	 * @return the {@link Pattern} that the {@link DefaultPattern} represents
	 */
	public Pattern getPattern();
}
