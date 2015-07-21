package toberumono.lexer;

import java.util.regex.Pattern;

/**
 * A superinterface to allow quick extension of default patterns.
 * 
 * @author Toberumono
 */
public interface IgnorePattern {
	
	public String getName();
	
	public Pattern getPattern();
}
