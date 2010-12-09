package treebank;

import id3.AttrValue;
import id3.Category;

import java.util.Collection;
import java.util.HashMap;

public class CategoryParser {

	HashMap<String,AttrValue> attrvalues = new HashMap<String, AttrValue>();
	HashMap<String,Category> categories = new HashMap<String, Category>();
	
	// On cree les catégories
	Category Noun = new Category("Noun");
	Category Verb = new Category("Verb");
	Category Other = new Category("Other");
	
	public CategoryParser() {
		categories.put("CC",Other);
		categories.put("CD",Other);
		categories.put("DT",Other);
		categories.put("EX",Other);
		categories.put("FW",Other);
		categories.put("IN",Other);
		categories.put("JJ",Other);
		categories.put("JJR",Other);
		categories.put("JJS",Other);
		categories.put("LS",Other);
		categories.put("MD",Other);
		categories.put("PDT",Other);
		categories.put("POS",Other);
		categories.put("PRP",Other);
		categories.put("PRP$",Other);
		categories.put("RB",Other);
		categories.put("RBR",Other);
		categories.put("RBS",Other);
		categories.put("RP",Other);
		categories.put("SYM",Other);
		categories.put("TO",Other);
		categories.put("HU",Other);
		categories.put("WDT",Other);
		categories.put("WP",Other);
		categories.put("WP$",Other);
		categories.put("WRB",Other);
		categories.put(",", Other);
		categories.put(".", Other);
		categories.put("(", Other);
		categories.put(")", Other);
		categories.put("''", Other);
		categories.put(":", Other);
		categories.put("``", Other);
		
		// To account for bugs in the corpus :
/*		categories.put("$", Other);
		categories.put("winter", Other);
		categories.put("UH", Other);
		categories.put("McGraw-Hill", Other);
		categories.put("McGraw", Other);
		categories.put("8", Other);
		categories.put("4", Other);
		categories.put("2", Other);
		categories.put("Contra", Other);
		categories.put("Firestone", Other);
*/
		// Some specific types :
		categories.put("NULL",Other);

		categories.put("NN",Noun);
		categories.put("NNP",Noun);
		categories.put("NNS",Noun);
		categories.put("NNPS",Noun);

		categories.put("VB",Verb);
		categories.put("VBD",Verb);
		categories.put("VBG",Verb);
		categories.put("VBN",Verb);
		categories.put("VBP",Verb);
		categories.put("VBZ",Verb);

		for (String s : categories.keySet()) {
			attrvalues.put(s,new AttrValue(s));
		}
	}

	Category getCategory(String type) {
		return categories.get(type);
	}
	
	boolean isNounOrVerb(String type) {
		return (categories.get(type) == Noun || categories.get(type) == Verb);
	}
	
	AttrValue getAttrValue(String type) {
		return attrvalues.get(type);
	}
	
	Collection<AttrValue> getAllTypes() {
		return attrvalues.values();
	}
}
