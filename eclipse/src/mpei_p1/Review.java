package mpei_p1;

import java.util.ArrayList;

public class Review {
	private String user;
	private String review;
	private ArrayList<String> shingles = new ArrayList<>();
	private ArrayList<Integer> minhash_shingles = new ArrayList<>();
	
	
	public Review(String user, String review) {
		this.user = user;
		this.review = review;
		
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

	public ArrayList<Integer> getminHash_shingles() {
		return minhash_shingles;
	}

	public void setminHash_shingles(ArrayList<Integer> hash_shingles) {
		this.minhash_shingles = hash_shingles;
	}
	
	public void addminHash_shingles(Integer hash_s) {
		this.minhash_shingles.add(hash_s);
	}

	@Override
	public String toString() {
		return "Review [user=" + user + ", review=" + review + "]";
	}
	
	
	
	

}
