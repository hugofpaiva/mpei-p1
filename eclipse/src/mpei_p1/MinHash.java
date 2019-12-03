package mpei_p1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MinHash {

	private ArrayList<Review> reviews;
	int s_shingle;// tamanho das shingles
	private HashFunction hash_shingle;
	private double[][] similar;

	public MinHash(ArrayList<Review> reviews, int s_shingle) {
		similar = new double[reviews.size()][reviews.size()];
		this.reviews = reviews;
		this.s_shingle = s_shingle;
		this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE, s_shingle);
		for (int i = 0; i < reviews.size(); i++) {
			create_shingles(reviews.get(i));	
		}
		for (int i = 0; i < reviews.size(); i++) {
			create_hash(reviews.get(i));	
		}
		
		for (int i = 0; i < reviews.size(); i++) {
			for(int l = 1; i<reviews.size();i++) {
				similar[i][l]=similarity(reviews.get(i), reviews.get(l));
			}
		}
		
	}

	public void create_shingles(Review review) {
		//Leitura da string e transformação em shingles
		String string = review.getReview();
		String string_no_space = string.replaceAll("\\s+", "");
		int num_shingles = string_no_space.length() - s_shingle + 1;
		for (int i = 0; i < (num_shingles); i++) {
			review.addShingles(string_no_space.substring(i, i + s_shingle));// Vou buscar a palavra de i até i+s_shingle
		}
		
	}
	
	public void create_hash(Review review) {
		//Criar as hashes e guardar neste objeto review
		ArrayList<String> shingles = review.getShingles();
		ArrayList<Integer> hashes;
		for (int i=0;i<shingles.size();i++) {
			hashes=this.hash_shingle.generateHash_S(shingles.get(i));
			//ordenação para ir buscar o valor mais pequeno
			Collections.sort(hashes);
			review.addminHash_shingles(hashes.get(0));		
		}	
	}
		
	public double similarity(Review review1, Review review2) {
		int common = 0;
		ArrayList<Integer> hashes1 = review1.getminHash_shingles();
		ArrayList<Integer> hashes2 = review2.getminHash_shingles();
		int total = hashes1.size()+hashes2.size();
		for (int i = 0; i < hashes1.size(); i++) {
				for (int l = 0; l < hashes2.size(); l++) {
					if(hashes1.get(i)==hashes2.get(l))
						System.out.println("hash1 - " + hashes1.get(i) + " | hash2 - " + hashes2.get(l));
						common++;		
			}
		}
		double jacart_coeficient = common/(total-common);
		System.out.println("Total - " + total + " | common - " +common + " | coeficiente - " + jacart_coeficient);
		return jacart_coeficient;
	}

	public double[][] getSimilar() {
		return similar;
	}

	public void setSimilar(double[][] similar) {
		this.similar = similar;
	}
	
	public void printSimilar() {
		for (int i = 0; i < reviews.size(); i++) {
			for(int l = 1; i<reviews.size();i++) {
				System.out.println(similar[i][l]);
			}
		}
	}
	
	
	
					
			
	
}