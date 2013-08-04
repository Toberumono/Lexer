package testCases;

import java.util.ArrayList;
import java.util.regex.Pattern;

import lexer.Action;
import lexer.Descender;
import lexer.Lexer;
import lexer.Rule;
import lexer.Token;
import lexer.Type;

public class TestSystem {
	
	public static void main(String[] args) {
		Lexer lexer = new Lexer();
		Type integer = new Type("Integer") {
			@Override
			public int compareValues(Object value1, Object value2) {
				return ((Integer) value1).compareTo((Integer) value2);
			}
		};
		Type decimal = new Type("Decimal") {
			@Override
			public int compareValues(Object value1, Object value2) {
				return ((Double) value1).compareTo((Double) value2);
			}
		};
		Type parentheses = new Type("Parentheses") {
			@Override
			public String valueToString(Object value) {
				ArrayList<Token> tokens = (ArrayList<Token>) value;
				String output = "(";
				for (Token token : tokens)
					output = output + token.toString() + " ";
				if (output.length() > 1)
					output = output.substring(0, output.length() - 1);
				output = output + ")";
				return output;
			}

			@Override
			public int compareValues(Object value1, Object value2) {
				return 0;
			}
		};
		lexer.addRule(new Rule(Pattern.compile("[0-9]+"), integer, new Action() {
			@Override
			public Object action(String input, Lexer lexer) {
				return new Integer(input);
			}
		}));
		lexer.addRule(new Rule(Pattern.compile("([0-9]+\\.[0-9]*|[0-9]*\\.[0-9]+)"), decimal, new Action() {
			@Override
			public Object action(String input, Lexer lexer) {
				return new Double(input);
			}
		}));
		lexer.addDescender(new Descender("(", ")", parentheses, null));
		String test = "10.0 100 (3.0 300)";
		System.out.println(test + " -> " + lexer.lex(test));
	}
}
