package treebank;

import id3.AttrValue;
import id3.Attribute;
import id3.Category;
import id3.Example;
import id3.ExampleSet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

public class TreeBankParser {

	static String baseFile = "WSJ/00/wsj_00";
	static String fileExt = ".pos";
	static String ambApp = ".amb";

	int nbPhrases = 0;
	int errCount = 0;
	LinkedList<Example> l = new LinkedList<Example>();
	LinkedList<Attribute> attrs = new LinkedList<Attribute>();

	CategoryParser cp;

	WordInfo previousWord;
	WordInfo currentWord;

	Attribute FollowingWord;
	Attribute PreviousWord;
	Attribute InNominalGroup;
	AttrValue IsInNominalGroup;
	AttrValue IsNotInNominalGroup;

	public TreeBankParser() {

		// On crée un objet qui gèrera les catégories :
		cp = new CategoryParser();

		// On cree les attributs
		// - Distinction sur le mot suivant
		FollowingWord = new Attribute("FollowingWord");
		for (AttrValue a : cp.getAllTypes()) {
			FollowingWord.addValue(a);
		}

		// - Distinction sur le mot précédent
		PreviousWord = new Attribute("PreviousWord");
		for (AttrValue a : cp.getAllTypes()) {
			PreviousWord.addValue(a);
		}

		// - Distinction selon si l'on est dans un group nominal
		InNominalGroup = new Attribute("InNominalGroup");
		IsInNominalGroup = new AttrValue("True");
		InNominalGroup.addValue(IsInNominalGroup);
		IsNotInNominalGroup = new AttrValue("False");
		InNominalGroup.addValue(IsNotInNominalGroup);

		// On crée la liste d'attributs initiale
		attrs.add(FollowingWord);
		attrs.add(PreviousWord);
		attrs.add(InNominalGroup);

		// On parse le tout pour créer la liste d'examples.
		// TODO : Remettre les chiffres à 9, et skipper le cas 00.
		for (int c1 = 0; c1 <= 9; c1++) {
			for (int c2 = 0; c2 <= 9; c2++) {
				if (c1 == 0 && c2 == 0) {
					continue;
				}
				parseFile(baseFile + c1 + c2 + fileExt);
			}
		}

		System.out.println("Nombre de phrases : " + nbPhrases);
		System.out.println("Nombre d'erreurs : " + errCount);
	}

	void parseFile(String filename) {

		String output = "";
		String ligne;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			while((ligne=br.readLine()) != null) {
				//				output += ligne;
				parseLine(ligne);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void parseLine(String line) {
		boolean isNominalGroup;

		// Empty line case.
		if (line.length() == 0) {
			return;
		}

		// If we begin a new phrase now.
		if (line.equals("======================================")) {
			newPhrase();
			return;
		}

		// If we have a nominal group. 
		if (line.charAt(0) == '[') {
			isNominalGroup = true;
			line = line.substring(2, line.length()-2);
		} else {
			isNominalGroup = false;
		}

		// On découpe en mots
		String[] words = line.split(" +");
		for (int i = 0; i < words.length; i++) {

			if (words[i].equals("")) {
				continue;
			}

			String[] pair = words[i].split("/");
			try {
				parseWord(pair[0],pair[1],isNominalGroup);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("*** Error in \"" + line + "\" at word : " + words[i]);
				errCount++;
			}
		}
	}

	void newPhrase() {
		// Commit infos for the end of previous sentence, if it exists
		if (currentWord != null) {
			currentWord.nexttype = "NULL";
			addExample(currentWord);
			currentWord = null;
		}
		nbPhrases++;
	}

	public ExampleSet toExampleSet() {
		return (new ExampleSet(l));
	}

	public LinkedList<Attribute> getAttrs() {
		return attrs;
	}

	public void parseWord(String word, String type, boolean inNominalGroup) {
		previousWord = currentWord;
		currentWord = new WordInfo(word, type, inNominalGroup);

		if (previousWord != null) {
			// Update infos for previous word and add as an example.
			previousWord.nexttype = type;
			addExample(previousWord);

			// Update infos for current word.
			currentWord.prevtype = previousWord.type;
		} else {
			currentWord.prevtype = "NULL";
		}

	}

	public void addExample(WordInfo wi) {
		if (cp.isNounOrVerb(wi.type)) {
			l.add(toExample(wi));
		}
	}

	public Example toExample(WordInfo wi) {
		HashMap<Attribute,AttrValue> h = new HashMap<Attribute, AttrValue>();

		h.put(PreviousWord, cp.getAttrValue(wi.prevtype));
		h.put(FollowingWord, cp.getAttrValue(wi.nexttype));
		h.put(InNominalGroup, wi.inNominalGroup ? IsInNominalGroup : IsNotInNominalGroup);

		return new Example(wi.word,cp.getCategory(wi.type),h);

	}

}
