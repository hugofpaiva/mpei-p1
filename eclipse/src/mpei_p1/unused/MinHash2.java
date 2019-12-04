package mpei_p1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MinHash2 {

	private ArrayList<Review> reviews;
	int s_shingle;// tamanho das shingles
	private HashFunction hash_shingle;
	private double[][] similar;
	private int max=0; //Maior número de shingles que temos numa review é 8754
	

	public MinHash2(ArrayList<Review> reviews, int s_shingle) {
		
		this.reviews = reviews;
		similar = new double[reviews.size()][reviews.size()];
		this.s_shingle = s_shingle;
		this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
		for (int i = 0; i < reviews.size(); i++) {
			create_shingles(reviews.get(i), i);	
		}
		System.out.println("Maior número de shingles numa review: "+max);
		for (int i = 0; i < reviews.size(); i++) {
			create_hash(reviews.get(i));
		}
		
		for (int i = 0; i < reviews.size()-1; i++) {
			for(int l = 1; i<reviews.size();i++) {
				similar[i][l]=similarity(reviews.get(i), reviews.get(l));
			}
		}
		
	}

	public void create_shingles(Review review,int l) {
		//Leitura da string e transformação em shingles
		int temp=0;
		String string = review.getReview();
		String string_no_space = string.replaceAll("\\s+", "");
		int num_shingles = string_no_space.length() - s_shingle + 1;
		for (int i = 0; i < (num_shingles); i++) {
			review.addShingles(string_no_space.substring(i, i + s_shingle));// Vou buscar a palavra de i até i+s_shingle
			temp++;
			if(temp> max)
				max=temp;
		}
		
	}
	
	public void create_hash(Review review) {
		//Criar as hashes e guardar neste objeto review
		ArrayList<String> shingles = review.getShingles();
		ArrayList<Integer> hashes;
		hashes=this.hash_shingle.generateHash_S(shingles);
		review.setminHash_shingles(hashes);

	}
		
	public double similarity(Review review1, Review review2) {
		double common = 0.0;
		double total;
		ArrayList<Integer> hashes1 = review1.getminHash_shingles();
		ArrayList<Integer> hashes2 = review2.getminHash_shingles();
		int min_size = hashes1.size();
		total = review1.getShingles().size()+review2.getShingles().size();
		for (int i = 0; i < hashes1.size(); i++) {
				if(hashes1.get(i)==hashes2.get(i)) {
		
							common=common+1.0;	
				
			}
		}
		double similariadade = common/min_size;
		
			System.out.println("Total - " + total + " | common - " +common + " | sim - " + similariadade);
		return similariadade;
	}

	public double[][] getSimilar() {
		return similar;
	}

	public void setSimilar(double[][] similar) {
		this.similar = similar;
	}
	
	public void printSimilar() {
		for (int i = 0; i < reviews.size(); i++) {
			for(int l = i+1; i<reviews.size();i++) {
				if(similar[i][l]>0.1) {
					System.out.println(similar[i][l]);
					System.out.println(reviews.get(i).getReview());
					System.out.println(reviews.get(l).getReview());
				}
				
			}
		}
	}
	
	
	public boolean isPrime(int n) {
		int m = n / 2;
		if (n == 0 || n == 1) {
			return false;
		} else {
			for (int l = 2; l < m; l++) {
				if (n % l == 0) {
					return false;
				}
			}
		}
		return true;

	}
	
	
	
	
	
					
			
	
}