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
@SuppressWarnings("unchecked")
public abstract class GenericToken<Ty extends GenericType, To extends GenericToken<Ty, To>> implements Comparable<To>, Cloneable, Iterable<To> {
	protected Ty carType, cdrType;
	protected Object car, cdr;
	protected To previous;
	protected final TokenConstructor<Ty, To> constructor;
	protected final Ty tokenType, emptyType;
	
	/**
	 * Constructs a {@link GenericToken Token} based on the given source.
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
	 * Constructs a {@link GenericToken Token} with the given car and cdr values.
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
	 * Constructs a {@link GenericToken Token} with the given car value and an empty cdr value.
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
	 * Creates an empty {@link GenericToken Token}
	 * 
	 * @param constructor
	 *            the constructor for the <tt>Token</tt> type that extends this one
	 * @param tokenType
	 *            the <tt>GenericType</tt> that represents <tt>Token</tt> type that extends this one
	 * @param emptyType
	 *            the <tt>GenericType</tt> that represents an empty (or null) value in the <tt>Token</tt> type that extends
	 *            this one
	 */
	public GenericToken(TokenConstructor<Ty, To> constructor, Ty tokenType, Ty emptyType) {
		this(null, emptyType, constructor, tokenType, emptyType);
	}
	
	/**
	 * @return the car value of this {@link GenericToken Token}
	 * @see #getCarType()
	 */
	public Object getCar() {
		return car;
	}
	
	/**
	 * @return the {@link GenericType GenericType} of the car value of this {@link GenericToken Token}
	 * @see #getCar()
	 */
	public Ty getCarType() {
		return carType;
	}
	
	/**
	 * @return the cdr value of this {@link GenericToken Token}
	 * @see #getCdrType()
	 */
	public Object getCdr() {
		return cdr;
	}
	
	/**
	 * @return the {@link GenericType GenericType} of the cdr value of this {@link GenericToken Token}
	 * @see #getCdr()
	 */
	public Ty getCdrType() {
		return cdrType;
	}
	
	@Override
	public String toString() {
		return ((car != null ? carType.valueToString(car) + " " : "") + (cdr != null ? cdrType.valueToString(cdr) + " " : "")).trim();
	}
	
	/* ************************************************ */
	/*                  CHECK FUNCTIONS                 */
	/* ************************************************ */
	
	/**
	 * Determines if the length of the current level of the tree structure starting with this {@link GenericToken Token} has
	 * a length of at least <tt>length</tt> (effectively, are there at least <tt>length</tt>-1 {@link GenericToken Tokens}
	 * after this {@link GenericToken Token}?).<br>
	 * If <tt>length</tt> is negative, it performs the same test, but scans backwards (effectively, are there at least |
	 * <tt>length</tt>|-1 {@link GenericToken Tokens} before this {@link GenericToken Token}?).
	 * 
	 * @param length
	 *            the length to test for
	 * @return {@code true} if there are at least <tt>length</tt>-1 {@link GenericToken Tokens} after this
	 *         {@link GenericToken Token} if <tt>length</tt> is at least 0 or if there are at least |<tt>length</tt>|-1
	 *         {@link GenericToken Tokens} before this {@link GenericToken Token}, otherwise it returns {@code false}
	 */
	public boolean hasLength(int length) {
		if (length < 0) {
			if (++length == 0)
				return true;
			for (GenericToken<Ty, To> cur = this; length < 0; cur = cur.getPreviousToken(), length++)
				if (cur == null)
					return false;
			return true;
		}
		if (--length == 0)
			return true;
		for (GenericToken<Ty, To> cur = this; length > 0; cur = cur.getNextToken(), length--)
			if (cur == null)
				return false;
		return true;
	}
	
	/**
	 * Determines if this {@link GenericToken Token} is the first one in its tree structure.
	 * 
	 * @return <tt>true</tt> if this {@link GenericToken Token}'s previous token field is null.
	 */
	public boolean isFirstToken() {
		return previous == null;
	}
	
	/**
	 * A null {@link GenericToken Token} is defined as one whose carType and cdrType are both equal to the empty type.
	 * 
	 * @return {@code true} if this {@link GenericToken Token} is a null (or empty) {@link GenericToken Token}
	 */
	public boolean isNull() {
		return carType.equals(emptyType) && cdrType.equals(emptyType);
	}
	
	/**
	 * Determines if this {@link GenericToken Token} is the last one in its tree structure.
	 * 
	 * @return <tt>true</tt> if this {@link GenericToken Token}'s cdr is not on instance of {@link GenericToken Token}.
	 */
	public boolean isLastToken() {
		return !(cdr instanceof GenericToken);
	}
	
	/* ************************************************ */
	/*              MODIFICATION FUNCTIONS              */
	/* ************************************************ */
	
	/**
	 * Sets the {@link #car} and {@link #carType} of this token to the given values.<br>
	 * This method returns the token it was called on for chaining purposes.
	 * 
	 * @param car
	 *            the new {@link #car} value
	 * @param carType
	 *            the {@link GenericType GenericType} of the new {@link #car} value
	 * @return {@code this}
	 * @see #getCar()
	 * @see #getCarType()
	 * @see #setCdr(Object, GenericType)
	 */
	public To setCar(Object car, Ty carType) {
		this.car = car;
		this.carType = carType == null ? emptyType : carType;
		return (To) this;
	}
	
	/**
	 * Sets the {@link #cdr} and {@link #cdrType} of this token to the given values. If the {@link #cdr} being replaced is a
	 * token, then the value of that token's {@link #previous} field is set to {@code null}.<br>
	 * This method returns the token it was called on for chaining purposes.
	 * 
	 * @param cdr
	 *            the new {@link #cdr} value
	 * @param cdrType
	 *            the {@link GenericType GenericType} new {@link #cdr} value
	 * @return {@code this}
	 * @see #getCdr()
	 * @see #getCdrType()
	 * @see #setCar(Object, GenericType)
	 */
	public To setCdr(Object cdr, Ty cdrType) {
		if (this.cdrType == tokenType)
			((To) cdr).previous = null;
		this.cdr = cdr;
		this.cdrType = cdrType == null ? emptyType : cdrType;
		return (To) this;
	}
	
	/**
	 * This method appends the given token or tokens to this one, and, if this token is null as defined in {@link #isNull()},
	 * overwrites this the contents of this token with the first token to be appended.<br>
	 * If this token's cdr is also a token, then {@link #insert(GenericToken)} is recursively called on the last token in the
	 * inserted token tree's root level (found via a call to {@link #getLastToken()}) with this token's cdr as the argument.
	 * If this token's cdr is not empty and is not a token, then the cdr of the last token in the given token or tokens' cdr
	 * is set to this token's cdr.
	 * 
	 * @param next
	 *            the token to insert
	 * @return <tt>this</tt> if {@code this.isNull()} was {@code true}, otherwise <tt>next</tt>
	 * @see #append(GenericToken)
	 */
	public To insert(To next) {
		if (isNull()) {
			next.setPrevious(null);
			car = next.car;
			carType = next.carType;
			if (next.cdrType == next.tokenType)
				((To) next.cdr).setPrevious((To) this);
			cdr = next.cdr;
			cdrType = next.cdrType;
			return (To) this;
		}
		next.setPrevious((To) this);
		if (cdrType == emptyType) {
			//If this was a terminal token, just set the cdr to point to next
			cdr = next;
			cdrType = next.tokenType;
			return next;
		}
		To out = next.getLastToken();
		if (cdrType == tokenType)
			out = out.insert((To) cdr);
		else
			out.setCdr(cdr, cdrType);
		cdr = next;
		cdrType = next.tokenType;
		return next;
	}
	
	/**
	 * Appends the given token to this token.<br>
	 * Roughly equivalent in function to {@link #insert(GenericToken)} - the only difference is that this returns the last
	 * {@link GenericToken Token} in the equivalent level of the resulting tree.
	 * 
	 * @param next
	 *            the token to insert
	 * @return the last token in the equivalent level of the resulting token tree. Equivalent to
	 *         {@code token.insert(next).getLastToken()}.
	 * @see #insert(GenericToken)
	 */
	public To append(To next) {
		To out = next.getLastToken();
		if (isNull()) {
			next.setPrevious(null);
			car = next.car;
			carType = next.carType;
			if (next.cdrType == next.tokenType)
				((To) next.cdr).setPrevious((To) this);
			cdr = next.cdr;
			cdrType = next.cdrType;
			return (To) this;
		}
		next.setPrevious((To) this);
		if (cdrType == emptyType) {
			//If this was a terminal token, just set the cdr to point to next
			cdr = next;
			cdrType = next.tokenType;
			return out;
		}
		if (cdrType == tokenType)
			out = out.append((To) cdr).getLastToken();
		else
			out.setCdr(cdr, cdrType);
		cdr = next;
		cdrType = next.tokenType;
		return next;
	}
	
	/**
	 * Splits the token tree on this {@link GenericToken Token}. After the split, the original tree will end at the
	 * {@link GenericToken Token} before this one, and this {@link GenericToken Token} will be the first {@link GenericToken
	 * Token} in the new tree.
	 * 
	 * @return {@code this}
	 */
	public To split() {
		setPrevious(null);
		return (To) this;
	}
	
	protected void setPrevious(To previous) {
		if (this.previous != null)
			this.previous.setCdr(null, null);
		this.previous = previous;
	}
	
	/* ************************************************ */
	/*                MOVEMENT FUNCTIONS                */
	/* ************************************************ */
	
	/**
	 * @return the next {@link GenericToken Token} in this {@link GenericToken Token}'s tree structure or a new, empty
	 *         {@link GenericToken Token} if there is not one.
	 * @see #getPreviousToken()
	 * @see #getLastToken()
	 */
	public To getNextToken() {
		return cdr instanceof GenericToken ? (To) cdr : constructor.construct(null, emptyType, null, emptyType);
	}
	
	/**
	 * Gets the nth {@link GenericToken Token} after this {@link GenericToken Token} (e.g. {@code getNextToken(1)} is
	 * equivalent to {@code getNextToken()}).<br>
	 * If <tt>n</tt> is negative, this is equivalent to {@link #getPreviousToken(int)} with <tt>n</tt> being positive.
	 * 
	 * @param n
	 *            the distance between this {@link GenericToken Token} and the desired {@link GenericToken Token}
	 * @return the nth {@link GenericToken Token} after this {@link GenericToken Token} in the current level of the tree
	 *         structure or an empty {@link GenericToken Token} if there is no such {@link GenericToken Token}
	 */
	public To getNextToken(int n) {
		if (n < 0)
			return getPreviousToken(-n);
		To cur = (To) this;
		for (; n > 0 && !cur.isNull(); cur = cur.getNextToken(), n--);
		return cur;
	}
	
	/**
	 * @return the last {@link GenericToken Token} in its tree structure. If this {@link GenericToken Token} is the last one,
	 *         it returns itself.
	 * @see #getNextToken()
	 */
	public To getLastToken() {
		To current = (To) this;
		while (current.cdrType == tokenType)
			current = (To) current.cdr;
		return current;
	}
	
	/**
	 * @return the previous {@link GenericToken Token} in this {@link GenericToken Token}'s tree structure or a new, empty
	 *         {@link GenericToken Token} if there is not one.
	 * @see #getNextToken()
	 * @see #getFirstToken()
	 */
	public To getPreviousToken() {
		return previous == null ? constructor.construct(null, emptyType, null, emptyType) : previous;
	}
	
	/**
	 * Gets the nth {@link GenericToken Token} before this {@link GenericToken Token} (e.g. {@code getPreviousToken(1)} is
	 * equivalent to {@code getPreviousToken()}).<br>
	 * If <tt>n</tt> is negative, this is equivalent to {@link #getNextToken(int)} with <tt>n</tt> being positive.
	 * 
	 * @param n
	 *            the distance between this {@link GenericToken Token} and the desired {@link GenericToken Token}
	 * @return the nth {@link GenericToken Token} before this {@link GenericToken Token} in the current level of the tree
	 *         structure or an empty {@link GenericToken Token} if there is no such {@link GenericToken Token}
	 */
	public To getPreviousToken(int n) {
		if (n < 0)
			return getNextToken(-n);
		To cur = (To) this;
		for (; n > 0 && cur != null; cur = cur.getPreviousToken(), n--);
		return cur;
	}
	
	/**
	 * @return the first {@link GenericToken Token} in its tree structure. If this {@link GenericToken Token} is the first
	 *         one, it returns itself.
	 * @see #getPreviousToken()
	 */
	public To getFirstToken() {
		To current = (To) this;
		while (current.previous != null)
			current = current.previous;
		return current;
	}
	
	/**
	 * Returns a shallow copy of this {@link GenericToken Token} with only the car and carType.<br>
	 * This effectively creates a {@link GenericToken Token} with a pointer to the same car value of this <tt>Token</tt> but
	 * separate from the list.
	 * 
	 * @return a shallow copy of this {@link GenericToken Token} that is separate from the list
	 */
	public To singular() {
		return constructor.construct(car, carType, null, emptyType);
	}
	
	/**
	 * Replaces the car value of this {@link GenericToken Token} with that of the given token.
	 * 
	 * @param token
	 *            the token whose car value is to be written to this {@link GenericToken Token}
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
	 * Creates a clone of this {@link GenericToken GenericToken's} tree structure, where non-token values are not cloned.
	 * 
	 * @return a clone of this {@link GenericToken Token}
	 */
	@Override
	public To clone() {
		return clone(null);
	}
	
	/**
	 * Creates a clone of this {@link GenericToken GenericToken's} tree structure, where non-token values are not cloned.
	 * 
	 * @param previous
	 *            the {@link GenericToken Token} that should be set as the cloned <tt>Token</tt>'s previous value
	 * @return a clone of this {@link GenericToken Token}
	 */
	protected To clone(To previous) {
		To clone = constructor.construct(car instanceof GenericToken ? ((To) car).clone((To) this) : car, carType, cdr instanceof GenericToken ? ((To) cdr).clone((To) this) : cdr, cdrType);
		clone.previous = previous;
		return clone;
	}
	
	/**
	 * @return the the number of {@link GenericToken AbstractTokens} in the level of the tree structure that this
	 *         {@link GenericToken Token} is on, starting from this {@link GenericToken Token}.
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
	 * Generates a {@link String} that shows the structure of this {@link GenericToken Token}'s entire tree.<br>
	 * This is primarily a debugging function.
	 * 
	 * @return a {@link String} describing this {@link GenericToken Token} tree's structure
	 */
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
	 * cdr, and cdrType of this {@link GenericToken Token}.<br>
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
			public To next() {
				if (!(last.cdr instanceof GenericToken))
					throw new NoSuchElementException();
				return (last = (To) last.cdr).singular();
			}
			
		};
	}
}
