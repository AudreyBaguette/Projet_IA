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
				else if(atester.charAt(atester.length()-1)==')') {
					int i = atester.length()-1 ;
					while( i<atester.length() && atester.charAt(i)==')'  ) {
						while(i<atester.length() && atester.charAt(i)!='(' ) {
						i++;
						}
						i++;
					}
					if(i<atester.length() && (atester.charAt(i)== '?'|| atester.charAt(i)== ':')){
						res = true;
					}
						
				}
				else if(atester.length()>3 && atester.charAt(atester.length()-1)=='+' && atester.charAt(atester.length()-2)=='+' && atester.charAt(atester.length()-3)=='1') {
					if(atester.charAt(atester.length()-4)== '?'|| atester.charAt(atester.length()-4)== ':') {
						res =true;
					}
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
	
	public static boolean isafiletoopen(ArrayList<String> a){
		boolean res = false;
		if(a.size()>0) {
			String atester = a.get(0);
			atester = atester.trim();
		
			if(atester.length()>=3) {
				if(atester.startsWith("Questionnaire")) {
					res = true; //... en un char ?
				}
			}
		}
		return res;
	}
	
	public static boolean isanend(ArrayList<String> a){
		boolean res = false;
		if(a.size()>0) {
			String atester = a.get(0);
			atester = atester.trim();
		
			if(atester.length()>=3) {
				if(atester.startsWith("fin")) {
					res = true;
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
			else if(atester.indexOf("*") >-1) {
				int i = atester.indexOf("*");
				i--;
				while(atester.charAt(i)== ' '){
					i--;
				}
				System.out.println("char avant if "+ atester.charAt(i));
				if(atester.charAt(i)== ']') {
					System.out.println("char : "+atester.charAt(i));
					String compare = "[TEXTE]";
					boolean difference = false;
					while( i>=0 && !difference) {
						if(atester.charAt(i)!=compare.charAt(i)) {
							System.out.println("Diffrence : "+atester.charAt(i)+" "+compare.charAt(i));
							difference = true;
						}
						i--;
					}
					if(!difference) {
						res = true ;
					}
				}
			}
		}
		
		return res;
	}
	
	public static int titremultipages(ArrayList<String> a) { //retour -1 si ce n'est pas un titre multi page, le nombre de question si ça l'est
		int res =-1;
		if(a.size()>0 && a.get(0).length()>0) {
			String atester = a.get(0);
			atester = atester.trim();
			if(atester.substring(atester.length()-1).equals(")")) {
				String marqueur = atester.substring(atester.indexOf("(") + 1, atester.length()-1);
				try  
				  {  
				    int i = Integer.parseInt(marqueur);  
				    res = i;
				  }  
				  catch(NumberFormatException nfe)  
				  {  
				    return res;  
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
	
	public static ArrayList<String> hasmarqueur(ArrayList<String> a) { //renvoie chaine vide si pas de marqueur, renvoie marqueur sinon
		ArrayList<String> res = new ArrayList<String>();
		if(a.size()>0) {
			if(a.get(0).length() > 0){
				String atester = a.get(0);
				atester= atester.trim();
				int i =0;
				while(i<atester.length() && atester.charAt(i)=='(') {
					i++;
					String s= "";
					boolean fini = false;
					while(i<atester.length() && !fini) {
						if(atester.charAt(i)==')') {
							fini = true;
						}
						else {
							s=s+atester.charAt(i);
						}
						i++;
					}
					if(fini) {
						res.add(s);
					}
				}
			}
		
		}
		return res;
	}
	
	public static boolean hastoanswerquestion(ArrayList<String> marqueurs , ArrayList<String> a) {
		boolean res = true;
		ArrayList<String> marqueursquestion = hasmarqueur(a);
		for(int i = 0; i<marqueursquestion.size() && res; i++) {
			boolean trouve=false;;
			for(int j=0; j<marqueurs.size(); j++) {
				if(marqueursquestion.get(i).equals(marqueurs.get(j))) {
					trouve =true;
				}
			}
			if(!trouve) {
				res = false;
			}
		}
		return res;
	}
	
	public static boolean fincode(ArrayList<String> a) {
		boolean res = false;
		if(a.size()>0) {
			String atester = a.get(0);
			atester = atester.trim();
		
			if(atester.length()>=3) {
				if(atester.startsWith("[FINPRINCIPAL]")) {
					res = true; 
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
			//S'il y a deux marqueurs entre paretheses, il faut retirer les deux
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
