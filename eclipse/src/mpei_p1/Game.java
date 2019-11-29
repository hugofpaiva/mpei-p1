package mpei_p1;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
	private String name;
	private ArrayList<Review> reviews = new ArrayList<>();
	
	
	public Game(String nome, ArrayList<Review> reviews) {
		this.name = nome;
		this.reviews=reviews;
		
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