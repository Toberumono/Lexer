package testCases;

import java.util.regex.Pattern;

import toberumono.lexer.Descender;
import toberumono.lexer.Lexer;
import toberumono.lexer.Rule;
import toberumono.lexer.util.DefaultIgnorePatterns;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A simple testing class.
 * 
 * @author Toberumono
 */
public class TestSystem {
	
	/**
	 * The main method.
	 * 
	 * @param args
	 *            this is ignored
	 */
	public static void main(String[] args) {
		Lexer lexer = new Lexer(DefaultIgnorePatterns.SPACES);
		final ConsType integer = new ConsType("Integer");
		final ConsType decimal = new ConsType("Decimal");
		lexer.addRule("Integer", new Rule(Pattern.compile("[0-9]+"), (l, s, match) -> new ConsCell(new Integer(match.group()), integer)));
		lexer.addRule("Decimal", new Rule(Pattern.compile("([0-9]+\\.[0-9]*|[0-9]*\\.[0-9]+)"), (l, s, match) -> new ConsCell(new Double(match.group()), decimal)));
		lexer.addDescender("Parentheses", new Descender("(", ")", new ConsType("Parentheses", "(", ")")));
		lexer.addDescender("Brackets", new Descender("[", "]", new ConsType("Brackets", "[", "]")));
		lexer.addIgnore("Newline", Pattern.compile("\n+"));
		String test = "10.0 100 ((3.0 300\n)) [51 5 6] ()";
		try {
			ConsCell token = lexer.lex(test);
			System.out.println(test + " -> " + token.structureString());
			System.out.println("length: " + token.length());
			for (ConsCell t : token)
				System.out.println(t);
		}
		catch (Exception e) {
			System.out.println(test + " -> " + e.getMessage());
			e.printStackTrace();
		}
	}
}
