package testCases;

import toberumono.lexer.BasicDescender;
import toberumono.lexer.BasicLexer;
import toberumono.lexer.BasicRule;
import toberumono.lexer.util.DefaultIgnorePatterns;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

import static toberumono.lexer.util.NumberPatterns.*;

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
		BasicLexer lexer = new BasicLexer(DefaultIgnorePatterns.WHITESPACE);
		final ConsType integer = new ConsType("Integer");
		final ConsType decimal = new ConsType("Decimal");
		lexer.addRule("Integer", new BasicRule(INTEGER.getPattern(), (l, s, match) -> new ConsCell(new Integer(match.group()), integer)));
		lexer.addRule("Decimal", new BasicRule(DOUBLE.getPattern(), (l, s, match) -> new ConsCell(new Double(match.group()), decimal)));
		lexer.addDescender("Parentheses", new BasicDescender("(", ")", new ConsType("Parentheses", "(", ")")));
		lexer.addDescender("Brackets", new BasicDescender("[", "]", new ConsType("Brackets", "[", "]")));
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
