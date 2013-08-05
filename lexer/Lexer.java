package lexer;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class Lexer {
	private final PairedList<String, Rule<?>> rules;
	private final PairedList<String, Descender> descenders;
	private final ArrayList<Type<Object>> types;
	
	public Lexer() {
		rules = new PairedList<>();
		descenders = new PairedList<>();
		types = new ArrayList<Type<Object>>();
	}
	
	public final Token lex(String input) {
		Token head = new Token(), output = head;
		while (input.length() > 0) {
			System.out.println(head.getFirstToken());
			Descender d = null;
			for (Descender descender : descenders.getValues())
				if (input.length() > descender.open.length() + descender.close.length() && input.substring(0, descender.open.length()).equals(descender.open)) {
					d = descender;
					break;
				}
			if (d != null) {
				int end = getEndIndex(input, 0, d.open, d.close);
				if (end > 0) {
					head = head.append(new Token(d.apply(input.substring(1, end), this), d.getType()));
					input = (end + d.close.length() == input.length()) ? "" : input.substring(end + d.close.length()).trim();
					continue;
				}
				System.err.println("There was a Descender that did not have a matching close token.");
			}
			else {
				Rule<?> hit = null;
				String match = "";
				Matcher m;
				for (Rule<?> rule : rules.getValues()) {
					m = rule.pattern.matcher(input);
					if (!m.find() || m.start() != 0 || m.group().length() == 0)
						continue;
					hit = rule;
					if (match.length() < m.group().length())
						match = m.group();
				}
				if (hit != null) {
					head = head.append(new Token(hit.apply(match, this), hit.getType()));
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
		System.out.println(head);
		return output;
	}
	
	private final int getEndIndex(String section, int start, String startSymbol, String endSymbol) {
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
	
	public final void addRule(String name, Rule<?> rule) {
		rules.put(name, rule);
	}
	
	public final void addDescender(String name, Descender descender) {
		descenders.put(name, descender);
	}
	
	/**
	 * @return the types that this lexer can find.
	 */
	public ArrayList<Type<Object>> getTypes() {
		return types;
	}
}
