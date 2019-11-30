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
        	if (jogos.get(i).getName().equals("Enclave")) {
            	System.out.println(reviews.size());
        	}
        	//System.out.println(reviews.size());
        	//System.out.println(jogos.get(i).getName());
        	if (reviews.size() == 0) {
        		//System.out.println(jogos.get(i).getName());
        	}
        	for (int j=0;j<reviews.size();j++) {
        		String jogo = (String) jogos.get(i).getName();
            	//String review_content = (String) reviews.get(j).getReview();
            	//String review_user = (String) reviews.get(j).getUser();
            	// System.out.println(jogo);
            	mybloom.insertEle(jogo);
        	}
		}
		
		System.out.println(mybloom);
		
		System.out.println(mybloom.isEle("Might and MagicÂ® 6-pack Limited Edition"));
		System.out.println(mybloom.numEle("Might and MagicÂ® 6-pack Limited Edition"));
		System.out.println(mybloom.numEle("Enclave")); // devia dar 193
		
		mybloom.deleteEle("Might and MagicÂ® 6-pack Limited Edition");
		System.out.println(mybloom.isEle("Might and MagicÂ® 6-pack Limited Edition"));

	}
}