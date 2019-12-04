package mpei_p1;

import java.util.ArrayList;
import java.util.Random;

public class HashFunction {
	private static int max = 1000000;
	private ArrayList<Integer> seeds = new ArrayList<>();
	private int[][] randA;
	private int[][] randB;
	private int n;
	private int k;
	private int prime;
	
	public HashFunction(int k, int n, int maxC) {
		this.k=k;//num hash functions
		this.n = n;//nº elementos do bloom
		//Maxc tamanho máximo que as strings têm

		Random rand = new Random();
		while (this.prime<2001 && !isPrime(this.prime)) {
			this.prime = Math.abs(rand.nextInt(max));
		}
		
		 this.randA = new int[this.k][maxC];
	     this.randB = new int[this.k][maxC];

		 for (int i = 0; i < this.k; i++) {
	            for (int j = 0; j < maxC; j++) {
	                this.randA[i][j] = rand.nextInt(this.prime - 1);
	                this.randB[i][j] = rand.nextInt(this.prime - 1);
	            }
	     }
		
		
	}

	// Gerar Hash para String
	public ArrayList<Integer> generateHash(String s) {
		ArrayList<Integer> hashes = new ArrayList<>();
		for (int i=0; i< this.k; i++) {
			int hash=0;
			for (int j = 0; j < s.length(); j++) {
				hash+=((this.randA[i][j]*(int) s.charAt(j) + this.randB[i][j]) % this.prime) % this.n;
			}
			hashes.add(hash % n);
		}
		return hashes;
	}
	
	public ArrayList<Integer> generateHash_S(ArrayList<String> s) {
		ArrayList<Integer> hashes = new ArrayList<>();
		int min;
		int hash;
		min = Integer.MAX_VALUE;
		for (int i=0; i< this.k; i++) {
			for(String shingle : s) {
				hash=0;
				min = Integer.MAX_VALUE;
				for (int j = 0; j < shingle.length(); j++) {
					hash+=((this.randA[i][j]*(int) shingle.charAt(j) + this.randB[i][j]) % this.prime);
				}
				hash=hash%n;
				if( hash < min) {
					min=hash;
				}
			}
			hashes.add(min);
			
		}
		return hashes;
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

	public ArrayList<Integer> getSeeds() {
		return seeds;
	}

	public void setSeeds(ArrayList<Integer> seeds) {
		this.seeds = seeds;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

}
