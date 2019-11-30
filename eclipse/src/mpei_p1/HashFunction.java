package mpei_p1;

import java.util.ArrayList;
import java.util.Random;

public class HashFunction {
	private static int max = 1000000;
	private static int static_prime = 3089;
	private ArrayList<Integer> seeds = new ArrayList<>();
	private int n;

	public HashFunction(int k, int n) {
		this.n = n;
		int prime;
		Random rand = new Random();
		for (int i = 0; i < k; i++) {

			prime = Math.abs(rand.nextInt(max));
			while (!isPrime(prime) && !seeds.contains(prime)) {
				prime = Math.abs(rand.nextInt(max));
			}
			this.seeds.add(prime);
		}
	}

	// Gerar Hash para String
	public ArrayList<Integer> generateHash_S(String s) {
		ArrayList<Integer> hashes = new ArrayList<>();
		int max= Integer.MAX_VALUE;
		for (int i = 0; i < seeds.size(); i++) {
			int hash = 0;
			for (int z = 0; z < s.length(); z++) {
				if (((hash+static_prime*seeds.get(i) * (int) s.charAt(z)) % n) > 0) {
					hash += (static_prime*seeds.get(i) * (int) s.charAt(z)) % n;
				}
			}
			hashes.add(hash % n);
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
