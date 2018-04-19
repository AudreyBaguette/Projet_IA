package Questionnaire;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class InterfaceHtml {
	public static void main(String[] args){
		File f = new File("test.html");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write("<title> Titre <title>");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
