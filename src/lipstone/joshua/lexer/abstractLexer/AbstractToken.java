package lipstone.joshua.lexer.abstractLexer;

import lipstone.joshua.lexer.Type;

public abstract class AbstractToken<T extends AbstractType<?, T>, V extends AbstractToken<T, V>> implements Comparable<V>, Cloneable {
	protected T carType, cdrType;
	protected Object car, cdr;
	protected V previous;
	protected final TokenConstructor<T, V> constructor;
	protected final T tokenType, emptyType;
	
	public AbstractToken(V source, V previous, TokenConstructor<T, V> constructor, T tokenType, T emptyType) {
		this(source.car, source.carType, source.cdr, source.cdrType, constructor, tokenType, emptyType);
		this.previous = (V) previous;
	}
	
	public AbstractToken(Object car, T carType, Object cdr, T cdrType, TokenConstructor<T, V> constructor, T tokenType, T emptyType) {
		this.carType = carType;
		this.car = car;
		if (car instanceof AbstractToken)
			((V) car).previous = null;
		this.cdrType = cdrType;
		this.cdr = cdr;
		if (cdr instanceof AbstractToken)
			((V) cdr).previous = (V) this;
		previous = null;
		this.constructor = constructor;
		this.tokenType = tokenType;
		this.emptyType = emptyType;
	}
	
	public AbstractToken(Object car, T carType, TokenConstructor<T, V> constructor, T tokenType, T emptyType) {
		this.car = car;
		this.carType = carType;
		this.cdr = null;
		this.cdrType = emptyType;
		this.constructor = constructor;
		this.tokenType = tokenType;
		this.emptyType = emptyType;
	}
	
	/**
	 * Creates an empty <tt>AbstractToken</tt>
	 * 
	 * @param constructor
	 *            the constructor for the <tt>Token</tt> type that extends this one
	 * @param tokenType
	 *            the <tt>Type</tt> that represents <tt>Token</tt> type that extends this one
	 * @param emptyType
	 *            the <tt>Type</tt> that represents an empty (or null) value in the <tt>Token</tt> type that extends this one
	 */
	public AbstractToken(TokenConstructor<T, V> constructor, T tokenType, T emptyType) {
		this(null, emptyType, constructor, tokenType, emptyType);
	}
	
	/**
	 * @return the car value of this {@link AbstractToken}
	 * @see #getCarType()
	 */
	public Object getCar() {
		return car;
	}
	
	/**
	 * @return the {@link Type} of the car value of this {@link AbstractToken}
	 * @see #getCar()
	 */
	public T getCarType() {
		return carType;
	}
	
	/**
	 * @return the cdr value of this {@link AbstractToken}
	 * @see #getCdrType()
	 */
	public Object getCdr() {
		return cdr;
	}
	
	/**
	 * @return the {@link Type} of the cdr value of this {@link AbstractToken}
	 * @see #getCdr()
	 */
	public T getCdrType() {
		return cdrType;
	}
	
	@Override
	public String toString() {
		return ((car != null ? carType.valueToString(car) + " " : "") + (cdr != null ? cdrType.valueToString(cdr) + " " : "")).trim();
	}
	
	/**
	 * @return the next {@link AbstractToken} in this {@link AbstractToken}'s tree structure or a new, empty
	 *         {@link AbstractToken} if there is not one.
	 * @see #getPreviousToken()
	 * @see #getLastToken()
	 */
	public V getNextToken() {
		return cdr instanceof AbstractToken ? (V) cdr : constructor.makeNewToken(null, emptyType, null, emptyType);
	}
	
	/**
	 * Determines if this {@link AbstractToken} is the last one in its tree structure.
	 * 
	 * @return <tt>true</tt> if this {@link AbstractToken}'s cdr is not on instance of {@link AbstractToken}.
	 */
	public boolean isLastToken() {
		return !(cdr instanceof AbstractToken);
	}
	
	/**
	 * @return the last {@link AbstractToken} in its tree structure. If this {@link AbstractToken} is the last one, it
	 *         returns itself.
	 * @see #getNextToken()
	 */
	public V getLastToken() {
		V current = (V) this;
		while (current.cdr instanceof AbstractToken)
			current = (V) current.cdr;
		return current;
	}
	
	/**
	 * @return the previous {@link AbstractToken} in this {@link AbstractToken}'s tree structure or a new, empty
	 *         {@link AbstractToken} if there is not one.
	 * @see #getNextToken()
	 * @see #getFirstToken()
	 */
	public V getPreviousToken() {
		return previous == null ? constructor.makeNewToken(null, emptyType, null, emptyType) : previous;
	}
	
	/**
	 * Determines if this {@link AbstractToken} is the first one in its tree structure.
	 * 
	 * @return <tt>true</tt> if this {@link AbstractToken}'s previous token field is null.
	 */
	public boolean isFirstToken() {
		return previous == null;
	}
	
	/**
	 * @return the first {@link AbstractToken} in its tree structure. If this {@link AbstractToken} is the first one, it
	 *         returns itself.
	 * @see #getPreviousToken()
	 */
	public V getFirstToken() {
		V current = (V) this;
		while (current.previous != null)
			current = current.previous;
		return current;
	}
	
	/**
	 * A null {@link AbstractToken} is defined as one whose carType and cdrType are both equal to the empty type.
	 * 
	 * @return true if this {@link AbstractToken} is a null {@link AbstractToken}
	 */
	public boolean isNull() {
		return carType.equals(emptyType) && cdrType.equals(emptyType);
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
		cdrType = tokenType;
		cdr = next;
		next.previous = (V) this;
		return getLastToken();
	}
	
	/**
	 * Returns a shallow copy of this <tt>AbstractToken</tt> with only the car and carType.<br>
	 * This effectively creates a <tt>AbstractToken</tt> with a pointer to the same car value of this <tt>Token</tt> but
	 * separate from the list.
	 * 
	 * @return a shallow copy of this <tt>AbstractToken</tt> that is separate from the list
	 */
	public V singular() {
		return constructor.makeNewToken(car, carType, null, emptyType);
	}
	
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
	
	/**
	 * Creates a clone of this <tt>AbstractToken's</tt> tree structure, where non-token values are not cloned.
	 * 
	 * @return a clone of this <tt>AbstractToken</tt>
	 */
	@Override
	public V clone() {
		return clone(null);
	}
	
	/**
	 * Creates a clone of this <tt>AbstractToken's</tt> tree structure, where non-token values are not cloned.
	 * 
	 * @param previous
	 *            the <tt>AbstractToken</tt> that should be set as the cloned <tt>Token</tt>'s previous value
	 * @return a clone of this <tt>AbstractToken</tt>
	 */
	protected V clone(V previous) {
		V clone = constructor.makeNewToken(car instanceof AbstractToken ? ((V) car).clone((V) this) : car, carType, cdr instanceof AbstractToken ? ((V) cdr).clone((V) this) : cdr, cdrType);
		clone.previous = previous;
		return clone;
	}
	
	/**
	 * @return the the number of {@link AbstractToken AbstractTokens} in the level of the tree structure that this
	 *         {@link AbstractToken} is on, starting from this {@link AbstractToken}.
	 */
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
	 * Removes this token from the list and returns the one after it.
	 * 
	 * @return the next token in the list as determined by {@link #getNextToken()}
	 * @see #getNextToken()
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
		cdrType = tokenType;
		return next;
	}
	
	/**
	 * Generates a {@link String} that shows the structure of this {@link AbstractToken}'s entire tree.<br>
	 * This is primarily a debugging function.
	 * 
	 * @return a {@link String} describing this {@link AbstractToken} tree's structure
	 */
	public String structureString() {
		String output = "";
		AbstractToken<T, V> current = this;
		do {
			if (current.car instanceof AbstractToken)
				output = output + current.carType.getOpen() + ((AbstractToken<?, ?>) current.car).structureString() + current.carType.getClose() + " ";
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
