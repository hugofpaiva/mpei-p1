package mpei_p1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MinHashLSH extends MinHash{
	private int rows;
    private int bands;
    private double[][] minHashLSH;
    private int size;

	public MinHashLSH(ArrayList<Review> reviews, int s_shingle, int rows) {
		super(reviews,s_shingle);
		this.rows=rows;
		this.size=reviews.size();
		this.bands = 100/rows;
	    this.minHashLSH = new double[this.bands][size];
        createMinHashLSH();
	}
	
	public MinHashLSH(ArrayList<Review> reviews, int rows, String filename) {
		super(reviews, filename);
		this.size=reviews.size();
		this.rows=rows;
		this.bands = 100/rows;
	    this.minHashLSH = new double[this.bands][size];
        createMinHashLSH();
	}
	
	
	 private void createMinHashLSH() {
		for (int col = 0; col < this.size; col++) {
			for (int band = 0; band < this.bands; band++) {
                int hashVal=0;
                for (int i = 0; i < this.rows; i++) {
                    hashVal += this.getHash_shingle().hashInt(this.getSimilar()[i + this.rows * band][col]);
                }
                this.minHashLSH[band][col]=hashVal;
            }
        }
	 }
	 
	 public void printSimilarsLSH(double limiar) {
		double inters;
		for (int i=0; i<this.size-1;i++) {
			for (int j=i+1;j<this.size;j++) {
				if (interLSH(i, j) == 1) {
					inters =intersect_r(i, j);
					if (inters >= limiar) {
						System.out.printf("\nReviews com Grau Similariedade de %.2f%%.\n",inters);
						System.out.printf("\tUtilizador: %s\n\t\tConte�do da review: %s\n\tUtilizador: %s\n\t\tConte�do da review: %s\n",this.getReviews().get(i).getUser(),this.getReviews().get(i).getReview(),this.getReviews().get(j).getUser(),this.getReviews().get(j).getReview());
					}
				}
			}
		}
	}
	 
		public HashMap<Review, Double> findSimilarsLSH(String rev, double limiar){
			HashMap<Review,Double> simRevs=new HashMap<>();
			ArrayList<String> shingles = new ArrayList<>();
			
			String string_no_space = rev.replaceAll("\\s+", "");
			int num_shingles = string_no_space.length() - 3 + 1;
			
			for (int j = 0; j < num_shingles; j++) {
				shingles.add(string_no_space.substring(j, j + 3));
			}
			
			int[] sign=new int[100];
			sign=this.getHash_shingle().generateSignatures(shingles);

			double[] signLSH = new double[this.bands];
			for (int band = 0; band < this.bands; band++) {
				int hashVal=0;
				for (int i = 0; i < this.rows; i++) {
                    hashVal += this.getHash_shingle().hashInt(sign[i + this.rows * band]);
                }
                signLSH[band]=hashVal;
            }

			for (int i=0;i<this.size;i++) {
				double inter=0;
		        for (int row = 0; row < bands; row++) {
	                if (minHashLSH[row][i] == signLSH[row]) {
	                	for (int row2 = 0; row2 < 100; row2++) {
	    	                if ((int) this.getSimilar()[row2][i]==(sign[row2])) {
	    	                	inter++;
	    	                }
	    	            }
	                	if (inter >= limiar) {
	    					simRevs.put(this.getReviews().get(i), inter);
	    				}
	                	break;
	                }
		        }
			}
			
			return simRevs;
		}	
	 
	 public void printSimilarsNoSpamLSH(double limiar, ArrayList<Review> spam) {
			double inters;
			boolean found=false;
			for (int i=0; i<this.size-1;i++) {
				for (Review rev: spam) {
					if (this.getReviews().get(i).getReview().equals(rev.getReview())) {
						found=true;
						break;
					}
				}
				if (found==true) {
					found=false;
					continue;
				}
				for (int j=i+1;j<this.size;j++) {
					for (Review rev: spam) {
						if (this.getReviews().get(j).getReview().equals(rev.getReview())) {
							found=true;
							break;
						}
					}
					if (found==true) {
						found=false;
						continue;
					}
					if (interLSH(i,j)==1) {
						inters =intersect_r(i, j);
						if (inters>= limiar) {
							System.out.printf("\nReviews com Grau Similariedade de %.2f%%.\n",inters);
							System.out.printf("\tUtilizador: %s\n\t\tConte�do da review: %s\n\tUtilizador: %s\n\t\tConte�do da review: %s\n",this.getReviews().get(i).getUser(),this.getReviews().get(i).getReview(),this.getReviews().get(j).getUser(),this.getReviews().get(j).getReview());
						}
					}
				}
			}
		}
	 
	 
	 
	 public HashSet<Review> removeSpamLSH() {
		 	double limiar=95;
			double inters;
			
			HashSet<Review> spam= new HashSet<>();
			
			for (int i=0; i<this.size-1;i++) {
				for (int j=i+1;j<this.size;j++) {
					if(interLSH(i,j)==1) {
						inters =intersect_r(i, j);
						if (inters>= limiar) {
							spam.add(this.getReviews().get(j));
							spam.add(this.getReviews().get(i));
						}
					}
	            }
			}
			return spam;
	 }
	 
	 public int interLSH(int i, int j) {
        int inter = 0;
        for (int rowB = 0; rowB < this.bands; rowB++) {
            if (minHashLSH[rowB][i] == minHashLSH[rowB][j]) {
                inter=1;
                break;
            }
        }
        return inter;
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
