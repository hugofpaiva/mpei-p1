package mpei_p1;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static int numNoRev, numRev;
	
	private static ArrayList<Game> jogos = new ArrayList<>();
	
	private static void teste() {
	 	long startTime = System.nanoTime();
		
		ReadJSON json = new ReadJSON();
		json.read();
		jogos = json.getJogos();
		
		long stopTime = System.nanoTime();
		long elapsedTime = stopTime-startTime;
		double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
		System.out.println("Time elapsed to read json (ms): " + elapsedTimeInSecond+" seconds.");
		
		// function to calculate max length of the elements to be hashed
		int maxCG = 0;
	    for (int i=0;i<jogos.size();i++) {
	        if (jogos.get(i).getName().length() > maxCG) {
	            maxCG = jogos.get(i).getName().length();
	        }
	    }

	    startTime = System.nanoTime();

	    // CriaÁao do bloom e inserir a HashFunction para o bloom usar
		CBloom reviewsBloom = new CBloom(jogos.size(), 0.01, maxCG);
		reviewsBloom.initialize();
		
		stopTime = System.nanoTime();
		elapsedTime = stopTime-startTime;
		elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
		System.out.println("Time elapsed to create bloom (ms): " + elapsedTimeInSecond+" seconds.");
		
		int numIns=0;
		for (int i=0;i<jogos.size();i++) {
        	ArrayList<Review> reviews = jogos.get(i).getReviews();
        	if (jogos.get(i).getName().equals("Might and Magic® 6-pack Limited Edition")) {
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
	            	reviewsBloom.insertEle(jogos.get(i).getName());
        		}
        		numIns++;
        	}
		}
		
		System.out.println("enclave tem reviews? "+reviewsBloom.isEle("Enclave")); // tem de dar true
		System.out.println("galatic tem reviews? "+reviewsBloom.isEle("Galactic Civilizations III - Revenge of the Snathi DLC")); // tem de dar false
		System.out.println("might tem reviews? "+ reviewsBloom.isEle("Might and Magic® 6-pack Limited Edition")); // tem de dar true
		
		System.out.println("enclave tem quantas reviews? "+reviewsBloom.numEle("Enclave")); // tem de dar 68
		System.out.println("galatic tem quantas reviews? "+reviewsBloom.numEle("Galactic Civilizations III - Revenge of the Snathi DLC")); // tem de dar 0
		System.out.println("migth tem quantas reviews? "+reviewsBloom.numEle("Might and Magic® 6-pack Limited Edition")); // tem de dar 133
		
		//reviewsBloom.deleteEle("Might and Magic√Ç¬Æ 6-pack Limited Edition");
		//System.out.println(reviewsBloom.isEle("Might and Magic√Ç¬Æ 6-pack Limited Edition")); // tem de dar false
		
		double pfp=Math.pow(1-Math.pow(1-1/reviewsBloom.getN(), reviewsBloom.getK()*reviewsBloom.getM()), reviewsBloom.getK());
		System.out.println("Probabilidade de falso positivo: " + pfp);
		
		int pos=0;
		for (int i=0;i<jogos.size();i++) {
			if (reviewsBloom.isEle(jogos.get(i).getName())) {
				pos++;
			}
		}
		
		System.out.println("Numero de elementos inseridos no bloom: " + numIns);
		System.out.println("Numero de elementos que estão no bloom: " + pos);
		System.out.println("Numero de colisoes: "+ (pos-numIns));
		

	}
	
	public static void main(String[] args) {
		 teste();// tests to bloom
		ReadJson();

		//display_menu();
		MinHash lol = new MinHash(jogos.get(0).getReviews(), 10); //10 pq o professor disse
		
		lol.printSimilarities(0.07);
	}
		  
	

 static void display_menu() {
	    System.out.println ( "1) Search by game\n2) See the similarities in the reviews of a game\n3) See our program data\n4) Test our program" );
	    System.out.print ( "Selection: " );
	    
	    Scanner in = new Scanner ( System.in );
	    
		switch ( in.nextInt() ) {
			case 1:
		        System.out.println ( "You picked option 1" );
		        break;
			case 2:
				System.out.println ( "You picked option 2" );
				break;
			case 3:
				System.out.println("Our dataset contains "+jogos.size()+" games.");
				System.out.println("There are "+numRev+" games with reviews and "+(jogos.size()-numRev)+" without reviews in out dataset.");
				break;
			case 4:
				System.out.println ( "You picked option TESTES" );
				break;
			default:
				System.err.println ( "Unrecognized option" );
				break;
	    }
		in.close();
	 }

	private static void ReadJson() {
		ReadJSON json = new ReadJSON();
		json.read();
		jogos = json.getJogos();
		
		// function to calculate max length of the elements to be hashed
		int maxCG = 0; // max caracters of a game name
	    for (int i=0;i<jogos.size();i++) {
	        if (jogos.get(i).getName().length() > maxCG) {
	            maxCG = jogos.get(i).getName().length();
	        }
	    }
		
		CBloom reviewsBloom = new CBloom(jogos.size(), 0.1, maxCG); // bloom to find out the games with reviews and how many
		reviewsBloom.initialize();	
		CBloom noReviewsBloom = new CBloom(jogos.size(), 0.1, maxCG); // bloom 
		noReviewsBloom.initialize();
		
		numNoRev=0;
		numRev=0;
		for (int i=0;i<jogos.size();i++) {
        	ArrayList<Review> reviews = jogos.get(i).getReviews();
        	if (reviews.size() == 0) {
        		numNoRev++;
            	noReviewsBloom.insertEle(jogos.get(i).getName());
        	} else {
        		numRev++;
        		for (int j=0;j<reviews.size();j++) {
	            	reviewsBloom.insertEle(jogos.get(i).getName());
        		}
        	}
		}
		
		
	}
}