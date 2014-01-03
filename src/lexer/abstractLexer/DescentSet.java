package lexer.abstractLexer;

class DescentSet<T extends AbstractToken<?, T>> {
	private final String input;
	private final int head;
	private final T output, previous;
	
	public DescentSet(String input, int head, T output, T previous) {
		this.input = input;
		this.head = head;
		this.output = output;
		this.previous = previous;
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
	
	public T getPrevious() {
		return previous;
	}
}
