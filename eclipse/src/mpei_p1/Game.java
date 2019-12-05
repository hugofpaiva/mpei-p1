package mpei_p1;

import java.util.ArrayList;

public class Game {
	private String name;
	private ArrayList<Review> reviews;
	private ArrayList<String> genres;
	private ArrayList<Language> languages;
	private double price;
	private String publisher;
	private String developer;
	
	
	public Game(String nome, ArrayList<Review> reviews, ArrayList<String> genres, ArrayList<Language> languages, double price, String publisher, String developer) {
		this.name = nome;
		this.reviews=reviews;
		this.genres=genres;
		this.languages=languages;
		this.price=price;
		this.publisher=publisher;
		this.developer=developer;
		
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

	public ArrayList<String> getGenres() {
		return genres;
	}

	public void setGenres(ArrayList<String> genres) {
		this.genres = genres;
	}

	public ArrayList<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(ArrayList<Language> languages) {
		this.languages = languages;
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	@Override
	public String toString() {
		return "Game [name=" + name + "]";
	}
}