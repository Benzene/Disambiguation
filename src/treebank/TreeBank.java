package treebank;

import id3.AttrValue;
import id3.Attribute;
import id3.Category;
import id3.Example;
import id3.ExampleSet;
import id3.Node;
import id3.Tree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

public class TreeBank {
	
	static String baseFile = "WSJ/00/wsj_00";
	static String fileExt = ".pos";
	static String ambApp = ".amb";
	
	int nbPhrases = 0;
	int errCount = 0;
	
	int nbTestedWords;
	int nbPositives;
	
	public Tree t;

	WordInfo previousWord;
	WordInfo currentWord;

	CategoryParser cp;

	Attribute FollowingWord;
	Attribute PreviousWord;
	Attribute InNominalGroup;
	AttrValue IsInNominalGroup;
	AttrValue IsNotInNominalGroup;

	LinkedList<Example> l = new LinkedList<Example>();

	LinkedList<Attribute> attrs = new LinkedList<Attribute>();

	boolean testmode = false;
	
	
	public TreeBank() {
		init();
		parseDir();
		buildTree();
	}
	
	/*
	 * Fonction d'initialisation, avant de parser le fichier.
	 */	
	void init() {
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
	}

	/*
	 * Boucle sur tous les fichiers d'un dossier
	 */
	void parseDir() {
		// On parse le tout pour créer la liste d'examples.
		for (int c1 = 0; c1 <= 9; c1++) {
			for (int c2 = 0; c2 <= 9; c2++) {
				if (c1 == 0 && c2 == 0) {
					continue;
				}
				parseFile(baseFile + c1 + c2 + fileExt);
			}
		}
	}
	
	void buildTree() {
		t = new Node(new ExampleSet(l),attrs);
		testmode = true;
	}
	/*
	 * Boucle sur toutes les lignes d'un fichier.
	 */
	void parseFile(String filename) {

		String ligne;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			while((ligne=br.readLine()) != null) {
				parseLine(ligne);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Boucle sur les lignes de 2 fichiers en parallèle
	 */
	void parseFiles(String filename1, String filename2) {
		String line1;
		String line2;
		try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(filename1)));
			BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(filename2)));
			while((line1=br1.readLine()) != null && (line2=br2.readLine()) != null) {
				parseLines(line1,line2);
			}
			br1.close();
			br2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Boucle sur tous les mots d'une ligne
	 */
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

	/*
	 * Boucle sur les mots de 2 lignes en parallele
	 */
	void parseLines(String line1, String line2) {
		boolean isNominalGroup;

		// Empty line case.
		if (line1.length() == 0) {
			return;
		}

		// If we begin a new phrase now.
		if (line1.equals("======================================")) {
			newPhrase();
			return;
		}

		// If we have a nominal group. 
		if (line1.charAt(0) == '[') {
			if (line2.charAt(0) != '[') {
				System.out.println("Line mismatch ([] check). Shouldn't happen.");
				return;
			}
			isNominalGroup = true;
			line1 = line1.substring(2, line1.length()-2);
			line2 = line2.substring(2, line2.length()-2);
		} else {
			isNominalGroup = false;
		}

		// On découpe en mots
		String[] words1 = line1.split(" +");
		String[] words2 = line2.split(" +");
		if (words1.length != words2.length) {
			System.out.println("Line mismatch (nbWords check). Shouldn'y happen.");
			return;
		}
		for (int i = 0; i < words1.length; i++) {

			if (words1[i].equals("")) {
				continue;
			}

			String[] pair1 = words1[i].split("/");
			String[] pair2 = words2[i].split("/");
			parseWords(pair1[0],pair1[1],pair2[0],pair2[1],isNominalGroup);
		}
	}

	/*
	 * Parse un mot, crée le WordInfo, et le processe dès que complet.
	 */
	void parseWord(String word, String type, boolean inNominalGroup) {
		previousWord = currentWord;
		currentWord = new WordInfo(word, type, inNominalGroup);

		if (previousWord != null && !previousWord.word.equals("") ) {
			// Update infos for previous word and add as an example.
			previousWord.nexttype = type;
			processWord(previousWord);

			// Update infos for current word.
			currentWord.prevtype = previousWord.type;
		} else {
			currentWord.prevtype = "NULL";
		}

	}

	void parseWords(String word, String type, String word2, String potentialTypes, boolean inNominalGroup) {
		if (!word.equals(word2)) {
			System.out.println("Word mismatch ! ("+word+"!="+word2+"). Shouldn't happen");
			return;
		}
		previousWord = currentWord;
		currentWord = new WordInfo(word, potentialTypes, inNominalGroup);
		currentWord.realtype = type;

		if (previousWord != null && !previousWord.word.equals("") ) {
			// Update infos for previous word and add as an example.
			previousWord.nexttype = potentialTypes;
			processWord(previousWord);

			// Update infos for current word.
			currentWord.prevtype = previousWord.type;
		} else {
			currentWord.prevtype = "NULL";
		}
	}
	
	
	/*
	 * Fonction appelée en fin de phrase.
	 */
	void newPhrase() {
		// Commit infos for the end of previous sentence, if it exists
		previousWord = currentWord;
		currentWord = new WordInfo("","NULL",false);
		
		if (previousWord != null) {
			previousWord.nexttype = "NULL";
			processWord(previousWord);
		}
		
		nbPhrases++;
	}
	
	Example toExample(WordInfo wi) {
		HashMap<Attribute,AttrValue> h = new HashMap<Attribute, AttrValue>();

		h.put(PreviousWord, cp.getAttrValue(wi.prevtype));
		h.put(FollowingWord, cp.getAttrValue(wi.nexttype));
		h.put(InNominalGroup, wi.inNominalGroup ? IsInNominalGroup : IsNotInNominalGroup);

		return new Example(wi.word,cp.getCategory(wi.type),h);

	}

	void processWord(WordInfo wi) {
		if (testmode) {
			processWordTest(wi);
		} else {
			processWordBuild(wi);
		}
	}
	
	void processWordBuild(WordInfo wi) {
		if (cp.isNounOrVerb(wi.type)) {
			l.add(toExample(wi));
		}
	}

	void processWordTest(WordInfo wi) {
		if (wi.type.contains("|")) {
//			System.out.println("Process word : " + wi.word + " (" + wi.type + ")");
//			System.out.println("(" + wi.prevtype + " " + wi.type + " " + wi.nexttype + ")");
			AttrValue v = cp.getAttrValue(wi.prevtype);
			if (v == null) {
				System.out.println("Corpus fail ! found type in previous word : " + wi.prevtype);
				return;
			}
			v = cp.getAttrValue(wi.nexttype);
			if (v == null) {
				System.out.println("Corpus fail ! found type in next word : " + wi.nexttype);
				return;
			}
//			Example e = toExample(wi);
//			System.out.println(e);
			Category computedCategory = t.get(toExample(wi));
//			System.out.println("Computed Category : " + computedCategory);
			nbTestedWords++;
			if (computedCategory == cp.getCategory(wi.realtype)) {
			//	System.out.println("Success ! "+ wi.type + " -> " + computedCategory + "(actually "+wi.realtype+")");
				nbPositives++;
			} else {
				System.out.println("Failure ! "+ wi.type + " -> " + computedCategory + "(actually "+wi.realtype+")");
			}
		}
	}


	public void testPerformance() {
		nbTestedWords = 0;
		nbPositives = 0;
		
		for (int c1 = 0; c1 <= 9; c1++) {
			for (int c2 = 0; c2 <= 9; c2++) {
				if (c1 == 0 && c2 == 0) {
					continue;
				}
				parseFiles(baseFile + c1 + c2 + fileExt,baseFile + c1 + c2 + fileExt + ambApp);
			}
		}
		
		System.out.println("Tested words : " + nbTestedWords + " - Positive matches : " + nbPositives + " - Score : " +
				(double)nbPositives*100/(double)nbTestedWords);
	}
	
}
