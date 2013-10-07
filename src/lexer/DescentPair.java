package lexer;

class DescentPair {
	private final String input;
	private final int head;
	
	public DescentPair(String input, int head) {
		this.input = input;
		this.head = head;
	}
	
	public String getInput() {
		return input;
	}
	
	public int getHead() {
		return head;
	}
}
