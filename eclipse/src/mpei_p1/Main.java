package mpei_p1;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	
	private static HashMap<String,String> reviews = new HashMap<>();
	private static ArrayList<Game> jogos = new ArrayList<>();
	
	public static void main(String[] args) {
		ReadJSON json = new ReadJSON();
		json.read();
		jogos = json.getJogos();
		reviews = json.getReviews();
	}

}
