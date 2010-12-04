package id3;

import java.util.LinkedList;

public class Attribute {
	
	String name;
	LinkedList<AttrValue> potentialValues = new LinkedList<AttrValue>();
	
	public Attribute(String name) {
		this.name = name;
	}
	
	public LinkedList<AttrValue> getValues() {
		return potentialValues;
	}
	
	public void addValue(AttrValue v) {
		potentialValues.add(v);
	}
	
	public String toString() {
		return name;
	}
	
}
