package mpei_p1;

import java.util.ArrayList;
import java.util.Arrays;

public class CBloom {
	private Integer[] myBloom; // Couting Bloom
	private int m; // n�mero m�ximo de elementos a inserir
	private int n; // tamnho do bloom
    private int k; // n�mero de hash functions
    private HashFunction hashFunction;
    
	public CBloom(int m, double fator) {
		 this.m = m;
	     this.n = (int) Math.round(m/fator);
	     this.k = (int) Math.floor((n*Math.log(2)/m));
	     this.hashFunction=new HashFunction(k, n);
	     myBloom = new Integer[this.n];
		 for (int i = 0; i < this.n; i++) {
             this.myBloom[i] = 0;
		 }
	}
    
	
	@Override
	public String toString() {
		return "CBloom [myBloom=" + Arrays.toString(myBloom) + "]";
	}

	public boolean isEle(String ele) {
		boolean test=true;
		ArrayList<Integer> index= hashFunction.generateHash(ele);
		for (int i=0;i<index.size();i++) {
			if (myBloom[index.get(i)] == 0) {
				test=false;
	        }
		}
		return test;
	}
	
	public void deleteEle(String ele) {
		ArrayList<Integer> index= hashFunction.generateHash(ele);
		if (isEle(ele)) {
			int numRem= numEle(ele);
			for (int i=0;i<index.size();i++) {
				myBloom[index.get(i)]-=numRem;
			}			
		}
	}

	public void insertEle(String ele) {
		ArrayList<Integer> index= hashFunction.generateHash(ele);
		for (int i=0;i<index.size();i++) {
			myBloom[index.get(i)]++;
		}
	}
	
	public int numEle(String ele) {
	    int min = Integer.MAX_VALUE; 
	    ArrayList<Integer> index= hashFunction.generateHash(ele);
		for (int i=0;i<index.size();i++) {
		    if (myBloom[index.get(i)] < min)
		    	min = myBloom[index.get(i)];
		}
	    return min;
	}
	
	
	public int getN() {
		return this.n;
	}

	public int getK() {
		return this.k;
	}
	
	public int getM() {
		return this.m;
	}
	
	
}