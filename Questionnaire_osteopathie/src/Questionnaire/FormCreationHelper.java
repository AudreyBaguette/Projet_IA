package Questionnaire;

import java.util.ArrayList;

public class FormCreationHelper {
	
	public static boolean isaquestion(ArrayList<String> a) {
		boolean res = false;
		if(a.size()>0) {
			String atester = a.get(0);
			atester = atester.trim();
		
			if(atester.charAt(atester.length()-1)== '?') {
				res = true;
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
				if(atester.charAt(atester.length()-1)== '.' && atester.charAt(atester.length()-2)== '.'&& atester.charAt(atester.length()-3)== '.') {
					res = true; //... en un char ?
				}
			}
		}
		return res;
	}
	
	public static boolean istocomplete(ArrayList<String>a) {
		boolean res = false;
		if(a.size()>0) {
			String atester = a.get(0);
			atester = atester.trim();
		
			if(atester.charAt(atester.length()-1)== ':') {
				res = true;
			}
		}
		return res;
	}

}
