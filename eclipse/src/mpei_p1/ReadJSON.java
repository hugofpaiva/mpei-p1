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
		private HashMap<String,String> reviews = new HashMap<>();
		private ArrayList<Game> jogos = new ArrayList<>();

		public void read() {
			try {
				
				// parse do dataset 
		        ArrayList<JSONObject> obj = (ArrayList<JSONObject>) new JSONParser().parse(new FileReader("./dataset/games.json")); 		       
		        
		        for (JSONObject jogo : obj) {
		        	// criação do nome do jogo
		        	String nome = (String) jogo.get("name");
		        	
		        	// criação das reviews do jogo
		        	
		        	JSONArray reviews_array = (JSONArray) jogo.get("reviews");
		            Iterator<JSONObject> reviews_iterator = reviews_array.iterator();
		            while(reviews_iterator.hasNext()) {
		            	String review_content = (String) reviews_iterator.next().get("content");
		        		reviews.put(nome, review_content);
		        	}
		        	
		    
		        	Game g = new Game(nome, reviews);
		        	jogos.add(g);		        			   
		        			
		        		
		        	//Leitura de outros campos do JSON para futuro
		        	
		        	/*Iterator<String> genres_iterator = genres_array.iterator();
		        	System.out.println("Genres:");
		        	while(genres_iterator.hasNext()) {
		        		System.out.println(genres_iterator.next());
		        	}
		        	
		        	JSONArray languages_array = (JSONArray) jogo.get("languages");
		            System.out.println(languages_array);
		            Iterator<JSONObject> languages_iterator = languages_array.iterator();
		            while(languages_iterator.hasNext()) {
		        		System.out.println(languages_iterator.next().get("name"));
		        	}		            		      
		        */

		        	
		        		
		        }
			} catch (IOException | ParseException e) {
				System.err.println("Erro de leitura do ficheiro JSON!");
				e.printStackTrace();
			} 
	          
	        
		}

		public HashMap<String, String> getReviews() {
			return reviews;
		}

		public void setReviews(HashMap<String, String> reviews) {
			this.reviews = reviews;
		}

		public ArrayList<Game> getJogos() {
			return jogos;
		}

		public void setJogos(ArrayList<Game> jogos) {
			this.jogos = jogos;
		}
		
		

	}

	


