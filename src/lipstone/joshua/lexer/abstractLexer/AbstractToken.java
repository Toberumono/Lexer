package lipstone.joshua.lexer.abstractLexer;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Generic implementation of a doubly-linked list, using a structure based on cons cells from Lisp.<br>
 * Each entry in the list contains two pointers and two types to allow for easier type checking.
 * 
 * @author Joshua Lipstone
 * @param <Ty>
 *            the implementation of {@link AbstractType} used by the extending implementation.
 * @param <To>
 *            the extending implementation
 */
public abstract class AbstractToken<Ty extends GenericType, To extends AbstractToken<Ty, To>> implements Comparable<To>, Cloneable, Iterable<To> {
	protected Ty carType, cdrType;
	protected Object car, cdr;
	protected To previous;
	protected final TokenConstructor<Ty, To> constructor;
	protected final Ty tokenType, emptyType;
	
	public AbstractToken(To source, To previous, TokenConstructor<Ty, To> constructor, Ty tokenType, Ty emptyType) {
		this(source.car, source.carType, source.cdr, source.cdrType, constructor, tokenType, emptyType);
		this.previous = previous;
	}
	
	@SuppressWarnings("unchecked")
	public AbstractToken(Object car, Ty carType, Object cdr, Ty cdrType, TokenConstructor<Ty, To> constructor, Ty tokenType, Ty emptyType) {
		this.carType = carType;
		this.car = car;
		if (car instanceof AbstractToken)
			((To) car).previous = null;
		this.cdrType = cdrType;
		this.cdr = cdr;
		if (cdr instanceof AbstractToken)
			((To) cdr).previous = (To) this;
		previous = null;
		this.constructor = constructor;
		this.tokenType = tokenType;
		this.emptyType = emptyType;
	}
	
	public AbstractToken(Object car, Ty carType, TokenConstructor<Ty, To> constructor, Ty tokenType, Ty emptyType) {
		this(car, carType, null, emptyType, constructor, tokenType, emptyType);
	}
	
	/**
	 * Creates an empty {@link AbstractToken}
	 * 
	 * @param constructor
	 *            the constructor for the <tt>Token</tt> type that extends this one
	 * @param tokenType
	 *            the <tt>Type</tt> that represents <tt>Token</tt> type that extends this one
	 * @param emptyType
	 *            the <tt>Type</tt> that represents an empty (or null) value in the <tt>Token</tt> type that extends this one
	 */
	public AbstractToken(TokenConstructor<Ty, To> constructor, Ty tokenType, Ty emptyType) {
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
	 * @return the {@link AbstractType} of the car value of this {@link AbstractToken}
	 * @see #getCar()
	 */
	public Ty getCarType() {
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
	 * @return the {@link AbstractType} of the cdr value of this {@link AbstractToken}
	 * @see #getCdr()
	 */
	public Ty getCdrType() {
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
	@SuppressWarnings("unchecked")
	public To getNextToken() {
		return cdr instanceof AbstractToken ? (To) cdr : constructor.makeNewToken(null, emptyType, null, emptyType);
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
	@SuppressWarnings("unchecked")
	public To getLastToken() {
		To current = (To) this;
		while (current.cdr instanceof AbstractToken)
			current = (To) current.cdr;
		return current;
	}
	
	/**
	 * @return the previous {@link AbstractToken} in this {@link AbstractToken}'s tree structure or a new, empty
	 *         {@link AbstractToken} if there is not one.
	 * @see #getNextToken()
	 * @see #getFirstToken()
	 */
	public To getPreviousToken() {
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
	public To getFirstToken() {
		@SuppressWarnings("unchecked")
		To current = (To) this;
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
	@SuppressWarnings("unchecked")
	public To append(To next) {
		if (isNull()) {
			car = next.car;
			carType = next.carType;
			if (car instanceof AbstractToken)
				((To) car).previous = null;
			cdr = next.cdr;
			cdrType = next.cdrType;
			if (cdr instanceof AbstractToken)
				((To) cdr).previous = (To) this;
			return getLastToken();
		}
		if (cdr instanceof AbstractToken)
			((To) cdr).previous = null;
		cdrType = tokenType;
		cdr = next;
		next.previous = (To) this;
		return getLastToken();
	}
	
	/**
	 * Returns a shallow copy of this {@link AbstractToken} with only the car and carType.<br>
	 * This effectively creates a {@link AbstractToken} with a pointer to the same car value of this <tt>Token</tt> but
	 * separate from the list.
	 * 
	 * @return a shallow copy of this {@link AbstractToken} that is separate from the list
	 */
	public To singular() {
		return constructor.makeNewToken(car, carType, null, emptyType);
	}
	
	public void replaceCar(To token) {
		car = token.car;
		carType = token.carType;
	}
	
	@Override
	public int compareTo(To o) {
		int result = carType.compareValues(car, o.car);
		if (result != 0)
			return result;
		return cdrType.compareValues(cdr, o.cdr);
	}
	
	/**
	 * Creates a clone of this {@link AbstractToken AbstractToken's} tree structure, where non-token values are not cloned.
	 * 
	 * @return a clone of this {@link AbstractToken}
	 */
	@Override
	public To clone() {
		return clone(null);
	}
	
	/**
	 * Creates a clone of this {@link AbstractToken AbstractToken's} tree structure, where non-token values are not cloned.
	 * 
	 * @param previous
	 *            the {@link AbstractToken} that should be set as the cloned <tt>Token</tt>'s previous value
	 * @return a clone of this {@link AbstractToken}
	 */
	@SuppressWarnings("unchecked")
	protected To clone(To previous) {
		To clone = constructor.makeNewToken(car instanceof AbstractToken ? ((To) car).clone((To) this) : car, carType, cdr instanceof AbstractToken ? ((To) cdr).clone((To) this) : cdr, cdrType);
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
		AbstractToken<Ty, To> token = this;
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
	public To remove() {
		if (this.previous != null) {
			previous.cdr = cdr;
			previous.cdrType = cdrType;
		}
		To next = getNextToken();
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
	@SuppressWarnings("unchecked")
	public String structureString() {
		String output = "";
		AbstractToken<Ty, To> current = this;
		do {
			if (current.car instanceof AbstractToken)
				output = output + current.carType.getOpen() + ((AbstractToken<?, ?>) current.car).structureString() + current.carType.getClose() + " ";
			else
				output = output + current.carType.valueToString(current.car);
			output = output + ": " + current.carType.toString() + ", ";
			current = (AbstractToken<Ty, To>) current.cdr;
		} while (current instanceof AbstractToken);
		if (output.length() > 0)
			output = output.substring(0, output.length() - 2);
		return output;
	}
	
	/**
	 * Generates the hash by calling {@link java.util.Objects#hash(Object...) Objects.hash(Object...)} on the car, carType,
	 * cdr, and cdrType of this {@link AbstractToken}.<br>
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(car, carType, cdr, cdrType);
	}
	
	/**
	 * {@inheritDoc}<br>
	 * <i>The first call to {@link java.util.Iterator#next() next()} returns this {@link AbstractToken token}</i>
	 * 
	 * @return an {@link java.util.Iterator#next() next()} that iterates through the {@link AbstractToken token} tree at the
	 *         level of the {@link AbstractToken token} that it was created on.
	 */
	@Override
	public Iterator<To> iterator() {
		return new Iterator<To>() {
			private To last = constructor.makeNewToken(null, emptyType, AbstractToken.this, tokenType);
			
			@Override
			public boolean hasNext() {
				return last.cdr instanceof AbstractToken;
			}
			
			@Override
			@SuppressWarnings("unchecked")
			public To next() {
				if (!(last.cdr instanceof AbstractToken))
					throw new NoSuchElementException();
				return (last = (To) last.cdr).singular();
			}
			
		};
	}
}
