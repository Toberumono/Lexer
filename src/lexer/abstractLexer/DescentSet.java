package lexer.abstractLexer;


class DescentSet<T extends AbstractToken> {
	private final String input;
	private final int head;
	private final T output;
	
	public DescentSet(String input, int head, T output) {
		this.input = input;
		this.head = head;
		this.output = output;
	}
	
	public String getInput() {
		return input;
	}
	
	public int getHead() {
		return head;
	}
	
	public T getOutput() {
		return output;
	}
}
