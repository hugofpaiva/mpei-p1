package mpei_p1;
import java.util.ArrayList;
import java.util.Random;

public class HashFunction2 {
		private static int max = 1000000;
		private static int static_prime = 3847;
		private ArrayList<Integer> seeds = new ArrayList<>();
		private int n;

		public HashFunction2(int k, int n) {
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
		public ArrayList<Integer> generateHash(String s) {
			ArrayList<Integer> hashes = new ArrayList<>();
			int min;
			int hash;
			for (int i = 0; i < seeds.size(); i++) {
				hash = 0;
				for (int z = 0; z < s.length(); z++) {
					hash += static_prime * seeds.get(i) * s.charAt(z);
				}

				hashes.add(hash % n);
			}
			return hashes;

		}
		
		public ArrayList<Integer> generateHash_S(String s) {
			ArrayList<Integer> hashes = new ArrayList<>();
			for (int i = 0; i < seeds.size(); i++) {
				int hash = 0;
				for (int z = 0; z < s.length(); z++) {
					hash += static_prime * seeds.get(i) * s.charAt(z);
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

