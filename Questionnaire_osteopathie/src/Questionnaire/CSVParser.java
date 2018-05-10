package Questionnaire;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVParser {
	  
	public static ArrayList<ArrayList<String>> importCSV(String filename) {
		    ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		    String line = "";
		    try {
		      BufferedReader br = new BufferedReader(new FileReader(filename));
		      while ((line = br.readLine()) != null){
		    	ArrayList<String> dataAsString = new ArrayList<String>(Arrays.asList(line.split("\\|")));
		    	if(FormCreationHelper.isaquestion(dataAsString) ||
		    			FormCreationHelper.isatitle(dataAsString) ||
		    			!FormCreationHelper.hasmarqueur(dataAsString).equals("") ||
		    			FormCreationHelper.isquestiontoaskmanytimes(dataAsString)){
		    		/**
		    		for(int i = 0; i < dataAsString.size(); i++){
		    			String string = dataAsString.get(i);
			    		byte[] b = string.getBytes("UTF-16");
			    		dataAsString.set(i, new String(b, "UTF-8"));
		    		}
		    		*/
		    		data.add(dataAsString);
		    		
		    	}
		      }
		      br.close();
		    }catch(IOException e) {
		    	e.printStackTrace();
		    }
		    return data;
		  }
}
