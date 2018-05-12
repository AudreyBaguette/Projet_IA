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
		      int i = 0;
		      while ((line = br.readLine()) != null){
		    	  i++;
		    	ArrayList<String> dataAsString = new ArrayList<String>(Arrays.asList(line.split("\\|")));
		    	data.add(dataAsString);
		      }
		      br.close();
		    }catch(IOException e) {
		    	e.printStackTrace();
		    }
		    return data;
	}
}

