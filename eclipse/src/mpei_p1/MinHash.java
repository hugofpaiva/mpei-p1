package mpei_p1;

import java.util.ArrayList;
import java.util.Arrays;

public class MinHash {

	private ArrayList<Review> reviews = new ArrayList<Review>();
	private ArrayList<Game> jogos = new ArrayList<Game>();
	int s_shingle;// tamanho das shingles
	private HashFunction hash_shingle;
	private double[][] similar_r; // array multidimensional com 100 rows e x colunas (cada coluna representa uma review)
	private double[][] similar_j; // array multidimensional com 100 rows e x colunas (cada coluna representa uma review)


	public MinHash(ArrayList<Review> reviews, int s_shingle) {
		similar_r = new double[reviews.size()][reviews.size()];
		this.reviews = reviews;
		this.s_shingle = s_shingle;
		this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
		for (int i = 0; i < reviews.size(); i++) {
			reviews.get(i).createShingles(s_shingle); // criar shingles de todas as reviews
			reviews.get(i).setminHash_shingles(this.hash_shingle.generateSignatures(reviews.get(i).getShingles())); // gerar assinaturas de todas as reviews
		}
		createMatrix();
	}

	public MinHash(int s_shingle, ArrayList<Game> jogos) {
		similar_j= new double[jogos.size()][jogos.size()];
		this.jogos = jogos;
		this.s_shingle = s_shingle;
		this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
		for (int i = 0; i < jogos.size(); i++) {
			jogos.get(i).createShingles(s_shingle); // criar shingles de todas as reviews
			jogos.get(i).setminHash_shingles_name(this.hash_shingle.generateSignatures(jogos.get(i).getShingles_name())); // gerar assinaturas de todas as reviews
		}
		createMatrix_jogo();
	}
	// Criar matrizes depende do que é introduzido na minhash
	private void createMatrix() {
		this.similar_r=new double[100][reviews.size()];
		for (int i=0; i<this.reviews.size();i++) {
			int[] minHashShingles=reviews.get(i).getminHash_shingles();
			for (int row=0;row<100;row++) {
				this.similar_r[row][i]=minHashShingles[row];
			}
		}		
	}
	
	private void createMatrix_jogo() {
		this.similar_j=new double[100][jogos.size()];
		for (int i=0; i<this.jogos.size();i++) {
			int[] minHashShingles=jogos.get(i).getminHash_shingles_name();
			for (int row=0;row<100;row++) {
				this.similar_j[row][i]=minHashShingles[row];
			}
		}
	}
	
	
	// Print dos similares
	public void printSimilars_r(double limiar) {
		double intersections;
		for (int i=0; i<reviews.size()-1;i++) {
			for (int j=i+1;j<reviews.size();j++) {
				intersections =intersect_r	(i, j) ;
				if (intersections >= limiar) {
					System.out.printf("\nReviews com Grau Similariedade de %f.\n", intersections);
					System.out.printf("\tUtilizador: %s\n\t\tConte�do da review: %s\n\tUtilizador: %s\n\t\tConte�do da review: %s\n",reviews.get(i).getUser(),reviews.get(i).getReview(),reviews.get(j).getUser(),reviews.get(j).getReview());
				}
			}
		}
	}
	
	public void printSimilars_j(double limiar, String name) {
		double intersections;
		
		//criar shingle para o nome
		ArrayList<String> shingles_name_j = new ArrayList<>();
		String string = name;
		String string_no_space = string.replaceAll("\\s+", "");
		int num_shingles = string_no_space.length() - s_shingle + 1;
		
		for (int i = 0; i < num_shingles; i++) {
			shingles_name_j.add(string_no_space.substring(i, i + s_shingle));
		}
		//criar o array hash para o nome
		int sign[];
		sign=this.hash_shingle.generateSignatures(shingles_name_j);
		
		//Ver interseções e criar 
		for (int i=0; i<jogos.size();i++) {
				intersections =intersect_jogo_ele(i, sign) ;
				if (intersections >= limiar) { // 0 a 100
					System.out.printf("\nJogos com Grau Similariedade de %f.\n", intersections);
					System.out.println(jogos.get(i).getName());
				}
			}
		}
	
	
	public double intersect_r(int i, int j) {
        double intersect = 0;
        for (int row = 0; row < 100; row++) {
            if (similar_r[row][i] == similar_r[row][j]) {
                intersect++;
            }
        }
        return intersect;
    }
	
	public double intersect_jogo_ele(int i, int[] sign) {
        double intersect = 0;
        for (int row = 0; row < 100; row++) {
            if (similar_j[row][i] == sign[row]) {
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



}