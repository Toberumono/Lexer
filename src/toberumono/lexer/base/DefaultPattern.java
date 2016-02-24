package toberumono.lexer.base;

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
	public default String getName() {
		return this.toString(); //This works for all uniquely-named enums
	}
	
	/**
	 * @return the {@link Pattern} that the {@link DefaultPattern} represents
	 */
	public Pattern getPattern();
}
