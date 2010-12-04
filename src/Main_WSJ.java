import id3.AttrValue;
import id3.Attribute;
import id3.Category;
import id3.ExampleSet;
import id3.Node;
import id3.Tree;

import java.util.LinkedList;

import treebank.TreeBankParser;


public class Main_WSJ {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TreeBankParser tbp = new TreeBankParser();
		
		ExampleSet es = tbp.toExampleSet();
		LinkedList<Attribute> attrs = tbp.getAttrs();
		Tree tree = new Node(es,attrs);
		System.out.println(tree);
		HTMLExport.toFile(tree);

	}

}
