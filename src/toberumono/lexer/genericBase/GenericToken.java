package toberumono.lexer.genericBase;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Generic implementation of a doubly-linked list, using a structure based on cons cells from Lisp.<br>
 * Each entry in the list contains two pointers and two types to allow for easier type checking.
 * 
 * @author Toberumono
 * @param <Ty>
 *            the implementation of {@link GenericType} used by the extending implementation.
 * @param <To>
 *            the extending implementation
 */
public abstract class GenericToken<Ty extends GenericType, To extends GenericToken<Ty, To>> implements Comparable<To>, Cloneable, Iterable<To> {
	protected Ty carType, cdrType;
	protected Object car, cdr;
	protected To previous;
	protected final TokenConstructor<Ty, To> constructor;
	protected final Ty tokenType, emptyType;
	
	/**
	 * Constructs a {@link GenericToken} based on the given source.
	 * 
	 * @param source
	 *            the source of the car/cdr values
	 * @param previous
	 *            the new {@link GenericToken GenericToken's} previous token
	 * @param constructor
	 *            the constructor to use
	 * @param tokenType
	 *            the type that indicates a token
	 * @param emptyType
	 *            the type that indicates an empty car/cdr field.
	 */
	public GenericToken(To source, To previous, TokenConstructor<Ty, To> constructor, Ty tokenType, Ty emptyType) {
		this(source.car, source.carType, source.cdr, source.cdrType, constructor, tokenType, emptyType);
		this.previous = previous;
	}
	
	/**
	 * Constructs a {@link GenericToken} with the given car and cdr values.
	 * 
	 * @param car
	 *            the car value
	 * @param carType
	 *            the car type
	 * @param cdr
	 *            the cdr value
	 * @param cdrType
	 *            the cdr type
	 * @param constructor
	 *            the constructor to use
	 * @param tokenType
	 *            the type that indicates a token
	 * @param emptyType
	 *            the type that indicates an empty car/cdr field.
	 */
	@SuppressWarnings("unchecked")
	public GenericToken(Object car, Ty carType, Object cdr, Ty cdrType, TokenConstructor<Ty, To> constructor, Ty tokenType, Ty emptyType) {
		this.carType = carType == null ? emptyType : carType;
		this.car = car;
		if (car instanceof GenericToken)
			((To) car).previous = null;
		this.cdrType = cdrType == null ? emptyType : cdrType;
		this.cdr = cdr;
		if (cdr instanceof GenericToken)
			((To) cdr).previous = (To) this;
		previous = null;
		this.constructor = constructor;
		this.tokenType = tokenType;
		this.emptyType = emptyType;
	}
	
	/**
	 * Constructs a {@link GenericToken} with the given car value and an empty cdr value.
	 * 
	 * @param car
	 *            the car value
	 * @param carType
	 *            the car type
	 * @param constructor
	 *            the constructor to use
	 * @param tokenType
	 *            the type that indicates a token
	 * @param emptyType
	 *            the type that indicates an empty car/cdr field.
	 */
	public GenericToken(Object car, Ty carType, TokenConstructor<Ty, To> constructor, Ty tokenType, Ty emptyType) {
		this(car, carType, null, emptyType, constructor, tokenType, emptyType);
	}
	
	/**
	 * Creates an empty {@link GenericToken}
	 * 
	 * @param constructor
	 *            the constructor for the <tt>Token</tt> type that extends this one
	 * @param tokenType
	 *            the <tt>Type</tt> that represents <tt>Token</tt> type that extends this one
	 * @param emptyType
	 *            the <tt>Type</tt> that represents an empty (or null) value in the <tt>Token</tt> type that extends this one
	 */
	public GenericToken(TokenConstructor<Ty, To> constructor, Ty tokenType, Ty emptyType) {
		this(null, emptyType, constructor, tokenType, emptyType);
	}
	
	/**
	 * @return the car value of this {@link GenericToken}
	 * @see #getCarType()
	 */
	public Object getCar() {
		return car;
	}
	
	/**
	 * @return the {@link GenericType Type} of the car value of this {@link GenericToken}
	 * @see #getCar()
	 */
	public Ty getCarType() {
		return carType;
	}
	
	/**
	 * @return the cdr value of this {@link GenericToken}
	 * @see #getCdrType()
	 */
	public Object getCdr() {
		return cdr;
	}
	
	/**
	 * @return the {@link GenericType Type} of the cdr value of this {@link GenericToken}
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
	 * @return the next {@link GenericToken} in this {@link GenericToken}'s tree structure or a new, empty
	 *         {@link GenericToken} if there is not one.
	 * @see #getPreviousToken()
	 * @see #getLastToken()
	 */
	@SuppressWarnings("unchecked")
	public To getNextToken() {
		return cdr instanceof GenericToken ? (To) cdr : constructor.construct(null, emptyType, null, emptyType);
	}
	
	/**
	 * Determines if this {@link GenericToken} is the last one in its tree structure.
	 * 
	 * @return <tt>true</tt> if this {@link GenericToken}'s cdr is not on instance of {@link GenericToken}.
	 */
	public boolean isLastToken() {
		return !(cdr instanceof GenericToken);
	}
	
	/**
	 * @return the last {@link GenericToken} in its tree structure. If this {@link GenericToken} is the last one, it returns
	 *         itself.
	 * @see #getNextToken()
	 */
	@SuppressWarnings("unchecked")
	public To getLastToken() {
		To current = (To) this;
		while (current.cdr instanceof GenericToken)
			current = (To) current.cdr;
		return current;
	}
	
	/**
	 * @return the previous {@link GenericToken} in this {@link GenericToken}'s tree structure or a new, empty
	 *         {@link GenericToken} if there is not one.
	 * @see #getNextToken()
	 * @see #getFirstToken()
	 */
	public To getPreviousToken() {
		return previous == null ? constructor.construct(null, emptyType, null, emptyType) : previous;
	}
	
	/**
	 * Determines if this {@link GenericToken} is the first one in its tree structure.
	 * 
	 * @return <tt>true</tt> if this {@link GenericToken}'s previous token field is null.
	 */
	public boolean isFirstToken() {
		return previous == null;
	}
	
	/**
	 * @return the first {@link GenericToken} in its tree structure. If this {@link GenericToken} is the first one, it
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
	 * A null {@link GenericToken} is defined as one whose carType and cdrType are both equal to the empty type.
	 * 
	 * @return true if this {@link GenericToken} is a null {@link GenericToken}
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
			if (car instanceof GenericToken)
				((To) car).previous = null;
			cdr = next.cdr;
			cdrType = next.cdrType;
			if (cdr instanceof GenericToken)
				((To) cdr).previous = (To) this;
			return getLastToken();
		}
		if (cdr instanceof GenericToken)
			((To) cdr).previous = null;
		cdrType = tokenType;
		cdr = next;
		next.previous = (To) this;
		return getLastToken();
	}
	
	/**
	 * Returns a shallow copy of this {@link GenericToken} with only the car and carType.<br>
	 * This effectively creates a {@link GenericToken} with a pointer to the same car value of this <tt>Token</tt> but
	 * separate from the list.
	 * 
	 * @return a shallow copy of this {@link GenericToken} that is separate from the list
	 */
	public To singular() {
		return constructor.construct(car, carType, null, emptyType);
	}
	
	/**
	 * Replaces the car value of this {@link GenericToken} with that of the given token.
	 * 
	 * @param token
	 *            the token whose car value is to be written to this {@link GenericToken}
	 */
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
	 * Creates a clone of this {@link GenericToken AbstractToken's} tree structure, where non-token values are not cloned.
	 * 
	 * @return a clone of this {@link GenericToken}
	 */
	@Override
	public To clone() {
		return clone(null);
	}
	
	/**
	 * Creates a clone of this {@link GenericToken AbstractToken's} tree structure, where non-token values are not cloned.
	 * 
	 * @param previous
	 *            the {@link GenericToken} that should be set as the cloned <tt>Token</tt>'s previous value
	 * @return a clone of this {@link GenericToken}
	 */
	@SuppressWarnings("unchecked")
	protected To clone(To previous) {
		To clone = constructor.construct(car instanceof GenericToken ? ((To) car).clone((To) this) : car, carType, cdr instanceof GenericToken ? ((To) cdr).clone((To) this) : cdr, cdrType);
		clone.previous = previous;
		return clone;
	}
	
	/**
	 * @return the the number of {@link GenericToken AbstractTokens} in the level of the tree structure that this
	 *         {@link GenericToken} is on, starting from this {@link GenericToken}.
	 */
	public int length() {
		if (isNull())
			return 0;
		GenericToken<Ty, To> token = this;
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
	 * Generates a {@link String} that shows the structure of this {@link GenericToken}'s entire tree.<br>
	 * This is primarily a debugging function.
	 * 
	 * @return a {@link String} describing this {@link GenericToken} tree's structure
	 */
	@SuppressWarnings("unchecked")
	public String structureString() {
		String output = "";
		GenericToken<Ty, To> current = this;
		do {
			if (current.car instanceof GenericToken)
				output = output + current.carType.getOpen() + ((GenericToken<?, ?>) current.car).structureString() + current.carType.getClose() + " ";
			else
				output = output + current.carType.valueToString(current.car);
			output = output + ": " + current.carType.toString() + ", ";
			current = (GenericToken<Ty, To>) current.cdr;
		} while (current instanceof GenericToken);
		if (output.length() > 0)
			output = output.substring(0, output.length() - 2);
		return output;
	}
	
	/**
	 * Generates the hash by calling {@link java.util.Objects#hash(Object...) Objects.hash(Object...)} on the car, carType,
	 * cdr, and cdrType of this {@link GenericToken}.<br>
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(car, carType, cdr, cdrType);
	}
	
	/**
	 * {@inheritDoc}<br>
	 * <i>The first call to {@link java.util.Iterator#next() next()} returns this {@link GenericToken token}</i>
	 * 
	 * @return an {@link java.util.Iterator#next() next()} that iterates through the {@link GenericToken token} tree at the
	 *         level of the {@link GenericToken token} that it was created on.
	 */
	@Override
	public Iterator<To> iterator() {
		return new Iterator<To>() {
			private To last = constructor.construct(null, emptyType, GenericToken.this, tokenType);
			
			@Override
			public boolean hasNext() {
				return last.cdr instanceof GenericToken;
			}
			
			@Override
			@SuppressWarnings("unchecked")
			public To next() {
				if (!(last.cdr instanceof GenericToken))
					throw new NoSuchElementException();
				return (last = (To) last.cdr).singular();
			}
			
		};
	}
}
