import id3.AttrValue;
import id3.Attribute;
import id3.Category;
import id3.Example;
import id3.ExampleSet;
import id3.Node;
import id3.Tree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;


public class Main {

	public static void main(String[] args) {

		// On cree les catégories
		Category Cinema = new Category("Cinema");
		Category Shopping = new Category("Shopping");
		Category Tennis = new Category("Tennis");
		Category StayIn = new Category("StayIn");

		// On crée les attributs et leurs valeurs associées
		Attribute Weather = new Attribute("Weather");
		AttrValue Sunny = new AttrValue("Sunny");
		Weather.addValue(Sunny);
		AttrValue Windy = new AttrValue("Windy");
		Weather.addValue(Windy);
		AttrValue Rainy = new AttrValue("Rainy");
		Weather.addValue(Rainy);

		Attribute Parents = new Attribute("Parents");
		AttrValue Yes = new AttrValue("Yes");
		Parents.addValue(Yes);
		AttrValue No = new AttrValue("No");
		Parents.addValue(No);

		Attribute Money = new Attribute("Money");
		AttrValue Rich = new AttrValue("Rich");
		Money.addValue(Rich);
		AttrValue Poor = new AttrValue("Poor");
		Money.addValue(Poor);

		// On crée la liste d'attributs initiale
		LinkedList<Attribute> attrs = new LinkedList<Attribute>();
		attrs.add(Weather);
		attrs.add(Parents);
		attrs.add(Money);

		// On crée l'ExampleSet initial
		LinkedList<Example> l = new LinkedList<Example>();

		HashMap<Attribute,AttrValue> h1 = new HashMap<Attribute, AttrValue>();
		h1.put(Weather, Sunny);
		h1.put(Parents, Yes);
		h1.put(Money, Rich);
		l.add(new Example("W1",Cinema,h1));

		HashMap<Attribute,AttrValue> h2 = new HashMap<Attribute, AttrValue>();
		h2.put(Weather, Sunny);
		h2.put(Parents, No);
		h2.put(Money, Rich);
		l.add(new Example("W2",Tennis,h2));

		HashMap<Attribute,AttrValue> h3 = new HashMap<Attribute, AttrValue>();
		h3.put(Weather, Windy);
		h3.put(Parents, Yes);
		h3.put(Money, Rich);
		l.add(new Example("W3",Cinema,h3));

		HashMap<Attribute,AttrValue> h4 = new HashMap<Attribute, AttrValue>();
		h4.put(Weather, Rainy);
		h4.put(Parents, Yes);
		h4.put(Money, Poor);
		l.add(new Example("W4",Cinema,h4));

		HashMap<Attribute,AttrValue> h5 = new HashMap<Attribute, AttrValue>();
		h5.put(Weather, Rainy);
		h5.put(Parents, No);
		h5.put(Money, Rich);
		l.add(new Example("W5",StayIn,h5));

		HashMap<Attribute,AttrValue> h6 = new HashMap<Attribute, AttrValue>();
		h6.put(Weather, Rainy);
		h6.put(Parents, Yes);
		h6.put(Money, Poor);
		l.add(new Example("W6",Cinema,h6));

		HashMap<Attribute,AttrValue> h7 = new HashMap<Attribute, AttrValue>();
		h7.put(Weather, Windy);
		h7.put(Parents, No);
		h7.put(Money, Poor);
		l.add(new Example("W7",Cinema,h7));

		HashMap<Attribute,AttrValue> h8 = new HashMap<Attribute, AttrValue>();
		h8.put(Weather, Windy);
		h8.put(Parents, No);
		h8.put(Money, Rich);
		l.add(new Example("W8",Shopping,h8));

		HashMap<Attribute,AttrValue> h9 = new HashMap<Attribute, AttrValue>();
		h9.put(Weather, Windy);
		h9.put(Parents, Yes);
		h9.put(Money, Rich);
		l.add(new Example("W9",Cinema,h9));

		HashMap<Attribute,AttrValue> h10 = new HashMap<Attribute, AttrValue>();
		h10.put(Weather, Sunny);
		h10.put(Parents, No);
		h10.put(Money, Rich);
		l.add(new Example("W10",Tennis,h10));

		ExampleSet es = new ExampleSet(l);
		Tree t = new Node(es,attrs);
		System.out.println(t);

		HTMLExport.toFile(t);
	}

}
