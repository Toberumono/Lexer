package lexer;

import java.util.ArrayList;
import java.util.Stack;
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
		Stack<Token> descendStack = new Stack<>();
		Token head = new Token(), output = head;
		while (input.length() > 0) {
			Descender d = null;
			for (Descender descender : descenders.getValues())
				if (input.length() >= descender.open.length() && input.substring(0, descender.open.length()).equals(descender.open)) {
					d = descender;
					break;
				}
			if (d != null) {
				Token descent = new Token();
				head = head.append(new Token(descent, d.getType()));
				descendStack.push(head);
				head = descent;
				input = input.substring(d.open.length());
				continue;
			}
			else {
				for (Descender descender : descenders.getValues())
					if (input.length() >= descender.close.length() && input.substring(0, descender.close.length()).equals(descender.close)) {
						d = descender;
						break;
					}
				if (d != null) {
					if (descendStack.isEmpty() || descendStack.peek().getCarType() != d.getType())
						System.err.println("There was an unbalanced descender in the input.");
					else {
						input = input.substring(d.close.length());
						head = descendStack.pop();
						continue;
					}
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
		}
		return output;
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
