package id3;

public interface Tree {
	
	public String toString();
	public String toHtml();
	
	public Category get(Example e);

}
