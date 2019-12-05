package mpei_p1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MinHash {

	private ArrayList<Review> reviews;
	int s_shingle;// tamanho das shingles
	private HashFunction hash_shingle;
	private double[][] similar;
	
	// create matrix of similarities from arraylist
	public MinHash(ArrayList<Review> reviews, int s_shingle) {
		this.reviews = reviews;
		this.s_shingle = s_shingle;
		this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
		for (int i = 0; i < reviews.size(); i++) {
			reviews.get(i).createShingles(s_shingle); // criar shingles de todas as reviews
			reviews.get(i).setminHash_shingles(this.hash_shingle.generateSignatures(reviews.get(i).getShingles())); // gerar assinaturas de todas as reviews
		}
		createMatrix();
	}
	// read from a file the matrix of similarities of the given array
	public MinHash(ArrayList<Review> reviews, String fileName) {
		 try {
			 this.reviews = reviews;
			 this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
			  Scanner sc = new Scanner( new BufferedReader(new FileReader(fileName)));
		      int rows = 100;
		      int columns = reviews.size();
		      this.similar = new double[rows][columns];
		      while(sc.hasNextLine()) {
		         for (int i=0; i<this.similar.length; i++) {
		            String[] line = sc.nextLine().trim().split(" ");
		            for (int j=0; j<line.length; j++) {
		               this.similar[i][j] = Double.parseDouble((line[j]));
		            }
		         }
		      }
		 
		      sc.close();
		 } catch (IOException e) {}
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
	
	// used to print the matrix of all reviews to a file, because not enought memory to store all shingles in each Review had to create a temporary shingle arrayList
	public MinHash(int s_shingle, ArrayList<Review> reviews) {
			this.reviews = reviews;
			this.s_shingle = s_shingle;
			this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
			for (int i = 0; i < reviews.size(); i++) {
				String string = reviews.get(i).getReview();
				String string_no_space = string.replaceAll("\\s+", "");
				int num_shingles = string_no_space.length() - s_shingle + 1;
				ArrayList<String> shingles = new ArrayList<>();
				for (int j = 0; j < num_shingles; j++) {
					shingles.add(string_no_space.substring(j, j + s_shingle));
				}
				
				reviews.get(i).setminHash_shingles(this.hash_shingle.generateSignatures(shingles)); // gerar assinaturas de todas as reviews
			}
			createMatrix();
		 try {
			 
			File file = new File ("ReviewsSignatures.txt");
			PrintWriter printWriter = new PrintWriter (file);
		    for (int i = 0; i < similar.length; i++) {
		        for (int j = 0; j < similar[i].length; j++) {
		            printWriter.printf("%.0f ",similar[i][j]);
		        }
		        printWriter.printf("\n");
		    }
		    printWriter.close();
		 } catch (IOException e) {}
	}

	public void printSimilars(double limiar) {
		double inters;
		for (int i=0; i<reviews.size()-1;i++) {
			for (int j=i+1;j<reviews.size();j++) {
				inters =inter(i, j);
				if (inters>= limiar) {
					System.out.printf("\nReviews com Grau Similariedade de %.2f%%.\n",inters);
					System.out.printf("\tUtilizador: %s\n\t\tConte�do da review: %s\n\tUtilizador: %s\n\t\tConte�do da review: %s\n",reviews.get(i).getUser(),reviews.get(i).getReview(),reviews.get(j).getUser(),reviews.get(j).getReview());
				}
			}
		}
	}
	public void printSimilarsNoSpam(double limiar, ArrayList<Review> spam) {
		double inters;
		boolean found=false;
		for (int i=0; i<this.getReviews().size()-1;i++) {
			for (int k=0;k<spam.size();k++) {
				if (this.getReviews().get(i).getReview().equals(spam.get(k).getReview())) {
					found=true;
					break;
				}
			}
			if (found==true) {
				found=false;
				continue;
			}
			for (int j=i+1;j<this.getReviews().size();j++) {
				for (int k=0;k<spam.size();k++) {
					if (this.getReviews().get(j).getReview().equals(spam.get(k).getReview())) {
						continue;
					}
				}
				if (found==true) {
					found=false;
					continue;
				}
				inters =inter(i, j);
				if (inters>= limiar) {
					System.out.printf("\nReviews com Grau Similariedade de %.2f%%.\n",inters);
					System.out.printf("\tUtilizador: %s\n\t\tConte�do da review: %s\n\tUtilizador: %s\n\t\tConte�do da review: %s\n",this.getReviews().get(i).getUser(),this.getReviews().get(i).getReview(),this.getReviews().get(j).getUser(),this.getReviews().get(j).getReview());
				}
			}
		}
	}
	
	public ArrayList<Review> removeSpam() {
		double limiar=95;
		double inters;
		boolean found=false;
		
		ArrayList<Review> spam= new ArrayList<>();
		
		for (int i=0; i<this.reviews.size()-1;i++) {
			for (int j=i+1;j<this.reviews.size();j++) {
				inters =inter(i, j);
				if (inters>= limiar) {
					found=true;
					spam.add(this.getReviews().get(j));
				}
            }
			if (found==true) {
				spam.add(this.getReviews().get(i));
				found=false;
			}
		}
		
		return spam;
	}
	
	
	
	
	public HashMap<Review, Double> findSimilars(String rev, double limiar){
		HashMap<Review,Double> simRevs=new HashMap<>();
		
		String string_no_space = rev.replaceAll("\\s+", "");
		int num_shingles = string_no_space.length() - s_shingle + 1;
		ArrayList<String> shingles = new ArrayList<>();
		for (int j = 0; j < num_shingles; j++) {
			shingles.add(string_no_space.substring(j, j + s_shingle));
		}
		
		int[] sign;
		sign=this.hash_shingle.generateSignatures(shingles);
		
		for (int i=0;i<reviews.size();i++) {
			double inter=0;
	        for (int row = 0; row < 100; row++) {
	            if (similar[row][i] == sign[row]) {
	                inter++;
	            }
	        }
			if (inter>= limiar) {
				simRevs.put(reviews.get(i), inter);
			}
		}
		
		return simRevs;
	}	
	
	
	public double inter(int i, int j) {
        double inter = 0;
        for (int row = 0; row < 100; row++) {
            if (similar[row][i] == similar[row][j]) {
                inter++;
            }
        }
        return inter;
    }
	
	
	
	public HashFunction getHash_shingle() {
		return hash_shingle;
	}


	public void setHash_shingle(HashFunction hash_shingle) {
		this.hash_shingle = hash_shingle;
	}

	public double[][] getSimilar() {
		return similar;
	}

	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	public void setSimilar(double[][] similar) {
		this.similar = similar;
	}

}