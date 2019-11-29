package mpei_p1;

public class CBloom {
	private int m; // número de elementos a inserir
	private int n; // tamnho do bloom
    private int k; // número de hash functions
    private double fator; // fator de carga do bloom 
    
	public CBloom(int m, double fator) {
		this.m = m;
		this.fator = fator;
		this.k=(int) (Math.round(n/m*Math.log(2)));
		this.n=(int) Math.round(m/fator);
	}
    
	public void initialize() {
		int[] CBloom = new int[this.n];
		for (int i = 0; i < n; i++)
            CBloom[i] = 0;	
	}
	
	public boolean isElement() {
		return false;
	}
	
	public boolean delElement() {
		return false;
	}
	
	public boolean insElement() {
		return false;
	}
	
}