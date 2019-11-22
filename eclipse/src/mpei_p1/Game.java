package mpei_p1;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
	private String name;
	private ArrayList<Review> reviews = new ArrayList<>();
	
	
	public Game(String nome, HashMap<String,String> review) {
		this.name = nome;
		addReview(review);
		
	}
	
	private void addReview(HashMap<String,String> review) {
		for (String key : review.keySet()) {
			Review r = new Review(key, review.get(key));
			reviews.add(r);
		}
		
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Review> getReviews() {
		return reviews;
	}
	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	@Override
	public String toString() {
		return "Game [name=" + name + "]";
	}
	
	
	
	
	
	
	
}
	
	

