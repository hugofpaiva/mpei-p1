package mpei_p1;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {
	
	private static ArrayList<Game> jogos = new ArrayList<>();
	
	public static void main(String[] args) {
		
	    long startTime = System.nanoTime();
		
		ReadJSON json = new ReadJSON();
		json.read();
		jogos = json.getJogos();
		
		long stopTime = System.nanoTime();
		long elapsedTime = startTime-stopTime;
		double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
		long convert=TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
		System.out.println("Time elapsed to read json (ms): " + elapsedTimeInSecond);
		// function to calculate max length of the elements to be hashed
		int maxC = 0;
	    for (int i=0;i<jogos.size();i++) {
	        if (jogos.get(i).getName().length() > maxC) {
	            maxC = jogos.get(i).getName().length();
	        }
	    }



	    long startTime1 = System.nanoTime();

	    // Cria�ao do bloom e inserir a HashFunction para o bloom usar
		CBloom mybloom = new CBloom(jogos.size(), 0.1, maxC);
		mybloom.initialize();
		
		long stopTime1 = System.nanoTime();
		System.out.println("Time create bloom filter (ms): "+(stopTime1 - startTime1)/1_000_000_000);
		
		int numIns=0;
		for (int i=0;i<jogos.size();i++) {
        	ArrayList<Review> reviews = jogos.get(i).getReviews();
        	if (jogos.get(i).getName().equals("Might and MagicÂ® 6-pack Limited Edition")) {
            	System.out.println("n de reviews de migth: "+reviews.size());
        	}
        	if (jogos.get(i).getName().equals("Galactic Civilizations III - Revenge of the Snathi DLC")) {
    			System.out.println("n de reviews de galatic: "+reviews.size());
    		}
        	if (jogos.get(i).getName().equals("Enclave")) {
    			System.out.println("n de reviews de enclave: "+reviews.size());
    		}

        	if (reviews.size() > 0) {
        		for (int j=0;j<reviews.size();j++) {
	            	mybloom.insertEle(jogos.get(i).getName());
        		}
        		numIns++;
        	}
		}
		
		System.out.println("enclave tem quantas reviews? "+mybloom.isEle("Enclave")); // tem de dar true
		System.out.println("galatic tem reviews? "+mybloom.isEle("Galactic Civilizations III - Revenge of the Snathi DLC")); // tem de dar false
		System.out.println("might tem reviews? "+ mybloom.isEle("Might and MagicÂ® 6-pack Limited Edition")); // tem de dar true
		
		System.out.println("enclave tem quantas reviews? "+mybloom.numEle("Enclave")); // tem de dar 68
		System.out.println("galatic tem quantas reviews? "+mybloom.numEle("Galactic Civilizations III - Revenge of the Snathi DLC")); // tem de dar 0
		System.out.println("migth tem quantas reviews? "+mybloom.numEle("Might and MagicÂ® 6-pack Limited Edition")); // tem de dar 133
		
		//mybloom.deleteEle("Might and MagicÂ® 6-pack Limited Edition");
		//System.out.println(mybloom.isEle("Might and MagicÂ® 6-pack Limited Edition")); // tem de dar false
		
		double pfp=Math.pow(1-Math.pow(1-1/mybloom.getN(), mybloom.getK()*mybloom.getM()), mybloom.getK());
		System.out.println("Probabilidade de falso positivo: " + pfp);
		
		int pos=0;
		for (int i=0;i<jogos.size();i++) {
			if (mybloom.isEle(jogos.get(i).getName())) {
				pos++;
			}
		}
		
		System.out.println("Numero de elementos inseridos no bloom: " + numIns);
		System.out.println("Numero de elementos que est�o no bloom: " + pos);
		System.out.println("Numero de colisoes: "+ (pos-numIns));
		MinHash lol = new MinHash(jogos.get(0).getReviews(), 10);
	}
}