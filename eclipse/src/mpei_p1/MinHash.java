package mpei_p1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MinHash {
	
	private ArrayList<Review> reviews;
	int k;

	public MinHash(ArrayList<Review> reviews, int k) {
		this.reviews=reviews;
		this.k=k;
		getProfile(reviews.get(0).getReview());
	}
	
	public Map<String, Integer> getProfile(String string) {
        HashMap<String, Integer> shingles = new HashMap<String, Integer>();

        String string_no_space = string.replaceAll("\\s+","");	
        for (int i = 0; i < (string_no_space.length() - k + 1); i++) {
            String shingle = string_no_space.substring(i, i + k);
            Integer old = shingles.get(shingle);
            if (old != null) {
                shingles.put(shingle, old + 1);
            } else {
                shingles.put(shingle, 1);
            }
        }
        System.out.println(shingles);

        return Collections.unmodifiableMap(shingles);
    }
}