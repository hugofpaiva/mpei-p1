package mpei_p1;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadJSON {
		//private ArrayList<Review> reviews;
		private ArrayList<Game> jogos = new ArrayList<>();


		public void read() {
			try {
				
				// parse do dataset 
		        @SuppressWarnings("unchecked")
				ArrayList<JSONObject> obj = (ArrayList<JSONObject>) new JSONParser().parse(new FileReader("./dataset/games.json")); 		       
		        
		        for (JSONObject jogo : obj) {
		        	// criação do nome do jogo
		        	String nome = (String) jogo.get("name");
		        	
		        	// criação das reviews do jogo
		        	
		        	JSONArray reviews_array = (JSONArray) jogo.get("reviews");
		        	ArrayList<Review> reviews = new ArrayList<Review>();
		        	String review_user = null;
					String review_content = null;

		            for (int i=0;i<reviews_array.size();i++) {
		            	JSONObject obj2 = (JSONObject) reviews_array.get(i);
		            	review_user=(String) obj2.get("name");
		            	review_content=(String) obj2.get("content");
		            	if (review_user !=null && review_content != null) {
			            	Review r = new Review(review_user,review_content);
							reviews.add(r);
		            	}
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


		public ArrayList<Game> getJogos() {
			return jogos;
		}


		public void setJogos(ArrayList<Game> jogos) {
			this.jogos = jogos;
		}

		
		

	}

	


