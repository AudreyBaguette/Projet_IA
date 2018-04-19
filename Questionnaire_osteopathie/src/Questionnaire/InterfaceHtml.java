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
			BufferedWriter bw2 = new BufferedWriter(new FileWriter("page2.php"));
			bw2.write("<?php\n $nom=$_POST['nom'];\n $prenom=$_POST['prenom'];\n $age=$_POST['age'];\n" +
			"$prof=$_POST['prof'];\n $prob=$_POST['prob'];\n $duree=$_POST['duree'];\n" +
					"$dbt=$_POST['debut'];\n echo\"Patient : {$prenom}, {$nom} <br> Age : {$age} <br>" +
			"Profession : {$prof} <br> Consulte pour cause de {$prb} <br>" +
					"Tout a commence il y a {$duree}, quand il/elle avait ${dbt}.\n ?>");
			bw.write("<html lang=\"en\">\n" + "<head>\n" + "<meta charset=\"UTF-8\">\n" +
					"<title>Questionnaire osteopathie</title>\n" + "</head>\n" + "<body>\n");
			bw.write(qGeneral());
			bw.write("</body>\n"+ "</html>");
            
			bw.close();
			bw2.close();
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
		String code_html = "<form action=\"page2.php\" method=\"post\">\n" +
	"<h1>Bonjour, cher(e) patient(e)!</h1>\n" +
	"<h2>Merci de bien vouloir repondre a ces quelques questions pour commencer... </h2>\n" +
	//Nom
	"Quel est votre nom? <pre style=\"display: inline-block; width: 2ch;\">&#9</pre> <input type=\"text\" name = \"nom\"> <br>\n" +
	//Prenom
	"Quel est votre prenom? <pre style=\"display: inline-block; width: 2ch;\">&#9</pre> <input type=\"text\" name = \"prenom\"> <br>\n" +
	//Age
	"Quel est votre age? <pre style=\"display: inline-block; width: 2ch;\">&#9</pre> <input type=\"text\" name = \"age\"> <br>\n" +
	//Profession
	"Quel est votre profession? <pre style=\"display: inline-block; width: 2ch;\">&#9</pre> <input type=\"text\" name = \"prof\"> <br>\n" +
	//Probleme
	"Quel est le probleme pourlequel vous allez consulter? <br> S'il y en a plusieurs, choisissez le plus important." +
	"<pre style=\"display: inline-block; width: 2ch;\">&#9</pre>" +
	"<select name = \"prob\">\n <option>Peurs ou angoisses</option>\n <option>Douleurs</option>\n" +
	"<option>Stess, fatigue ou troubles de l'attention </option>\n <option>Trauma, separation ou deuil </option>\n" +
	"<option>Depression ou solitude</option>\n <option>Manque de confiance </option>\n" +
	"<option>Addiction ou consommation de tabac </option>\n <option>Troubles du sommeil </option>\n" +
	"<option>Troubles cutanes </option>\n <option>Phobies </option>\n </select>\n <br>\n" +
	//Duree
	"Depuis combien de temps souffrez-vous de ce probleme? <pre style=\"display: inline-block; width: 2ch;\">&#9</pre>" +
	"<select name = \"duree\">\n <option>0-5 ans</option>\n <option>5-10 ans</option>\n <option>10-15 ans</option>\n" +
	"<option>15-20 ans</option>\n <option>20-25 ans</option>\n <option>Plus de 25 ans</option>\n </select>\n <br>\n" +
	//Age debut
	"Quel age aviez-vous lorsque ce probleme a commence? <pre style=\"display: inline-block; width: 2ch;\">&#9</pre>" +
	"<select name = \"debut\">\n <option>0-10 ans</option>\n <option>10-20 ans</option>\n <option>20-30 ans</option>\n" +
	"<option>30-60 ans</option>\n <option>Plus de 60 ans</option>\n </select>\n <br>\n" +
	//Submit
	"<input type=\"submit\" value = \"Suivant\"/>\n </form>\n";
		
		
		/*
		 * Choix probleme = [peurs-angoisses, douleurs, stress-fatigue-trb attention, trauma-separation-deuil, depression-solitude, manque de confiance, addiction-tabac, trbs du sommeil, trbs cutanes, phobies]
Depuis cb de temps = [0-5, 5-10, 10-15, 15-20, 20-25, 25+]
A quel age = [0-10, 10-20, 20-30, 30-60, 60+]

		 */

		return code_html;
	}
}
