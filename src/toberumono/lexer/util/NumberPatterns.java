package toberumono.lexer.util;

import java.util.regex.Pattern;

import toberumono.lexer.genericBase.DefaultPattern;
import toberumono.lexer.genericBase.GenericLexer;
import toberumono.structures.sexpressions.ConsCellConstructor;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * A few {@link Pattern Patterns} for various number formats.<br>
 * NOTE: the PICKY_... {@link Pattern Patterns} are more complicated to parse, so only use them when needed.
 * 
 * @author Toberumono
 * @see GenericLexer#GenericLexer(ConsCellConstructor, GenericConsType, DefaultPattern...)
 * @see CommentPatterns
 */
public enum NumberPatterns implements DefaultPattern,NumberPatternsConstants {
	/**
	 * Describes an integer. This <i>can</i> start with 0.
	 * 
	 * @see #PICKY_INTEGER
	 * @see #DOUBLE
	 */
	INTEGER(Pattern.compile("[+-]?[0-9]+")),
	/**
	 * Describes an integer with a more rigorous format. This <i>cannot</i> start with 0 unless it equals 0.
	 * 
	 * @see #INTEGER
	 * @see #PICKY_DOUBLE
	 */
	PICKY_INTEGER(Pattern.compile("[+-]?([1-9][0-9]*|0)")),
	/**
	 * Describes a double. This <i>can</i> start with 0.
	 * 
	 * @see #PICKY_DOUBLE
	 * @see #INTEGER
	 */
	DOUBLE(Pattern.compile("[+-]?([0-9]+\\.[0-9]*|[0-9]*\\.[0-9]+)")),
	/**
	 * Describes a double with a more rigorous format. This <i>cannot</i> start with 0 unless it equals 0, 0., or 0.0.
	 * 
	 * @see #DOUBLE
	 * @see #PICKY_INTEGER
	 */
	PICKY_DOUBLE(Pattern.compile("[+-]?" + DN)),
	/**
	 * Describes a complex number of the following forms:
	 * <table summary="Complex Number Formats">
	 * <tr>
	 * <td>&plusmn;i</td>
	 * <td>&plusmn;ai</td>
	 * <td>&plusmn;ai&plusmn;b</td>
	 * <td>&plusmn;a&plusmn;bi</td>
	 * <td>&plusmn;a&plusmn;i</td>
	 * </tr>
	 * <tr>
	 * <td>&plusmn;a</td>
	 * <td>&plusmn;ia</td>
	 * <td>&plusmn;ia&plusmn;b</td>
	 * <td>&plusmn;a&plusmn;ib</td>
	 * <td>&plusmn;i&plusmn;b</td>
	 * </tr>
	 * </table>
	 * <br>
	 * NOTE: This uses the same pattern as {@link #PICKY_DOUBLE}
	 * 
	 * @see #PICKY_DOUBLE
	 */
	COMPLEX(Pattern.compile("[+-]?((i|i[*/]?" + DN + "|" + DN + "[*/]?i)(" + PMDN + ")?|" + DN + "(" + PM + "(i|i[*/]?" + DN + "|" + DN + "[*/]?i))?)"));
	//Number formats:                  i, ai, ia, ai+-b, ia+-b, i+-b              |                    a, a+-i, a+-bi, a+-ib
	
	private final Pattern pattern;
	
	NumberPatterns(Pattern pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public Pattern getPattern() {
		return pattern;
	}
	
}

interface NumberPatternsConstants { //Only way to make DN accessible to the PICKY_DOUBLE enum
	String DN = "(([1-9][0-9]*|0)\\.(0*[1-9][0-9]*|0?)|\\.0*[1-9][0-9]*)", PM = "\\s*[+-]\\s*", PMDN = PM + DN;
}
