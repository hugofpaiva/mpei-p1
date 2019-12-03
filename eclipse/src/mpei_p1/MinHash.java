package mpei_p1;

import java.util.ArrayList;
import java.util.Arrays;

public class MinHash {

	private ArrayList<Review> reviews;
	int s_shingle;// tamanho das shingles
	private HashFunction hash_shingle;
	private double[][] similar; // array multidimensional com 100 rows e x colunas (cada coluna representa uma review)

	public MinHash(ArrayList<Review> reviews, int s_shingle) {
		similar = new double[reviews.size()][reviews.size()];
		this.reviews = reviews;
		this.s_shingle = s_shingle;
		this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE, s_shingle);
		for (int i = 0; i < reviews.size(); i++) {
			minHashShingles(reviews.get(i));	
		}
		createMatrix(); //
	}

	private void createMatrix() {
		similar=new double[100][reviews.size()];
		for (int i=0; i<this.reviews.size();i++) {
			int[] minHashShingles=reviews.get(i).getminHash_shingles();
			for (int row=0;row<100;row++) {
				similar[row][i]=minHashShingles[row];
			}
		}
		System.out.println(Arrays.deepToString(similar).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
	}

	public void minHashShingles(Review review) {
		String string = review.getReview();
		String string_no_space = string.replaceAll("\\s+", "");
		int num_shingles = string_no_space.length() - s_shingle + 1;
		
		int[] randA=this.hash_shingle.getRandA();
		int[] randB=this.hash_shingle.getRandB();
		int prime=this.hash_shingle.getPrime();
		int n=this.hash_shingle.getN();
		
		for (int j=0;j<100;j++) {
			int min=Integer.MAX_VALUE;
			for (int i = 0; i < num_shingles; i++) {
				String s=string_no_space.substring(i, i + s_shingle);
				int hash=0;
				for (int l=0;l<s.length();l++) {
					hash+=((randA[j]*(int) s.charAt(l) + randB[j]) % prime) % n;
				}
				if (hash<0) {
					hash+=n;
				}
				int hashCode= (hash % n);
				if (hashCode<min) {
					min=hashCode;
				}
			}
			review.addminHash_shingles(min, j);
		}
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