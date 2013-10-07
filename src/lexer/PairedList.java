package lexer;

import java.util.ArrayList;
import java.util.Iterator;

class PairedList<T, V> implements Iterable<T>, Comparable<PairedList<T, V>> {
	private ArrayList<T> keys;
	private ArrayList<V> values;
	
	/**
	 * Constructs a new paired list
	 */
	public PairedList() {
		keys = new ArrayList<T>();
		values = new ArrayList<V>();
	}
	
	/**
	 * If key is already in the list, it overwrites the value associated with key. Otherwise, it adds it to the list.
	 * 
	 * @param key
	 *            the key to set the value of
	 * @param value
	 *            the value to set the key to
	 */
	public void put(T key, V value) {
		int index = keys.indexOf(key);
		if (index < 0) {
			keys.add(key);
			values.add(value);
			return;
		}
		values.set(index, value);
	}
	
	/**
	 * @param key
	 *            the key to get the value of
	 * @return the value associated with the key if the key is in the map, otherwise null
	 */
	public V get(T key) {
		int index = keys.indexOf(key);
		if (index < 0)
			return null;
		return values.get(index);
	}
	
	/**
	 * @param key
	 *            the key to remove
	 * @return the value mapped to the key or null if the key was not in the list
	 */
	public V remove(T key) {
		int index = keys.indexOf(key);
		if (index < 0)
			return null;
		keys.remove(index);
		return values.remove(index);
	}
	
	public boolean containsKey(T key) {
		return keys.indexOf(key) >= 0;
	}
	
	/**
	 * @return the size of this list, which is equal to the number of keys within the list.
	 */
	public int size() {
		return keys.size();
	}
	
	public ArrayList<T> getKeys() {
		return new ArrayList<T>(keys);
	}
	
	public ArrayList<V> getValues() {
		return new ArrayList<V>(values);
	}
	
	@Override
	public String toString() {
		String output = "";
		for (T key : keys)
			output = output + "<" + key.toString() + ", " + get(key).toString() + ">, ";
		if (output.endsWith(", "))
			output = output.substring(0, output.length() - 2);
		return output;
	}

	@Override
	public Iterator<T> iterator() {
		return keys.iterator();
	}

	@Override
	public int compareTo(PairedList<T, V> o) {
		if (keys.size() > o.keys.size())
			return 1;
		if (keys.size() < o.keys.size())
			return -1;
		if (values.get(0) instanceof Comparable) {
			int result = 0;
			for (T key : keys)
				result += ((Comparable<V>) get(key)).compareTo(o.get(key));
			return result;
		}
		return 0;
	}
}
