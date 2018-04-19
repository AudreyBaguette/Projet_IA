package Questionnaire;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class InterfaceHtml {
	public static void main(String[] args){
		File f = new File("test.html");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write("<html lang=\"en\">\n" + "<head>\n" + "<meta charset=\"UTF-8\">\n" +
					"<title>Questionnaire osteopathie</title>\n" + "</head>\n" + "<body>\n");
			bw.write(qGeneral());
			bw.write("</body>\n"+ "</html>");
            
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader("test.html"));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
		}
		String content = contentBuilder.toString();
		System.out.println(content);
		**/
	}
	
	public static String qGeneral(){
		String code_html = "<h1>Bonjour, cher(e) patient(e)!</h1>\n" +
	"<h2>Merci de bien vouloir repondre a ces quelques questions pour commencer... </h2>\n" +
	//Nom
	"<p>Quel est votre nom? <input type=\"text\"> </p>\n" +
	//Prenom
	"<p>Quel est votre prenom? <input type=\"text\"> </p>\n" +
	//Age
	"<p>Quel est votre age? <input type=\"text\"> </p>\n" +
	//Profession
	"<p>Quel est votre profession? <input type=\"text\"> </p>\n" +
	//Probleme
	"<p>Quel est le probleme pourlequel vous allez consulter? <br> S'il y en a plusieurs, choisissez le plus important." +
	"<select>\n <option>Peurs ou angoisses</option>\n <option>Douleurs</option>\n" +
	"<option>Stess, fatigue ou troubles de l'attention </option>\n <option>Trauma, separation ou deuil </option>\n" +
	"<option>Depression ou solitude</option>\n <option>Manque de confiance </option>\n" +
	"<option>Addiction ou consommation de tabac </option>\n <option>Troubles du sommeil </option>\n" +
	"<option>Troubles cutanes </option>\n <option>Phobies </option>\n </select>\n <p>\n" +
	//Duree
	"<p>Depuis combien de temps souffrez-vous de ce probleme?" +
	"<select>\n <option>0-5 ans</option>\n <option>5-10 ans</option>\n <option>10-15 ans</option>\n" +
	"<option>15-20 ans</option>\n <option>20-25 ans</option>\n <option>Plus de 25 ans</option>\n </select>\n <p>\n" +
	//Age debut
	"<p>Quel age aviez-vous lorsque ce probleme a commence?" +
	"<select>\n <option>0-10 ans</option>\n <option>10-20 ans</option>\n <option>20-30 ans</option>\n" +
	"<option>30-60 ans</option>\n <option>Plus de 60 ans</option>\n </select>\n <p>\n" ;
		
		
		/*
		 * Choix probleme = [peurs-angoisses, douleurs, stress-fatigue-trb attention, trauma-separation-deuil, depression-solitude, manque de confiance, addiction-tabac, trbs du sommeil, trbs cutanes, phobies]
Depuis cb de temps = [0-5, 5-10, 10-15, 15-20, 20-25, 25+]
A quel age = [0-10, 10-20, 20-30, 30-60, 60+]

		 */

		return code_html;
	}
}
