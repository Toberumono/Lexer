package testCases;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lexer.Action;
import lexer.Descender;
import lexer.Lexer;
import lexer.Rule;
import lexer.Type;
import lexer.errors.LexerException;

public class TestSystem {
	
	public static void main(String[] args) {
		Lexer lexer = new Lexer();
		Type<Integer> integer = new Type<>("Integer");
		Type<Double> decimal = new Type<>("Decimal");
		lexer.addRule("Integer", new Rule<Integer>(Pattern.compile("[0-9]+"), integer, new Action<Integer>() {
			@Override
			public Integer action(Matcher matcher, Lexer lexer) {
				return new Integer(matcher.group());
			}
		}));
		lexer.addRule("Decimal", new Rule<Double>(Pattern.compile("([0-9]+\\.[0-9]*|[0-9]*\\.[0-9]+)"), decimal, new Action<Double>() {
			@Override
			public Double action(Matcher matcher, Lexer lexer) {
				return new Double(matcher.group());
			}
		}));
		lexer.addDescender("Parentheses", new Descender("(", ")", Type.TOKEN, null));
		lexer.addDescender("Brackets", new Descender("[", "]", Type.TOKEN, null));
		String test = "10.0 100 (3.0 300) [51 5 6] ()";
		try {
			System.out.println(test + " -> " + lexer.lex(test));
		}
		catch (LexerException e) {
			System.out.println(test + " -> " + e.getMessage());
			e.printStackTrace();
		}
	}
}
