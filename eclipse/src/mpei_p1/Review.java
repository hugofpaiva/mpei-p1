package mpei_p1;

import java.util.ArrayList;

public class Review {
	private String user;
	private String review;
	private ArrayList<String> shingles = new ArrayList<>();
	private int[] minhash_shingles = new int[100];
	
	public Review(String user, String review) {
		this.user = user;
		this.review = review;
	}
	
	public void createShingles(int s_shingle) {
		String string = this.review;
		String string_no_space = string.replaceAll("\\s+", "");
		int num_shingles = string_no_space.length() - s_shingle + 1;
		
		for (int i = 0; i < num_shingles; i++) {
			this.shingles.add(string_no_space.substring(i, i + s_shingle));
		}
    }
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}

	public ArrayList<String> getShingles() {
		return shingles;
	}
	
	public void addShingles(String shingle) {
		this.shingles.add(shingle);
	}

	public void setShingles(ArrayList<String> shingles) {
		this.shingles = shingles;
	}

	public int[] getminHash_shingles() {
		return minhash_shingles;
	}

	public void setminHash_shingles(int[] hash_shingles) {
		this.minhash_shingles = hash_shingles;
	}

	public void addminHash_shingles(int hash_s, int pos) {
		this.minhash_shingles[pos]=hash_s;
	}
	
	@Override
	public String toString() {
		return "Review [user=" + user + ", review=" + review + "]";
	}
	
	
	
	

}
