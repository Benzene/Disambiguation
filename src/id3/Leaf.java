package id3;

public class Leaf implements Tree {
	
	Category c;

	public Leaf(Category c) {
		this.c = c;
	}
	
	public String toString() {
		return "(L: " + c + ")";
	}
	
	public String toHtml() {
		return "<div class=\"leaf\">" + c + "</div>";
	}
}
