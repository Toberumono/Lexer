package testCases;

import java.util.regex.Pattern;

import lipstone.joshua.lexer.Descender;
import lipstone.joshua.lexer.Lexer;
import lipstone.joshua.lexer.Rule;
import lipstone.joshua.lexer.Token;
import lipstone.joshua.lexer.Type;

public class TestSystem {
	
	public static void main(String[] args) {
		Lexer lexer = new Lexer();
		final Type<Integer> integer = new Type<>("Integer");
		final Type<Double> decimal = new Type<>("Decimal");
		lexer.addRule("Integer", new Rule(Pattern.compile("[0-9]+"), (match, l) -> {return new Token(new Integer(match.group()), integer);}));
		lexer.addRule("Decimal", new Rule(Pattern.compile("([0-9]+\\.[0-9]*|[0-9]*\\.[0-9]+)"), (match, l) -> {return new Token(new Double(match.group()), decimal);}));
		lexer.addDescender("Parentheses", new Descender("(", ")", new Type<Token>("Parentheses", "(", ")")));
		lexer.addDescender("Brackets", new Descender("[", "]", new Type<Token>("Brackets", "[", "]")));
		lexer.ignore("Newline", Pattern.compile("\n+"));
		String test = "10.0 100 ((3.0 300\n)) [51 5 6] ()";
		try {
			System.out.println(test + " -> " + lexer.lex(test).printStructure());
		}
		catch (Exception e) {
			System.out.println(test + " -> " + e.getMessage());
			e.printStackTrace();
		}
	}
}
