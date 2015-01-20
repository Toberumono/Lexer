package lipstone.joshua.lexer;

import lipstone.joshua.lexer.genericBase.AbstractType;

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
	 * A pre-created <tt>Type</tt> that flags the value as an instance of <tt>AbstractToken</tt>
	 */
	public static final Type TOKEN = new Type("Token") {
		
		@Override
		public Token cloneValue(Object value) {
			return ((Token) value).clone();
		}
	};
	
	public Type(String name, String open, String close) {
		super(name, open, close);
	}
	
	public Type(String name) {
		super(name);
	}
}
