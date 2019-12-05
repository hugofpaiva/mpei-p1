package mpei_p1;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadJSON {
	private ArrayList<Game> jogos = new ArrayList<>();

		public void read() {
			try {
				
				// parse do dataset 
		        @SuppressWarnings("unchecked")
				ArrayList<JSONObject> obj = (ArrayList<JSONObject>) new JSONParser().parse(new FileReader("./dataset/games.json")); 		       
		        
		        for (JSONObject jogo : obj) {
		        	// criação do nome do jogo
		        	String nome = (String) jogo.get("name");
		        	
		        	// criação géneros do jogo
		        	JSONArray genre_array = (JSONArray) jogo.get("genres");
		        	ArrayList<String> genres = new ArrayList<String>();
		        	for(Object genre: genre_array) 
		        			genres.add((String) genre);
		        	
		        	// criação das linguagens do jogo
		        	JSONArray language_array = (JSONArray) jogo.get("languages");
		        	ArrayList<Language> languages = new ArrayList<Language>();
		        	String language = null;
					Boolean text = null;
					Boolean audio = null;
		       
		            for(Object obj2:language_array) {
		            	language=(String) ((JSONObject) obj2).get("name");
		            	text=(Boolean) ((JSONObject) obj2).get("text");
		            	audio=(Boolean) ((JSONObject) obj2).get("audio");
		            	if (language !=null && text != null && audio != null) {
		            		Language l = new Language(language, text, audio);
							languages.add(l);

		            	}
		            }
		            
		            //Criação do price do jogo
		            double price = (double) jogo.get("price");
		            
		          //Criação do rating do jogo
		            double rating = (double) jogo.get("player_rating");
		            
		            //Criação do publisher do jogo
		            String publisher = (String) jogo.get("publisher");
		            
		            
		            //Criação do developer do jogo
		            String developer = (String) jogo.get("developer");
		        	
		        	// criação das reviews do jogo
		        	
		        	JSONArray reviews_array = (JSONArray) jogo.get("reviews");
		        	ArrayList<Review> reviews = new ArrayList<Review>();
		        	String review_user = null;
					String review_content = null;

		       
		            for(Object review:reviews_array) {
		            	review_user=(String) ((JSONObject) review).get("name");
		            	review_content=(String) ((JSONObject) review).get("content");
		            	if (review_user !=null && review_content != null) {
			            	Review r = new Review(review_user,review_content);
							reviews.add(r);
		            	}
		            }
		            
		            Game g = new Game(nome, reviews, genres, languages, price, publisher, developer, rating);
		        	jogos.add(g);
		     	    		        			        	
		        		
		        }
			} catch (IOException | ParseException e) {
				System.err.println("Erro de leitura do ficheiro JSON!");
				e.printStackTrace();
			} 
	          
	        
		}


		public ArrayList<Game> getJogos() {
			return jogos;
		}


		public void setJogos(ArrayList<Game> jogos) {
			this.jogos = jogos;
		}

		
		

	}

	


