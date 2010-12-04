package id3;

import java.util.HashMap;

public class Answer {
	
	public Attribute a;
	public HashMap<AttrValue,ExampleSet> answers;
	
	public Answer(Attribute a, HashMap<AttrValue,ExampleSet> answers) {
		this.a = a;
		this.answers = answers;
	}
	

}
