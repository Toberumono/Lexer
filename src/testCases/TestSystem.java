package testCases;

import java.util.regex.Matcher;

import lexer.Action;
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
		lexer.addRule("Integer", new Rule<Integer>("[0-9]+", integer, new Action<Integer>() {
			@Override
			public Token action(Matcher matcher, Lexer lexer, Type<Integer> type) {
				return new Token(new Integer(matcher.group()), type);
			}
		}));
		lexer.addRule("Decimal", new Rule<Double>("([0-9]+\\.[0-9]*|[0-9]*\\.[0-9]+)", decimal, new Action<Double>() {
			@Override
			public Token action(Matcher matcher, Lexer lexer, Type<Double> type) {
				return new Token(new Double(matcher.group()), type);
			}
		}));
		lexer.addDescender("Parentheses", new Descender("(", ")", new Type<Token>("Parentheses"), null));
		lexer.addDescender("Brackets", new Descender("[", "]", new Type<Token>("Brackets"), null));
		lexer.ignore("\n");
		String test = "10.0 100 (3.0 300\n) [51 5 6] ()";
		try {
			System.out.println(test + " -> " + lexer.lex(test));
		}
		catch (LexerException e) {
			System.out.println(test + " -> " + e.getMessage());
			e.printStackTrace();
		}
	}
}
