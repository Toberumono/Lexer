package toberumono.lexer.util;

import java.util.regex.Pattern;

import toberumono.lexer.genericBase.GenericLexer;
import toberumono.lexer.genericBase.IgnorePattern;
import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * A few {@link Pattern Patterns} for various comment styles.<br>
 * Mainly used in the {@link GenericLexer GenericLexer's} constructor.
 * 
 * @author Toberumono
 * @see GenericLexer#GenericLexer(ConsCellConstructor, GenericConsType, IgnorePattern...)
 * @see DefaultIgnorePatterns
 */
public enum CommentPatterns implements IgnorePattern {
	/**
	 * Ignores the contents of C-style single-line comments.
	 * 
	 * @see #C_COMMENT
	 * @see #SH_COMMENT
	 * @see #MULTI_LINE_COMMENT
	 */
	SINGLE_LINE_COMMENT(Pattern.compile("//.*?" + System.lineSeparator())),
	/**
	 * Ignores the contents of C-style multi-line comments.
	 * 
	 * @see #C_COMMENT
	 * @see #SINGLE_LINE_COMMENT
	 */
	MULTI_LINE_COMMENT(Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL)),
	/**
	 * Ignores the contents of C-style single- and multi-line comments.
	 * 
	 * @see #SINGLE_LINE_COMMENT
	 * @see #MULTI_LINE_COMMENT
	 */
	C_COMMENT(Pattern.compile("(//.*?" + System.lineSeparator() + "|/\\*.*?\\*/)", Pattern.DOTALL)),
	/**
	 * Ignores the contents of Shell-style single-line comments.
	 * 
	 * @see #SINGLE_LINE_COMMENT
	 */
	SH_COMMENT(Pattern.compile("#.*?" + System.lineSeparator())),
	/**
	 * Ignores the contents of C- and Shell-style single- and multi-line comments.
	 * 
	 * @see #SH_COMMENT
	 * @see #C_COMMENT
	 */
	COMMENT(Pattern.compile("((//|#).*?" + System.lineSeparator() + "|/\\*.*?\\*/)", Pattern.DOTALL));
	
	private Pattern pattern;
	
	private CommentPatterns(Pattern pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public Pattern getPattern() {
		return pattern;
	}
	
	@Override
	public String getName() {
		return this.toString();
	}
}
