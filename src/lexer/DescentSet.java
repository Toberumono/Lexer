package lexer;

class DescentSet {
	private final String input;
	private final int head;
	private final Token output;
	
	public DescentSet(String input, int head, Token output) {
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
	
	public Token getOutput() {
		return output;
	}
}
