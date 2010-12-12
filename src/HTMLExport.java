import id3.Tree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class HTMLExport {

	static String html_header_filename="TestDrawTreeHeader.html";
	static String html_output_filename="TestDrawTree.html";


	public static void toFile(Tree t) {
		System.out.print("Generating html ("+html_output_filename+") ... (this could take a while) ... ");
		String output = "";
		String ligne;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(html_header_filename)));
			while((ligne=br.readLine()) != null) {
				output += ligne;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		output += t.toHtml();
		output += "</body></html>";

		try {
			FileWriter fw = new FileWriter (html_output_filename);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
			fichierSortie.println (output); 
			fichierSortie.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}		
		System.out.println("done !");

	}

}
