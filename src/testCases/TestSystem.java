package testCases;

import java.util.regex.Matcher;

import lexer.Descender;
import lexer.Lexer;
import lexer.Rule;
import lexer.Token;
import lexer.Type;
import lexer.errors.LexerException;

public class TestSystem {
	
	public static void main(String[] args) {
		Lexer lexer = new Lexer();
		final Type<Integer> integer = new Type<>("Integer");
		final Type<Double> decimal = new Type<>("Decimal");
		lexer.addRule("Integer", new Rule<Integer>("[0-9]+", integer) {
			@Override
			public Token apply(Matcher matcher, Lexer lexer) {
				return new Token(new Integer(matcher.group()), type);
			}
		});
		lexer.addRule("Decimal", new Rule<Double>("([0-9]+\\.[0-9]*|[0-9]*\\.[0-9]+)", decimal) {
			@Override
			public Token apply(Matcher matcher, Lexer lexer) {
				return new Token(new Double(matcher.group()), type);
			}
		});
		lexer.addDescender("Parentheses", new Descender("(", ")", new Type<Token>("Parentheses")));
		lexer.addDescender("Brackets", new Descender("[", "]", new Type<Token>("Brackets")));
		lexer.ignore("Newline", "\n");
		String test = "10.0 100 (3.0 300\n) [51 5 6] ()";
		try {
			System.out.println(test + " -> " + lexer.lex(test).printStructure());
		}
		catch (LexerException e) {
			System.out.println(test + " -> " + e.getMessage());
			e.printStackTrace();
		}
	}
}
