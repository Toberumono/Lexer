package toberumono.lexer;

import toberumono.lexer.genericBase.AbstractType;

/**
 * An implementation of {@link AbstractType}.
 * 
 * @author Toberumono
 */
public class Type extends AbstractType {
	
	/**
	 * A pre-created type that <i>must</i> be used to denote empty values (e.g. the end of a list)
	 */
	public static final Type EMPTY = new Type("Empty") {
		@Override
		public String valueToString(Object value) {
			return "";
		}
		
		@Override
		public int compareValues(Object value1, Object value2) {
			return 0;
		}
	};
	
	/**
	 * A pre-created {@link Type} that flags the value as an instance of {@link Token}
	 */
	public static final Type TOKEN = new Type("Token") {
		
		@Override
		public Token cloneValue(Object value) {
			return ((Token) value).clone();
		}
	};
	
	/**
	 * Constructs a {@link Type} with the given name and descender tokens.
	 * 
	 * @param name
	 *            the name of the type
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 */
	public Type(String name, String open, String close) {
		super(name, open, close);
	}

	/**
	 * Constructs a {@link Type} with the given name and no descender tokens.
	 * 
	 * @param name
	 *            the name of the type
	 */
	public Type(String name) {
		super(name);
	}
}
