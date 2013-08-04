package lexer;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class Lexer {
	private final ArrayList<Rule> rules;
	private final ArrayList<Descender> descenders;
	private final ArrayList<Type> types;
	
	public Lexer() {
		rules = new ArrayList<Rule>();
		descenders = new ArrayList<Descender>();
		types = new ArrayList<Type>();
	}
	
	public ArrayList<Token> lex(String input) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		while (input.length() > 0) {
			Descender d = null;
			for (Descender descender : descenders)
				if (input.length() > descender.open.length() + descender.close.length() && input.substring(0, descender.open.length()).equals(descender.open)) {
					d = descender;
					break;
				}
			if (d != null) {
				int end = getEndIndex(input, 0, d.open, d.close);
				if (end > 0) {
					tokens.add(d.apply(input.substring(1, end), this));
					input = (end + d.close.length() == input.length()) ? "" : input.substring(end + d.close.length()).trim();
					continue;
				}
				System.err.println("There was a Descender that did not have a matching close token.");
			}
			else {
				Rule hit = null;
				String match = "";
				Matcher m;
				for (Rule rule : rules) {
					m = rule.pattern.matcher(input);
					if (!m.find() || m.start() != 0 || m.group().length() == 0)
						continue;
					hit = rule;
					if (match.length() < m.group().length())
						match = m.group();
				}
				if (hit != null) {
					tokens.add(hit.apply(match, this));
					input = input.substring(match.length()).trim();
					continue;
				}
				else {
					System.err.println("There was an unrecognized character.");
				}
			}
			//There was an error if this is reached
			System.err.println("Remaining input: " + input);
			break;
		}
		return tokens;
	}
	
	private int getEndIndex(String section, int start, String startSymbol, String endSymbol) {
		int index = -1, parenthesis = 0;
		for (int i = start; i < section.length() - startSymbol.length() + 1 && i < section.length() - endSymbol.length() + 1; i++) {
			if (section.substring(i, i + startSymbol.length()).equals(startSymbol))
				parenthesis++;
			if (section.substring(i, i + endSymbol.length()).equals(endSymbol))
				parenthesis--;
			if (parenthesis == 0) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public void addRule(Rule rule) {
		rules.add(rule);
	}
	
	public void addDescender(Descender descender) {
		descenders.add(descender);
	}
	
	/**
	 * @return the types that this lexer can find.
	 */
	public ArrayList<Type> getTypes() {
		return types;
	}
}
