package mpei_p1;

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
	     this.hashFunction=new HashFunction(k, m);
	}
    
	public void initialize() {
		myBloom = new Integer[this.n];
		for (int i = 0; i < this.n; i++)
            this.myBloom[i] = 0;	
	}
	
	@Override
	public String toString() {
		return "CBloom [myBloom=" + Arrays.toString(myBloom) + "]";
	}

	public boolean isEle(String ele) {
		int index= hashFunction.generateHash_S(ele);
		if (myBloom[index] == 0) {
            return false;
        }
		return true;
	}
	
	public void deleteEle(String ele) {
		int index= hashFunction.generateHash_S(ele);
		myBloom[index] = 0;
	}
	
	public void insertEle(String ele) {
		int index= hashFunction.generateHash_S(ele);
        myBloom[index]++;
	}
	
	public int numEle(String ele) {
	    int min = Integer.MAX_VALUE; // verifica�ao para evitar caso d� overflows acho eu
	    int index= hashFunction.generateHash_S(ele);
	    if (myBloom[index] < min)
	    	min = myBloom[index];
	    return min;
	}
	
	
	
}