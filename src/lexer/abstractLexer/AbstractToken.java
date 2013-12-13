package lexer.abstractLexer;

import lexer.Type;


/**
 * This uses non-atomic S-expressions car & cdr
 * 
 * @author Joshua Lipstone and Sean Mullan
 */
public abstract class AbstractToken implements Comparable<AbstractToken>, Cloneable {
	protected Type<?> carType, cdrType;
	protected Object car, cdr;
	protected AbstractToken previous;
	
	public AbstractToken(Object car, Type<?> carType, Object cdr, Type<?> cdrType) {
		this.carType = carType;
		this.car = car;
		if (car instanceof AbstractToken)
			((AbstractToken) car).previous = null;
		this.cdrType = cdrType;
		this.cdr = cdr;
		if (cdr instanceof AbstractToken)
			((AbstractToken) cdr).previous = this;
		previous = null;
	}
	
	public AbstractToken(Object car, Type<?> carType) {
		this(car, carType, null, Type.EMPTY);
	}
	
	public AbstractToken() {
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
	
	public AbstractToken getNextToken() {
		return (cdr == null || !(cdr instanceof AbstractToken)) ? makeNewToken() : (AbstractToken) cdr;
	}
	
	public AbstractToken getLastToken() {
		AbstractToken current = this, previous = current;
		while (!(current = current.getNextToken()).isNull())
			previous = current;
		return previous;
	}
	
	public AbstractToken getPreviousToken() {
		return previous == null ? makeNewToken() : previous;
	}
	
	public AbstractToken getFirstToken() {
		AbstractToken current = this, previous = current;
		while ((current = current.previous) != null)
			previous = current;
		return previous;
	}
	
	public boolean isNull() {
		return car == null && carType.equals(Type.EMPTY) && cdr == null && cdrType.equals(Type.EMPTY);
	}
	
	public AbstractToken append(AbstractToken next) {
		if (isNull()) {
			car = next.car;
			carType = next.carType;
			if (car instanceof AbstractToken)
				((AbstractToken) car).previous = null;
			cdr = next.cdr;
			cdrType = next.cdrType;
			if (cdr instanceof AbstractToken)
				((AbstractToken) cdr).previous = this;
			return getLastToken();
		}
		if (cdr instanceof AbstractToken)
			((AbstractToken) cdr).previous = null;
		cdrType = Type.TOKEN;
		cdr = next;
		next.previous = this;
		return getLastToken();
	}
	
	public abstract AbstractToken singular();
	
	public void replaceCar(AbstractToken token) {
		car = token.car;
		carType = token.carType;
	}
	
	@Override
	public int compareTo(AbstractToken o) {
		int result = carType.compareValues(car, o.car);
		if (result != 0)
			return result;
		return cdrType.compareValues(cdr, o.cdr);
	}
	
	@Override
	public AbstractToken clone() {
		return clone(null);
	}
	
	protected abstract AbstractToken clone(AbstractToken previous);
	
	public int length() {
		if (isNull())
			return 0;
		AbstractToken token = this;
		int length = 1;
		while (!(token = token.getNextToken()).isNull())
			length++;
		return length;
	}
	
	/**
	 * Due to how type-erasure works, this method must be initialized in subclasses with the following code:</br>
	 * <code>return new {@literal <}class name{@literal >}();</code>
	 */
	public abstract AbstractToken makeNewToken();
	
	/**
	 * Removes this token from the list and returns the one after it.
	 * 
	 * @return the next token in the list
	 */
	public AbstractToken remove() {
		if (this.previous != null) {
			previous.cdr = cdr;
			previous.cdrType = cdrType;
		}
		AbstractToken next = getNextToken();
		next.previous = previous;
		previous = null;
		cdr = null;
		cdrType = null;
		return next;
	}
}
