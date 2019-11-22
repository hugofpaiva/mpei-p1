package mpei_p1;

public class Review {
	private String user;
	private String review;
	
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

	@Override
	public String toString() {
		return "Review [user=" + user + ", review=" + review + "]";
	}
	
	
	
	

}
