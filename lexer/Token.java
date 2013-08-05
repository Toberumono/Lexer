package lexer;

public class Token implements Comparable<Token> {
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
		return carType.vts(car) + (cdr instanceof Token ? " " + cdr.toString() : (cdr == null || cdrType.equals(Type.EMPTY) ? "" : " " + cdrType.vts(cdr)));
	}
	
	@Override
	public int compareTo(Token o) {
		// TODO Auto-generated method stub
		return 0;
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
}
