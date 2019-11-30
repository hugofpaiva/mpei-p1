package mpei_p1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONObject;

public class Main {
	
	private static ArrayList<Review> reviews = new ArrayList<>();
	private static ArrayList<Game> jogos = new ArrayList<>();
	
	public static void main(String[] args) {
		ReadJSON json = new ReadJSON();
		json.read();
		jogos = json.getJogos();
		reviews = json.getReviews();
		
		// Cria�ao do bloom e inserir a HashFunction para o bloom usar
		CBloom mybloom = new CBloom(jogos.size(), 0.1);
		mybloom.initialize();
		
		
		for (int i=0;i<jogos.size();i++) {
        	ArrayList<Review> reviews = jogos.get(i).getReviews();
        	if (jogos.get(i).getName().equals("Might and MagicÂ® 6-pack Limited Edition")) {
            	System.out.println(reviews.size());
        	}
        	if (jogos.get(i).getName().equals("Galactic Civilizations III - Revenge of the Snathi DLC")) {
    			System.out.println(reviews.size());
    		}
        	if (jogos.get(i).getName().equals("Enclave")) {
    			System.out.println(reviews.size());
    		}

        	if (reviews.size() == 0) {
        		//System.out.println(jogos.get(i).getName());
        	} else {
        		for (int j=0;j<reviews.size();j++) {
	            	mybloom.insertEle(jogos.get(i).getName());
        		}
        	}
		}
		
		System.out.println(mybloom.isEle("Galactic Civilizations III - Revenge of the Snathi DLC")); // tem de dar false
		System.out.println(mybloom.isEle("Might and MagicÂ® 6-pack Limited Edition")); // tem de dar true
		System.out.println(mybloom.numEle("Might and MagicÂ® 6-pack Limited Edition")); // tem de dar 133
		System.out.println(mybloom.numEle("Enclave")); // tem de dar 68, mas da 104
		
		mybloom.deleteEle("Might and MagicÂ® 6-pack Limited Edition");
		System.out.println(mybloom.isEle("Might and MagicÂ® 6-pack Limited Edition")); // tem de dar false
	}
}