package toberumono.lexer.util;

import java.util.regex.Pattern;

import toberumono.lexer.base.DefaultPattern;
import toberumono.lexer.base.AbstractLexer;
import toberumono.lexer.base.LanguageConstructor;
import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * A few {@link Pattern Patterns} for various comment styles.<br>
 * Mainly used in the {@link AbstractLexer GenericLexer's} constructor.
 * 
 * @author Toberumono
 * @see AbstractLexer#AbstractLexer(ConsCellConstructor, LanguageConstructor, GenericConsType, DefaultPattern...)
 * @see DefaultIgnorePatterns
 */
public enum CommentPatterns implements DefaultPattern {
	/**
	 * Describes C-style single-line comments.
	 * 
	 * @see #C_COMMENT
	 * @see #SH_COMMENT
	 * @see #MULTI_LINE_COMMENT
	 */
	SINGLE_LINE_COMMENT(Pattern.compile("//.*?" + System.lineSeparator())),
	/**
	 * Describes C-style multi-line comments.
	 * 
	 * @see #C_COMMENT
	 * @see #SINGLE_LINE_COMMENT
	 */
	MULTI_LINE_COMMENT(Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL)),
	/**
	 * Describes C-style single- and multi-line comments.
	 * 
	 * @see #SINGLE_LINE_COMMENT
	 * @see #MULTI_LINE_COMMENT
	 */
	C_COMMENT(Pattern.compile("(//[^\n\r]*?" + System.lineSeparator() + "|/\\*.*?\\*/)", Pattern.DOTALL)),
	/**
	 * Describes Shell-style single-line comments.
	 * 
	 * @see #SINGLE_LINE_COMMENT
	 */
	SH_COMMENT(Pattern.compile("#.*?" + System.lineSeparator())),
	/**
	 * Describes Markup language-style comments (e.g. HTML, XML)
	 * @see #SINGLE_LINE_COMMENT
	 * @see #MULTI_LINE_COMMENT
	 */
	ML_COMMENT(Pattern.compile("<!--.*?-->", Pattern.DOTALL)),
	/**
	 * Describes C- and Shell-style single- and multi-line comments.
	 * 
	 * @see #SH_COMMENT
	 * @see #C_COMMENT
	 * @see #ML_COMMENT
	 */
	COMMENT(Pattern.compile("((//|#)[^\n\r]*?" + System.lineSeparator() + "|/\\*.*?\\*/|<!--.*?-->)", Pattern.DOTALL));
	
	private Pattern pattern;
	
	private CommentPatterns(Pattern pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public Pattern getPattern() {
		return pattern;
	}
}
