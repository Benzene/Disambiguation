package treebank;

import id3.AttrValue;
import id3.Attribute;
import id3.Example;

import java.util.HashMap;

public class WordInfo {

	public String word;
	public String type;
	public String prevtype;
	public String prevprevtype;
	public String nexttype;
	public String nextnexttype;
	public String realtype;
	public boolean inNominalGroup;
	
	public WordInfo(String word, String type, boolean inNominalGroup) {
		this.word = word;
		this.type = type;
		this.inNominalGroup = inNominalGroup;
	}
	
}
