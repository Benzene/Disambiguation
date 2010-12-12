import id3.Tree;
import treebank.TreeBank;


public class Main_WSJ {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TreeBank tb = new TreeBank("00");
		
		Tree tree = tb.t;
		System.out.println(tree);
		HTMLExport.toFile(tree);

		tb.testPerformance();
	}

}
