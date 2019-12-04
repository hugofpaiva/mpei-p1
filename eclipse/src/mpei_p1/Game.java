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
	private ArrayList<String> shingles_name = new ArrayList<>();
	private int[] minhash_shingles_name = new int[100];
	
	
	public Game(String nome, ArrayList<Review> reviews, ArrayList<String> genres, ArrayList<Language> languages, double price, String publisher, String developer) {
		this.name = nome;
		this.reviews=reviews;
		this.genres=genres;
		this.languages=languages;
		this.price=price;
		this.publisher=publisher;
		this.developer=developer;
		
	}
	
	public void createShingles(int s_shingle) {
		String string = this.name;
		String string_no_space = string.replaceAll("\\s+", "");
		int num_shingles = string_no_space.length() - s_shingle + 1;
		
		for (int i = 0; i < num_shingles; i++) {
			this.shingles_name.add(string_no_space.substring(i, i + s_shingle));
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
	
	public ArrayList<String> getShingles_name() {
		return shingles_name;
	}

	public void setShingles_name(ArrayList<String> shingles_name) {
		this.shingles_name = shingles_name;
	}

	public int[] getminHash_shingles_name() {
		return minhash_shingles_name;
	}

	public void setminHash_shingles_name(int[] hash_shingles) {
		this.minhash_shingles_name = hash_shingles;
	}

	public void addminHash_shingles_name(int hash_s, int pos) {
		this.minhash_shingles_name[pos]=hash_s;
	}

	@Override
	public String toString() {
		return "Game [name=" + name + "]";
	}
}