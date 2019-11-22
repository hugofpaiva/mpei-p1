package mpei_p1;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadJSON {

		public static void main(String[] args) {
			// parsing file "JSONExample.json" 
			try {
				
				// parsing file "JSONExample.json" 
		        ArrayList<JSONObject> obj = (ArrayList<JSONObject>) new JSONParser().parse(new FileReader("./dataset/games.json")); 
		        
		        //System.out.println(obj);
		        
		        for (JSONObject jo : obj) {
		        	//System.out.println(jo.get("name"));
		        	JSONArray genres_array = (JSONArray) jo.get("genres");
		        	System.out.println("Game: " + jo.get("name"));
		        	Iterator<String> genres_iterator = genres_array.iterator();
		        	System.out.println("Genres:");
		        	while(genres_iterator.hasNext()) {
		        		System.out.println(genres_iterator.next());
		        	}
		        	
		        	JSONArray languages_array = (JSONArray) jo.get("languages");
		            System.out.println(languages_array);
		            Iterator<JSONObject> languages_iterator = languages_array.iterator();
		            while(languages_iterator.hasNext()) {
		        		System.out.println(languages_iterator.next().get("name"));
		        	}
		            
		            
		            JSONArray reviews_array = (JSONArray) jo.get("reviews");
		            //System.out.println(languages_array);
		            Iterator<JSONObject> reviews_iterator = reviews_array.iterator();
		            while(reviews_iterator.hasNext()) {
		        		System.out.println(reviews_iterator.next().get("name"));
		        	}
		            
		            
		        	System.out.println("-------------");
		        	
		            
		            //Iterating the contents of the array
		            /*Iterator<String> iterator1 = jsonArray1.iterator();
		            while(iterator1.hasNext()) {
		               System.out.println(iterator1.next());
		            }*/

		        	
		        		
		        }
		        
		        // typecasting obj to JSONObject 
		        //JSONObject jo = (JSONObject) obj; 
		        
		        //JSONArray ja = (JSONArray) jo.get(""); 
		        
		        // Iterating JSON Array  
		        //Iterator itr2 = ja.iterator();
		          
		        
		        // getting firstName and lastName 
		        //String firstName = (String) jo.get("url"); 
		        //String lastName = (String) jo.get("name"); 
		          
		        //System.out.println(firstName); 
		        //System.out.println(lastName); 
			} catch (IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	          
	        
		}

	}

	


