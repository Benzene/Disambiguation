package id3;

import java.util.HashMap;
import java.util.LinkedList;

public class ExampleSet {
	
	LinkedList<Example> l;
	HashMap<Category,LinkedList<Example>> hm;
	
	int size;
	
	/*
	 * Le constructeur standard
	 */
	public ExampleSet(LinkedList<Example> l) {
		this.l = new LinkedList<Example>(l);
		size = l.size();
		this.hm = new HashMap<Category, LinkedList<Example>>();

		Category c = null;
		for (Example e : l) {
			c = e.getCategory();
			if (hm.get(c) == null) {
				hm.put(c, new LinkedList<Example>());
			}
			hm.get(c).add(e);
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public int numCats() {
		return hm.size();
	}
	
	/*
	 * Renvoie d * log(d), ou 0 si d=0.
	 */
	double plogp(double d) {
		if (d <= 0) {
//			System.out.println("Plogp called with value <= 0");
			return 0;
		} else {
//			System.out.println("Plogp called with value " + d);
			return (d*Math.log(d));
		}
	}
	
	/*
	 * Renvoie l'entropie d'un ExampleSet
	 */
	// TODO : ne le calculer qu'une fois..
	double entropy() {
		
		double rep = 0;
		
		double prop = 0;
		for(Category c : hm.keySet()) {
			prop = (1/(double)size) * hm.get(c).size();
			rep -= plogp(prop);
		}
		
//		System.out.println("Entropy computed with value " + rep);
		
		return rep;
	}
	
	/*
	 * Calcule la prochaine meilleure question
	 * Pas de garantie que ça fonctionne si l'ensemble est vide..
	 */
	Answer getNextQuestion(LinkedList<Attribute> attributes) {
		Attribute rep = null;
		HashMap<AttrValue,ExampleSet> argrep = null;
		double maxGain = -1;

		HashMap<AttrValue,ExampleSet> argcur = null;
		double gaincur = 0;
		for (Attribute a : attributes) {
			argcur = possibleDistributions(a);
			gaincur = gain(a,argcur);
			if (gaincur > maxGain) {
				rep = a;
				maxGain = gaincur;
				argrep = argcur;
			}
		}
		
//		System.out.println("Thus we choose attribute " + rep + " because gain is " + maxGain);
		return new Answer(rep, argrep);
	}
	
	/*
	 * Calcule le gain d'une question donnée
	 */
	double gain(Attribute a, HashMap<AttrValue,ExampleSet> m) {
		
		double rep = this.entropy();
//		System.out.println("rep : " + rep);
		
		ExampleSet es = null;
		double tmp = 0;
		for (AttrValue v : m.keySet()) {
			es = m.get(v);
//			System.out.println("(1/"+size+"[size])*("+es.getSize()+"[es.getSize()])*("+es.entropy()+"[es.entropy()])");
//			System.out.println("rep : " + rep);
			tmp = (1/(double)size)*(es.getSize())*(es.entropy());
//			System.out.println("tmp : " + tmp);
			rep -= tmp;
//			System.out.println("rep : " + rep);
		}
		
//		System.out.println("Choosing attribute " + a + " would mean a gain of " + rep);
		return rep;
	}
	
	/*
	 * Renvoie l'ensemble des distributions que l'on peut recevoir après avoir posé une question donnée
	 */
	HashMap<AttrValue,ExampleSet> possibleDistributions(Attribute a) {
		
		HashMap<AttrValue,LinkedList<Example>> sv = new HashMap<AttrValue, LinkedList<Example>>();
		HashMap<AttrValue,ExampleSet> sv2 = new HashMap<AttrValue, ExampleSet>();
		// On calcule la liste parmi l de ceux qui répondent v à a, pour tous les v.
		AttrValue v = null;
		for (Example e : l) {
			v = e.getAnswer(a);
			if (sv.get(v) == null) {
				sv.put(v, new LinkedList<Example>());
			}
			sv.get(v).add(e);			
		}
		
		for (AttrValue v2 : sv.keySet()) {
			sv2.put(v2, new ExampleSet(sv.get(v2)));
		}		
		return sv2;
	}

	/*
	 * Renvoie la catégorie la plus fréquente
	 */
	Category mostProbableCat() {
		Category rep = null;
		int mSize = -1;

		int tmpSize = 0;
		for (Category c : hm.keySet()) {
			tmpSize = hm.get(c).size();
			if (tmpSize > mSize) {
				rep = c;
				mSize = tmpSize;
			}
		}
		
		return rep;

	}
}
