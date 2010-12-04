package id3;

import java.util.HashMap;

public class Example {
	
	String name;
	
	HashMap<Attribute,AttrValue> answers;
	
	Category category;

	public Example(String name, Category c, HashMap<Attribute,AttrValue> ans) {
		this.name = name;
		this.answers = ans;
		this.category = c;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public AttrValue getAnswer(Attribute a) {
		return answers.get(a);
	}

}
