package mpei_p1;

import java.util.ArrayList;

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
        double[] bandRows = new double[rows];
        for (int nB = 0; nB < this.bands; nB++) {
        	for (int u = 0; u < this.size; u++) {
                for (int i = 0; i < this.rows; i++) {
                    bandRows[i] = this.getSimilar()[i + this.rows * nB][u];
                }
                this.minHashLSH[nB][u] = 0;
                int hashVal=0;
                for (int i = 0; i < this.rows; i++) {
                    hashVal += this.getHash_shingle().hashInt(bandRows[i], i);
                }
                this.minHashLSH[nB][u]=hashVal;
            }
        }
	 }
	 
	 public void printSimilarsLSH(double limiar) {
		double inters;
		for (int i=0; i<this.size-1;i++) {
			for (int j=i+1;j<size;j++) {
				if (interLSH(i, j) == 1) {
					inters =inter(i, j);
					if (inters >= limiar) {
						System.out.printf("\nReviews com Grau Similariedade de %.2f%%.\n",inters);
						System.out.printf("\tUtilizador: %s\n\t\tConte�do da review: %s\n\tUtilizador: %s\n\t\tConte�do da review: %s\n",this.getReviews().get(i).getUser(),this.getReviews().get(i).getReview(),this.getReviews().get(j).getUser(),this.getReviews().get(j).getReview());
					}
				}
			}
		}
	}
	 
	 
	 public void printSimilarsNoSpamLSH(double limiar, ArrayList<Review> spam) {
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
					if (interLSH(i,j)==1) {
						inters =inter(i, j);
						if (inters>= limiar) {
							System.out.printf("\nReviews com Grau Similariedade de %.2f%%.\n",inters);
							System.out.printf("\tUtilizador: %s\n\t\tConte�do da review: %s\n\tUtilizador: %s\n\t\tConte�do da review: %s\n",this.getReviews().get(i).getUser(),this.getReviews().get(i).getReview(),this.getReviews().get(j).getUser(),this.getReviews().get(j).getReview());
						}
					}
				}
			}
		}
	 
	 
	 
	 public ArrayList<Review> removeSpamLSH() {
		 double limiar=95;
			double inters;
			boolean found=false;
			
			ArrayList<Review> spam= new ArrayList<>();
			
			for (int i=0; i<this.size-1;i++) {
				for (int j=i+1;j<this.size;j++) {
					if(interLSH(i,j)==1) {
						inters =inter(i, j);
						if (inters>= limiar) {
							found=true;
							spam.add(this.getReviews().get(j));
						}
					}
	            }
				if (found==true) {
					spam.add(this.getReviews().get(i));
					found=false;
				}
			}
			
			return spam;
	 }
	 
	 public int interLSH(int i, int j) {
        int inter = 0;
        for (int row = 0; row < this.rows; row++) {
            if (minHashLSH[row][i] == minHashLSH[row][j]) {
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
