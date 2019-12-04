package mpei_p1;

import java.util.ArrayList;
import java.util.Random;

public class HashFunction {
	private static int max = 1000000;
	private int[] randA;
	private int[] randB;
	private int n;
	private int k;
	private int prime;
	
	public HashFunction(int k, int n) {
		this.k=k;//num hash functions
		this.n = n;//nº elementos do bloom
		//Maxc tamanho máximo que as strings têm
/// meter  numero primo
		Random rand = new Random();
		while (this.prime<2001 && !isPrime(this.prime)) {
			this.prime = Math.abs(rand.nextInt(max));
		}
		
		 this.randA = new int[this.k];
	     this.randB = new int[this.k];

		 for (int i = 0; i < this.k; i++) {
	                this.randA[i] = rand.nextInt(this.prime - 1);
	                this.randB[i] = rand.nextInt(this.prime - 1);      
	     }
		
		
	}

	// Gerar Hash para String
	public ArrayList<Integer> generateHash(String s) {
		ArrayList<Integer> hashes = new ArrayList<>();
		for (int i=0; i< this.k; i++) {
			int hash=0;
			for (int j=0;j<s.length();j++) {
				hash+=((this.randA[i]*(int) s.charAt(j) + this.randB[i]) % this.prime) % this.n;
			}
			if (hash<0) {
				hash+=this.n;
			}
			hashes.add(hash % n);
		}
		return hashes;
	}
	
	public int[] generateSignatures(ArrayList<String> shingles) {
		int sign[]=new int[100];
		for (int j=0;j<100;j++) {
			int min=Integer.MAX_VALUE;
			for (int i = 0; i < shingles.size(); i++) {
				String s=shingles.get(i);
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
			sign[j]=min;
		}
		return sign;
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

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int[] getRandA() {
		return randA;
	}

	public void setRandA(int[] randA) {
		this.randA = randA;
	}

	public int[] getRandB() {
		return randB;
	}

	public void setRandB(int[] randB) {
		this.randB = randB;
	}

	public int getPrime() {
		return prime;
	}

	public void setPrime(int prime) {
		this.prime = prime;
	}
	
	

}
