package Questionnaire;

import java.util.ArrayList;

public class FormCreationHelper {
	
	public static boolean isaquestion(ArrayList<String> a) {
		boolean res = false;
		if(a.size()>0) {
			if(a.get(0).length() > 0){
				String atester = a.get(0);
				atester = atester.trim();
			
				if(atester.charAt(atester.length()-1)== '?'|| atester.charAt(atester.length()-1)== ':') {
					res = true;
				}
			}
		}
		return res;
	}
	
	public static boolean isatitle(ArrayList<String> a){
		boolean res = false;
		if(a.size()>0) {
			String atester = a.get(0);
			atester = atester.trim();
		
			if(atester.length()>=3) {
				if(atester.endsWith("...")) {
					res = true; //... en un char ?
				}
			}
		}
		return res;
	}
	
	public static boolean tocomplite(ArrayList<String> a) { //renvoie vrai si 4 collone contient [TEXTE]
		boolean res = false;
		if(a.size()>3) {
			String atester = a.get(3);
			atester = atester.trim();
		
			if(atester.charAt(atester.length()-1)== ']') {
				String compare = "[TEXTE]";
				boolean difference = false;
				for(int i = compare.length()-1; i>=0 && !difference;i--) {
					if(atester.charAt(i)!=compare.charAt(i)) {
						difference = true;
					}
				}
				if(!difference) {
					res = true ;
				}
			}
		}
		
		return res;
	}
	
	public static int titremultipages(ArrayList<String> a) { //retour -1 si ce n'est pas un titre multi page, le nombre de question si ça l'est
		int res =-1;
		if(a.size()>0) {
			String atester = a.get(0);
			atester = atester.trim();
		
			if(atester.charAt(atester.length()-1)== ')') {
				String nombreenvers = "";
				boolean fin = false;
				boolean pasnombre = false;
				for(int i = atester.length()-1; i>=0 && !fin && !pasnombre ;i--) {
					if(atester.charAt(i)==')') {
						fin = true;
					}
					else if(Character.isDigit(atester.charAt(i))){
						nombreenvers = nombreenvers + atester.charAt(i);
					}
					else {
						pasnombre =true;
					}
				}
				if(!pasnombre && fin) {
					String nombreendroit = "";
					for(int i = nombreenvers.length()-1; i>=0 ; i--) {
						nombreendroit = nombreendroit + nombreenvers.charAt(i);
					}
					res = Integer.parseInt(nombreendroit);
				}
				

			}
		}
		return res;
	}
	
	public static boolean isquestiontoaskmanytimes(ArrayList<String> a){
		boolean res = false;
		if(a.size()>0) {
			String atester = a.get(0);
			atester = atester.trim();
		
			if(atester.length()>=3) {
				if(atester.endsWith("++1")) {
					res = true; 
				}
			}
		}
		return res;
	}
	
	public static boolean isapage(ArrayList<String> a) {
		boolean res = false;
		if(a.size()>3) {
			String atester = a.get(0);
			atester = atester.trim();
		
			if(atester.charAt(atester.length()-1)== ']') {
				String compare = "[PAGE]";
				boolean difference = false;
				for(int i = compare.length()-1; i>=0 && !difference;i--) {
					if(atester.charAt(i)!=compare.charAt(i)) {
						difference = true;
					}
				}
				if(!difference) {
					res = true ;
				}
			}
		}
		
		return res;
	}
	
	public static String hasmarqueur(ArrayList<String> a) { //renvoie chaine vide si pas de marqueur, renvoie marqueur sinon
		String res = "";
		if(a.size()>0) {
			if(a.get(0).length() > 0){
				String atester = a.get(0);
				atester= atester.trim();
				
				if(atester.charAt(0)=='(') {
					boolean fini = false;
					for(int i=1;i<atester.length() && !fini;i++) {
						if(atester.charAt(i)==')') {
							fini = true;
						}
						else {
							res = res+atester.charAt(i);
						}
					}
					if(!fini) {
						res ="";
					}
				}
			}
		
		}
		return res;
	}
	
	public static boolean fincode(ArrayList<String> a) {
		boolean res = false;
		if(a.size()>0) {
			String atester = a.get(0);
			atester = atester.trim();
		
			if(atester.charAt(atester.length()-1)== ']') {
				String compare = "[FINPRINCIPAL]";
				boolean difference = false;
				for(int i = compare.length()-1; i>=0 && !difference;i--) {
					if(atester.charAt(i)!=compare.charAt(i)) {
						difference = true;
					}
				}
				if(!difference) {
					res = true ;
				}
			}
		}
		
		return res;
	}
	
	public static String retireMarqueurs(String phrase){
		//Retirer "++1"
		if(phrase.endsWith("++1")){
			phrase = phrase.substring(0, phrase.length()-3);
		}
		//Retirer "(x)" en fin de commentaire
		if(phrase.endsWith(")")){
			phrase = phrase.substring(0, phrase.indexOf("("));
		}
		//Retirer "(X)" en debut de commentaire
		if(phrase.startsWith("(")){
			phrase = phrase.substring(phrase.indexOf(")")+1);
		}
		//Retirer etoile s'il y a
		if(phrase.contains("*")){
			int index = phrase.indexOf("*");
			phrase = phrase.substring(0, index-1);
		}
		return phrase;
	}

}
