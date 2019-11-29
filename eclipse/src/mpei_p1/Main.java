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
		
		// Criaçao do bloom e inserir a HashFunction para o bloom usar
		CBloom mybloom = new CBloom(jogos.size(), 0.1);
		mybloom.initialize();
		HashFunction myHash=new HashFunction(mybloom.getK(), jogos.size());
		mybloom.setHashFunction(myHash);
		
		
		for (int i=0;i<jogos.size();i++) {
        	ArrayList<Review> reviews = jogos.get(i).getReviews();
        	if (reviews.size() == 0) {
        		//System.out.println(jogos.get(i).getName());
        	}
        	for (int j=0;j<reviews.size();j++) {
        		String jogo = (String) jogos.get(i).getName();
            	String review_content = (String) reviews.get(j).getReview();
            	String review_user = (String) reviews.get(j).getUser();
            	
            	mybloom.insertEle(jogo);
        	}
		}
		System.out.println(mybloom.numEle("Kim - Soundtrack"));
		
	

	}
}