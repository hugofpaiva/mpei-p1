package mpei_p1;

import java.util.ArrayList;

public class MinHashLSH extends MinHash {
	private int rows;
    private int bands;
    private int[] randValsA;
    private int[][] minHashR;
	

	public MinHashLSH(ArrayList<Review> reviews, int s_shingle) {
		super(reviews,s_shingle);
		this.bands = () / reviews.size();
	    this.minHashR = new int[dataSet.keySet().size()][this.bands];
		
	}

}
