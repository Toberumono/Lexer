package lexer;

/**
 * Token class that uses non-atomic S-expressions car & cdr
 * 
 * @author Joshua Lipstone and Sean Mullan
 */
public class Token implements Comparable<Token>, Cloneable {
	private Type<?> carType, cdrType;
	private Object car, cdr;
	private Token previous;
	
	public Token(Object car, Type<?> carType, Object cdr, Type<?> cdrType) {
		this.carType = carType;
		this.car = car;
		if (car instanceof Token)
			((Token) car).previous = null;
		this.cdrType = cdrType;
		this.cdr = cdr;
		if (cdr instanceof Token)
			((Token) cdr).previous = this;
		previous = null;
	}
	
	public Token(Object car, Type<?> carType) {
		this(car, carType, null, Type.EMPTY);
	}
	
	public Token() {
		this(null, Type.EMPTY, null, Type.EMPTY);
	}
	
	public Type<?> getCarType() {
		return carType;
	}
	
	public Object getCar() {
		return car;
	}
	
	public Type<?> getCdrType() {
		return cdrType;
	}
	
	public Object getCdr() {
		return cdr;
	}
	
	@Override
	public String toString() {
		return ((car != null ? carType.valueToString(car) : "") + (cdr != null ? cdrType.valueToString(cdr) : "")).trim();
	}
	
	public Token getNextToken() {
		return (cdr == null || !(cdr instanceof Token)) ? new Token() : (Token) cdr;
	}
	
	public Token getLastToken() {
		Token current = this, previous = current;
		while (!(current = current.getNextToken()).isNull())
			previous = current;
		return previous;
	}
	
	public Token getPreviousToken() {
		return previous == null ? new Token() : previous;
	}
	
	public Token getFirstToken() {
		Token current = this, previous = current;
		while ((current = current.previous) != null)
			previous = current;
		return previous;
	}
	
	public boolean isNull() {
		return car == null && carType.equals(Type.EMPTY) && cdr == null && cdrType.equals(Type.EMPTY);
	}
	
	public Token append(Token next) {
		if (isNull()) {
			car = next.car;
			carType = next.carType;
			if (car instanceof Token)
				((Token) car).previous = null;
			cdr = next.cdr;
			cdrType = next.cdrType;
			if (cdr instanceof Token)
				((Token) cdr).previous = this;
			return getLastToken();
		}
		if (cdr instanceof Token)
			((Token) cdr).previous = null;
		cdrType = Type.TOKEN;
		cdr = next;
		next.previous = this;
		return getLastToken();
	}
	
	public Token singular() {
		return new Token(carType.clone(car), carType);
	}
	
	public void replaceCar(Token token) {
		car = token.car;
		carType = token.carType;
	}
	
	@Override
	public int compareTo(Token o) {
		int result = carType.compareValues(car, o.car);
		if (result != 0)
			return result;
		return cdrType.compareValues(cdr, o.cdr);
	}
	
	@Override
	public Token clone() {
		return clone(null);
	}
	
	private Token clone(Token previous) {
		Token clone = new Token(carType.clone(car), carType, cdrType.clone(cdr), cdrType);
		clone.previous = previous;
		return clone;
	}
	
	public int length() {
		if (isNull())
			return 0;
		Token token = this;
		int length = 1;
		while (!(token = token.getNextToken()).isNull())
			length++;
		return length;
	}
	
	/**
	 * Removes this token from the list and returns the one after it.
	 * 
	 * @return the next token in the list
	 */
	public Token remove() {
		if (this.previous != null) {
			previous.cdr = cdr;
			previous.cdrType = cdrType;
		}
		Token next = getNextToken();
		next.previous = previous;
		previous = null;
		cdr = null;
		cdrType = null;
		return next;
	}
}
