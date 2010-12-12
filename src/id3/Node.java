package id3;

import java.util.HashMap;
import java.util.LinkedList;

public class Node implements Tree {

	Attribute a;
	LinkedList<Attribute> availableAttributes;
	ExampleSet es = null;
	HashMap<AttrValue, Tree> children;

	public Node(Attribute a) {
		this.a = a;
		this.children = new HashMap<AttrValue, Tree>();
		for (AttrValue v : a.getValues())
			this.children.put(v, null);
	}

	public Node(ExampleSet es, LinkedList<Attribute> attrs) {
		this.es = es;
		this.availableAttributes = attrs;
		Answer aa = es.getNextQuestion(availableAttributes);
		this.a = aa.a;		
		this.children = new HashMap<AttrValue, Tree>();

		ExampleSet esv = null;
		for (AttrValue v : a.getValues()) {
			esv = aa.answers.get(v);
			if (esv != null) {
				if (esv.numCats() == 1) {
					this.children.put(v, new Leaf(esv.l.getFirst().getCategory()));
				} else {
					LinkedList<Attribute> childAttrs = new LinkedList<Attribute>(availableAttributes);
					if (!childAttrs.remove(a)) {
						System.err.println("Attribute " + a.name + " not removed. This shouldn't happen.");
					}
					if (childAttrs.isEmpty()) {
						this.children.put(v, new Leaf(esv.mostProbableCat()));
					} else {
						this.children.put(v, new Node(esv,childAttrs));
					}
				}
			} else {
				this.children.put(v, new Leaf(this.es.mostProbableCat()));
			}
		}
	}

	public String toString() {
		String ret = "(N: " + a + " ";
		for (AttrValue v : children.keySet()) {
			ret += v;
			ret += children.get(v);
		}
		ret += ")";
		return ret;
	}

	public String toHtml() {
		String ret = "<div class=\"tree\" onclick=\"switch_visibility(event," + this.hashCode() + ")\">";
		ret += "<div class=\"node\">" + a + "</div><br />";
		ret += "<div id=\"" + this.hashCode() + "\" style=\"display: none;\">";
		for (AttrValue v : children.keySet()) {
			ret += "<div class=\"tree_wrapper\"><div class=\"tree_title\">" + v + "</div>" + children.get(v).toHtml() + "</div>\n";
		}
		ret += "</div></div>";
		return ret;
	}
	
	public Category get(Example e) {
		AttrValue v = e.getAnswer(a);
//		System.out.println("Got answer v=" + v + " to question a="+ a + " in get()");
//		System.out.println("children.get(v) : " + children.get(v));
//		System.out.println("children.get(v).get(e) : " + children.get(v).get(e));
		return children.get(v).get(e);
	}

}
