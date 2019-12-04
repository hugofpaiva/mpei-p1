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
		this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
		for (int i = 0; i < reviews.size(); i++) {
			reviews.get(i).createShingles(s_shingle); // criar shingles de todas as reviews
			reviews.get(i).setminHash_shingles(this.hash_shingle.generateSignatures(reviews.get(i).getShingles())); // gerar assinaturas de todas as reviews
		}
		createMatrix();
	}

	private void createMatrix() {
		this.similar=new double[100][reviews.size()];
		for (int i=0; i<this.reviews.size();i++) {
			int[] minHashShingles=reviews.get(i).getminHash_shingles();
			for (int row=0;row<100;row++) {
				this.similar[row][i]=minHashShingles[row];
			}
		}		
	}
	
	public void printSimilars(double limiar) {
		double intersections, distJac;
		for (int i=0; i<reviews.size()-1;i++) {
			for (int j=i+1;j<reviews.size();j++) {
				intersections =intersect(i, j) / 100;
				distJac=(1-intersections);
				if (distJac <= limiar) {
					System.out.printf("\nReviews com Grau Similariedade de Jaccard de %f.\n",distJac);
					System.out.printf("\tUtilizador: %s\n\t\tConte�do da review: %s\n\tUtilizador: %s\n\t\tConte�do da review: %s\n",reviews.get(i).getUser(),reviews.get(i).getReview(),reviews.get(j).getUser(),reviews.get(j).getReview());
				}
			}
		}
	}
	
	public double intersect(int i, int j) {
        double intersect = 0;
        for (int row = 0; row < 100; row++) {
            if (similar[row][i] == similar[row][j]) {
                intersect++;
            }
        }
        return intersect;
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