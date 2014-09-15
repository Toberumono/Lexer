package lexer.abstractLexer;

import lexer.Type;

/**
 * This uses non-atomic S-expressions car & cdr
 * 
 * @author Joshua Lipstone and Sean Mullan
 */
public abstract class AbstractToken<T extends Type<?>, V extends AbstractToken<T, V>> implements Comparable<V>, Cloneable {
	protected T carType, cdrType;
	protected Object car, cdr;
	protected V previous;
	
	public AbstractToken(V source, V previous) {
		this(source.carType.cloneValue(source.car), source.carType, source.cdrType.cloneValue(source.cdr), source.cdrType);
		this.previous = (V) previous;
	}
	
	public AbstractToken(Object car, T carType, Object cdr, T cdrType) {
		this.carType = carType;
		this.car = car;
		if (car instanceof AbstractToken)
			((V) car).previous = null;
		this.cdrType = cdrType;
		this.cdr = cdr;
		if (cdr instanceof AbstractToken)
			((V) cdr).previous = (V) this;
		previous = null;
	}
	
	public AbstractToken(Object car, T carType) {
		this(car, carType, null, (T) Type.EMPTY);
	}
	
	public AbstractToken() {
		this(null, (T) Type.EMPTY, null, (T) Type.EMPTY);
	}
	
	public T getCarType() {
		return carType;
	}
	
	public Object getCar() {
		return car;
	}
	
	public T getCdrType() {
		return cdrType;
	}
	
	public Object getCdr() {
		return cdr;
	}
	
	@Override
	public String toString() {
		return ((car != null ? carType.valueToString(car) : "") + (cdr != null ? cdrType.valueToString(cdr) : "")).trim();
	}
	
	public V getNextToken() {
		return (cdr == null || !(cdr instanceof AbstractToken)) ? makeNewToken() : (V) cdr;
	}
	
	public V getLastToken() {
		V current = (V) this, previous = current;
		while (current.cdr != null && current.cdr instanceof AbstractToken)
			previous = (current = (V) current.cdr);
		return previous;
	}
	
	public V getPreviousToken() {
		return previous == null ? makeNewToken() : previous;
	}
	
	public V getFirstToken() {
		V current = (V) this, previous = current;
		while ((current = current.previous) != null)
			previous = current;
		return previous;
	}
	
	public boolean isNull() {
		return car == null && carType.equals(Type.EMPTY) && cdr == null && cdrType.equals(Type.EMPTY);
	}
	
	/**
	 * This method appends the given token or tokens to this one, and, if this token is null as defined in {@link #isNull()},
	 * overwrites this token with the first token to be appended.
	 * 
	 * @param next
	 *            the token or list of tokens to append to this token
	 * @return the token appended or the last token in the list of appended tokens
	 */
	public V append(V next) {
		if (isNull()) {
			car = next.car;
			carType = next.carType;
			if (car instanceof AbstractToken)
				((V) car).previous = null;
			cdr = next.cdr;
			cdrType = next.cdrType;
			if (cdr instanceof AbstractToken)
				((V) cdr).previous = (V) this;
			return getLastToken();
		}
		if (cdr instanceof AbstractToken)
			((V) cdr).previous = null;
		cdrType = getTokenType();
		cdr = next;
		next.previous = (V) this;
		return getLastToken();
	}
	
	/**
	 * This will likely be:</br> <code>
		return new {@literal <}class passed to V{@literal >}(carType.clone(car), carType);</code>
	 * 
	 * @return a new token that is functionally separate from this one, but contains the same car and carType.
	 */
	public abstract V singular();
	
	public void replaceCar(V token) {
		car = token.car;
		carType = token.carType;
	}
	
	@Override
	public int compareTo(V o) {
		int result = carType.compareValues(car, o.car);
		if (result != 0)
			return result;
		return cdrType.compareValues(cdr, o.cdr);
	}
	
	@Override
	public V clone() {
		return clone(null);
	}
	
	/**
	 * This should be:</br><code>
		Token clone = new Token(carType.clone(car), carType, cdrType.clone(cdr), cdrType);</br>
		clone.previous = previous;</br>
		return clone;</code>
	 */
	protected abstract V clone(V previous);
	
	public int length() {
		if (isNull())
			return 0;
		V token = (V) this;
		int length = 1;
		while (!(token = token.getNextToken()).isNull())
			length++;
		return length;
	}
	
	/**
	 * Due to how type-erasure works, this method must be initialized in subclasses with the following code:</br>
	 * <code>return new {@literal <}class name{@literal >}();</code>
	 */
	public abstract V makeNewToken();
	
	/**
	 * Removes this token from the list and returns the one after it.
	 * 
	 * @return the next token in the list
	 */
	public V remove() {
		if (this.previous != null) {
			previous.cdr = cdr;
			previous.cdrType = cdrType;
		}
		V next = getNextToken();
		next.previous = previous;
		previous = null;
		cdr = null;
		cdrType = null;
		return next;
	}
	
	/**
	 * This should have the following code:</br> <code>return <name-of-class-for-Type>.TOKEN</code>
	 */
	protected abstract T getTokenType();
	
	public String printStructure() {
		String output = "";
		AbstractToken<T, V> current = this;
		do {
			if (current.car instanceof AbstractToken)
				output = output + current.carType.getOpen() + ((AbstractToken<?, ?>) current.car).printStructure() + current.carType.getClose() + " ";
			else
				output = output + current.carType.valueToString(current.car);
			output = output + ": " + current.carType.toString() + ", ";
			current = (AbstractToken<T, V>) current.cdr;
		} while (current instanceof AbstractToken);
		if (output.length() > 0)
			output = output.substring(0, output.length() - 2);
		return output;
	}
}
