package mpei_p1;

public class CBloom {
	private Integer[] myBloom; // Couting Bloom
	private int m; // número máximo de elementos a inserir
	private int n; // tamnho do bloom
    private int k; // número de hash functions
    private HashFunction hashFunction;
    
	public CBloom(int m, double fator) {
		 this.m = m;
	     this.n = (int) Math.round(m/fator);
	     this.k = (int) Math.floor((n*Math.log(2)/m));
	}
    
	public void initialize() {
		myBloom = new Integer[this.n];
		for (int i = 0; i < this.n; i++)
            this.myBloom[i] = 0;	
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
		System.out.println(this.k);
	    int min = Integer.MAX_VALUE; // verificaçao para evitar caso dê overflows acho eu
	    for (int i = 0; i < this.k; i++) {
	    	System.out.println("here");
	    	int index= hashFunction.generateHash_S(ele);
	        if (myBloom[index] < min)
	            min = myBloom[index];
	        }
	    return min;
	}
	
	
	
	
	public void setHashFunction(HashFunction hashFunction) {
		this.hashFunction = hashFunction;
	}
	
	public int getK() {
		return k;
	}
	
}