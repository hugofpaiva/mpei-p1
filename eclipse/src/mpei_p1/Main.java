package mpei_p1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
	static int numNoRev, numRev;
	private static CBloom reviewsBloom;
	private static CBloom noReviewsBloom;
	private static Boolean found=false;
	private static HashMap<String, CBloom> linguagens = new HashMap<>();
	private static HashMap<String, CBloom> publishers = new HashMap<>(); 
	private static HashMap<String, CBloom> developers = new HashMap<>(); 
	private static HashMap<String, CBloom> linguagens_text = new HashMap<>(); 
	private static HashMap<String, CBloom> linguagens_audio = new HashMap<>(); 
	private static ReadJSON json;
	
	private static ArrayList<Game> jogos = new ArrayList<>();
	
	private static void teste() throws InterruptedException {
		System.out.println("------------------READING JSON------------------");
	 	long startTime = System.nanoTime();
		ReadJSON json = new ReadJSON();
		json.read();
		jogos = json.getJogos();
		
		long stopTime = System.nanoTime();
		long elapsedTime = stopTime-startTime;
		double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
		System.out.println("Time elapsed to read json (s): " + elapsedTimeInSecond+" seconds.");
	    
	    System.out.println("------------------CREATING BLOOM------------------");
	    startTime = System.nanoTime();

	    // CriaÁao do bloom e inserir a HashFunction para o bloom usar
		CBloom reviewsBloom = new CBloom(jogos.size(), 0.1);
		reviewsBloom.initialize();
		
		stopTime = System.nanoTime();
		elapsedTime = stopTime-startTime;
		elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
		System.out.println("Time elapsed to create bloom (s): " + elapsedTimeInSecond+" seconds.");
		
		System.out.println("------------------Testes ao BLOOM------------------");
		TimeUnit.SECONDS.sleep(1);
		
		System.out.println("------------------Dados reais------------------");
		int numIns=0;
		for (int i=0;i<jogos.size();i++) {
        	ArrayList<Review> reviews = jogos.get(i).getReviews();
        	if (jogos.get(i).getName().equals("Might and Magic√Ç¬Æ 6-pack Limited Edition")) {
            	System.out.println("n de reviews reais de migth: "+reviews.size());
        	}
        	if (jogos.get(i).getName().equals("Galactic Civilizations III - Revenge of the Snathi DLC")) {
    			System.out.println("n de reviews reais de galatic: "+reviews.size());
    		}
        	if (jogos.get(i).getName().equals("Enclave")) {
    			System.out.println("n de reviews reais de enclave: "+reviews.size());
    		}

        	if (reviews.size() > 0) {
        		for (int j=0;j<reviews.size();j++) {
	            	reviewsBloom.insertEle(jogos.get(i).getName());
        		}
        		numIns++;
        	}
		}
		TimeUnit.SECONDS.sleep(1);
		System.out.println("------------------Dados do BLOOM------------------");
		System.out.println("enclave tem reviews? "+reviewsBloom.isEle("Enclave")); // tem de dar true
		System.out.println("galatic tem reviews? "+reviewsBloom.isEle("Galactic Civilizations III - Revenge of the Snathi DLC")); // tem de dar false
		System.out.println("might tem reviews? "+ reviewsBloom.isEle("Might and Magic√Ç¬Æ 6-pack Limited Edition")); // tem de dar true
		
		
		TimeUnit.SECONDS.sleep(1);
		System.out.println("enclave tem quantas reviews? "+reviewsBloom.numEle("Enclave")); // tem de dar 68
		System.out.println("galatic tem quantas reviews? "+reviewsBloom.numEle("Galactic Civilizations III - Revenge of the Snathi DLC")); // tem de dar 0
		System.out.println("migth tem quantas reviews? "+reviewsBloom.numEle("Might and Magic√Ç¬Æ 6-pack Limited Edition")); // tem de dar 133
		
		//reviewsBloom.deleteEle("Might and Magic√Ç¬Æ 6-pack Limited Edition");
		//System.out.println(reviewsBloom.isEle("Might and Magic√Ç¬Æ 6-pack Limited Edition")); // tem de dar false
		
		double pfp=Math.pow(1-Math.pow(1-1/reviewsBloom.getN(), reviewsBloom.getK()*reviewsBloom.getM()), reviewsBloom.getK());
		System.out.println("Probabilidade de falso positivo: " + pfp);
		
		int pos=0;
		for (int i=0;i<jogos.size();i++) {
			if (reviewsBloom.isEle(jogos.get(i).getName())) {
				pos++;
			}
		}
		
		System.out.println("Numero de elementos inseridos no bloom: " + numIns);
		System.out.println("Numero de elementos que estão no bloom: " + pos);
		System.out.println("Numero de colisoes: "+ (pos-numIns));
		
		
		System.out.println("------------------Testes à MinHash------------------");
		//display_menu();
				ArrayList<Review> reviews= new ArrayList<>();
				int size=0;
				for (Game jogo : jogos) {
					for(Review r: jogo.getReviews()) {
					size++;
			

						reviews.add(r);
				
				}}
				//System.out.println(reviews);

				MinHash lol = new MinHash(reviews, 10); //10 pq são frases grandes
				
				lol.printSimilars_r(90); // Similariedade, quanto maior mais similares são. Entre 0 e 1
	}
	
	public static ArrayList<String> language() {	    
		//Bloom para determinar se um jogo tem uma certa linguagem 
			   
		ArrayList<String> languages = new ArrayList<>();
		for(Game jogo:jogos) {
			for(Language lang:jogo.getLanguages()) {
				if(!languages.contains(lang.getName()) && !lang.getName().isEmpty()) {
					languages.add(lang.getName());
					//Bloom para determinar se um jogo tem uma certa linguagem 
					CBloom l = new CBloom(jogos.size(), 0.1); // bloom
					l.initialize();
					linguagens.put(lang.getName(), l);
					//Bloom para determinar se um jogo tem uma certa linguagem e nessa linguagem tem texto
					CBloom l_textBloom = new CBloom(jogos.size(), 0.1); // bloom 
					l_textBloom.initialize();
					linguagens_text.put(lang.getName(), l_textBloom);
					//Bloom para determinar se um jogo tem uma certa linguagem e nessa linguagem tem audio
					CBloom l_audioBloom = new CBloom(jogos.size(), 0.1); // bloom 
					l_audioBloom.initialize();
					linguagens_audio.put(lang.getName(), l_audioBloom);
				}
				else if(!lang.getName().isEmpty()) {
				CBloom l = linguagens.get(lang.getName());
				l.insertEle(jogo.getName());
				if(lang.getText()) {
				CBloom l_textBloom = linguagens_text.get(lang.getName());
				l_textBloom.insertEle(jogo.getName());
				}
				if(lang.getAudio()) {
				CBloom l_audioBloom = linguagens_audio.get(lang.getName());
				l_audioBloom.insertEle(jogo.getName());
					}
				}
			}
		}	
		return languages;
		
	}
	
	public static void goback() throws java.lang.InterruptedException {
		Scanner in = new Scanner ( System.in );
		System.out.println("Go back to the main menu? (y/n)");
		String goback=in.next();
		while(!goback.equals("y")&&!goback.equals("n")) {
			System.out.printf("Insert correct answer:");
			goback=in.next();
		}
		if(goback.equals("y")) {
			display_menu();
		}
		else if(goback.equals("n"))
			System.exit(0);
	}
	
	//Opção 1
	public static ArrayList<Game> similar_games_all(Game g) {
		ArrayList<Game> jogoselect = new ArrayList<Game>();
		MinHash lol = new MinHash(jogos); //min hash para comparar jogos 
		int i=1;
		Map<Game, Double> games = lol.printSimilars_j_all(50, g); // Similariedade, quanto maior mais similares são. Entre 0 e 1
		if(games.isEmpty()) {
			System.out.println("Nenhum jogo encontrado!\n");	
			
		}else {
		found=true;
		for(Game j:games.keySet()) {
			System.out.printf("%d. %s - Similarity: %.2f%%\n",i,j.getName(),games.get(j));
			jogoselect.add(j);
			i++;
		}
		}
		return jogoselect;
	}

	public static ArrayList<Game> similar_games_name(String name) {
		
		ArrayList<Game> jogoselect = new ArrayList<Game>();
		MinHash lol = new MinHash(2, jogos);
		int i=1;
		Map<Game, Double> games = lol.printSimilars_j(30, name); // Similariedade, quanto maior mais similares são. Entre 0 e 1
		if(games.isEmpty()) {
			System.out.println("Nenhum jogo encontrado!\n");	
			
		}else {
		found=true;
		for(Game g:games.keySet()) {
			System.out.printf("%d. %s - Similarity: %.2f%%\n",i,g.getName(),games.get(g));
			jogoselect.add(g);
			i++;
		}
		}
		return jogoselect;
	}
	
	public static ArrayList<String> similar_publisher(String name, ArrayList<String> publishers) {
		ArrayList<String> publisherselect = new ArrayList<String>();
		MinHash lol = new MinHash(2, false, publishers);
		int i=1;
		Map<String, Double> games = lol.printSimilars_string(30, name); // Similariedade, quanto maior mais similares são. Entre 0 e 1
		if(games.isEmpty()) {
			System.out.println("Nenhum publisher encontrado!\n");	
			
		}else {
		found=true;
		for(String g: games.keySet()) {
			System.out.printf("%d. %s - Similarity: %.2f%%\n",i,g,games.get(g));
			publisherselect.add(g);
			i++;
		}
		}
		return publisherselect;
	}
	
	public static ArrayList<String> similar_developer(String name, ArrayList<String> developers) {
		ArrayList<String> developerselect = new ArrayList<String>();
		MinHash lol = new MinHash(2, false, developers);
		int i=1;
		Map<String, Double> games = lol.printSimilars_string(30, name); // Similariedade, quanto maior mais similares são. Entre 0 e 1
		if(games.isEmpty()) {
			System.out.println("Nenhum developer encontrado!\n");	
			
		}else {
		found=true;
		for(String g: games.keySet()) {
			System.out.printf("%d. %s - Similarity: %.2f%%\n",i,g,games.get(g));
			developerselect.add(g);
			i++;
		}
		}
		return developerselect;
	}
	
	public static void showgamestats(Game jogo) throws java.lang.InterruptedException {
    System.out.println ( "1) Information about game Reviews\n2) Information about game Languages\n3) See all info about the game");
    System.out.print ( "Selection: " );
	Scanner in = new Scanner ( System.in );

	switch (in.nextInt()) {
		case 1:
			if(reviewsBloom.isEle(jogo.getName())) {
				System.out.println("O "+jogo.getName()+" tem aproximadamente "+reviewsBloom.numEle(jogo.getName())+" reviews \n");
				System.out.println ( "1) See spam reviews\n2) See similar reviews without spam\n3) Insert a phrase to see similar reviews\n4) See users with spam reviews in this game");
				MinHash simRevs=new MinHash(jogo.getReviews(), 3);
				HashSet<Review> spamRevs;
				spamRevs=simRevs.removeSpam();
				switch (in.nextInt()) {
				case 1:
					System.out.println("Lista de Reviews Spam:");
					int i=0;
					for (Review rev:spamRevs) {
						i++;
						System.out.printf("%d) %s\n",(i+1), rev.getReview());
					}
					System.out.println();
					goback();
					break;
				case 2:
					System.out.printf("Grau de Similaridade: ");
					double grau=in.nextDouble();
					simRevs.printSimilarsNoSpam(grau, spamRevs);
					System.out.println();
					goback();
					break;
				case 3:
					System.out.printf("Grau de Similaridade: ");
					double grau1=in.nextDouble();
					System.out.printf("Conteúdo da Review a Pesquisar: ");
					in.nextLine();
					String revCont=in.nextLine();
					HashMap<Review, Double> similarRevs= simRevs.findSimilars(revCont, grau1);
					int index=0;
					for (Review rev: similarRevs.keySet()) {
						index++;
						System.out.printf("%d) Similaridade: %.2f%%\nReview: %s\n",(index+1),similarRevs.get(rev),rev.getReview());
					}
					System.out.println();
					goback();
					break;
				case 4:
					System.out.println("Lista de Spammers:");
					int ind=0;
					for (Review rev:spamRevs) {
						ind++;
						System.out.printf("%d) %s\n", (ind+1), rev.getUser());
					}
					System.out.println();
					goback();
					break;
				default:
					System.err.println ( "Unrecognized option" );
					goback();
				}
				
				
			}else {
				System.out.println("O "+jogo.getName()+" não tem reviews\n");
			}
		    
			in.close();
			goback();
			
			
			
			
			

		case 2:
			System.out.println("Select Language for Info:");
			TimeUnit.SECONDS.sleep(1);
			ArrayList<String> languages = language();
			int i=1;
			for (String Lang: languages) {
				System.out.println(i+"."+Lang);
				i++;
			}
			System.out.println("Selection:");
			int langselect=in.nextInt();
			String key = languages.get(langselect-1);
			if(linguagens.get(key).isEle(jogo.getName())) {
				System.out.printf("O jogo "+jogo.getName()+" tem a linguagem "+key);
				if(linguagens_text.get(key).isEle(jogo.getName()) && linguagens_audio.get(key).isEle(jogo.getName())) {
					System.out.printf(", tanto em texto como em audio!\n");
				}
				else if(linguagens_text.get(key).isEle(jogo.getName())) {
					System.out.printf(" apenas em texto!\n");
				}
				else if(linguagens_audio.get(key).isEle(jogo.getName())) {
					System.out.printf(" apenas em audio!\n");
				}
			}
				
			else
				System.out.println("O jogo "+jogo.getName()+" não tem a linguagem "+key);

			goback();
	
	
case 3:
	System.out.println("Info about the game "+jogo.getName()+":");
	System.out.println("Genres:");
	for(String genre:jogo.getGenres()) {
		System.out.println(genre);
	}
	System.out.println();
	System.out.println("Languages:");
	for(Language lang:jogo.getLanguages()) {
		System.out.println(lang.getName());
	}
	System.out.println();
	System.out.println("Price: "+jogo.getPrice());
	System.out.println("Rating: "+jogo.getRating());
	System.out.println("Publisher: "+jogo.getPublisher());
	System.out.println("Developer: "+jogo.getDeveloper());
	System.out.println("Reviews:");
	if(reviewsBloom.isEle(jogo.getName())) {
		System.out.println("O jogo tem "+reviewsBloom.numEle(jogo.getName())+" reviews.");
}	else {
	System.out.println("O jogo não tem reviews.");
}
	goback();
	}	
}
	
	public static void showreviewsinfo() throws java.lang.InterruptedException {
	    System.out.println ( "1) Show full list of Spammers\n2) Search by a phrase all similar reviews of all games\n");
	    System.out.print ( "Selection: " );
		Scanner in = new Scanner ( System.in );
		ArrayList<Review> allRevs = new ArrayList<>();
		for (Game jogo: jogos) {
			allRevs.addAll(jogo.getReviews());
		}
		MinHashLSH allRevsMin = new MinHashLSH(allRevs, 5, "RevSignatures.txt");

		switch (in.nextInt()) {
			case 1:
				HashSet<Review> spamRevs;
				spamRevs=allRevsMin.removeSpamLSH();
				System.out.println("Lista de Spammers:");
				int i=0;
				for (Review rev : spamRevs) {
					i++;
					System.out.printf("%d) %s\n",(i+1), rev.getUser());
				}
				System.out.println();
				goback();
				break;
			case 2:
				System.out.printf("Grau de Similaridade: ");
				double grau1=in.nextDouble();
				System.out.printf("Conteúdo da Review a Pesquisar: ");
				in.nextLine();
				String revCont=in.nextLine();
				HashMap<Review,Double> similarRevs= allRevsMin.findSimilarsLSH(revCont, grau1);
				int index=0;
				for (Review rev: similarRevs.keySet()) {
					index++;
					System.out.printf("%d) Similaridade: %.2f%%\nUtilizador: %s\nReview: %s\n",(index+1),similarRevs.get(rev),rev.getUser(),similarRevs.get(rev),rev.getReview());
				}
				System.out.println();
				
				goback();
				break;
			default:
				System.err.println ( "Unrecognized option" );
				goback();
			}
		}	
		
	
	public static void main(String[] args) throws java.lang.InterruptedException {
		ReadJson();
		display_menu();
		
	}
	
	public static Game selectjogo() {
		ArrayList<Game> jogoselect = null;
		String name;
		Scanner j = new Scanner ( System.in );
		while(found!=true) {
	        System.out.printf( "Insert a game: " );
	       
	        name = j.nextLine();
	        System.out.println();
	        jogoselect = similar_games_name(name);
	        }
		found=false;
        System.out.printf( "\nSelect the game (by number): ");
        int select = j.nextInt();
        return jogoselect.get(select-1);
	}
	
	public static String selectpublisher() {
		ArrayList<String> publisherselect = new ArrayList<>();
		ArrayList<String> publishers;
		String publisher;
		Scanner j = new Scanner ( System.in );
		while(found!=true) {
	        System.out.printf( "Insert a publisher: " );
	       
	        publisher = j.nextLine();
	        System.out.println();
	        publishers=ReadJSON.getPublishers();
	        publisherselect = similar_publisher(publisher,publishers);
	        }
		found=false;
        System.out.printf( "\nSelect the publisher (by number): ");
        int select = j.nextInt();
        return publisherselect.get(select-1);
		}
	
	public static String selectdeveloper() {
		ArrayList<String> developerselect = new ArrayList<>();
		ArrayList<String> developers;
		String developer;
		Scanner j = new Scanner ( System.in );
		while(found!=true) {
	        System.out.printf( "Insert a developer: " );
	       
	        developer = j.nextLine();
	        System.out.println();
	        developers=ReadJSON.getDevelopers();
	        developerselect = similar_developer(developer,developers);
	        }
		found=false;
        System.out.printf( "\nSelect the developer (by number): ");
        int select = j.nextInt();
        return developerselect.get(select-1);
		}
	
	
	
	public static void showpublishertats(String select) throws java.lang.InterruptedException {
	    System.out.println ( "1) Check if a game is owned by the publisher \n");
	    System.out.print ( "Selection: " );
		Scanner in = new Scanner ( System.in );
		switch (in.nextInt()) {
			case 1:
				Game selection=selectjogo();
				if(publishers.get(select).isEle(selection.getName())) 
					System.out.println(select+" possui o jogo "+selection.getName());
				else
					System.out.println(select+" não possui o jogo "+selection.getName());
				goback();
				break;
			default:
				System.err.println ( "Unrecognized option" );
				break;
		  
					}
	}
	
	public static void showdeveloperstats(String select) throws java.lang.InterruptedException {
	    System.out.println ( "1) Check if a game is made by the developer \n");
	    System.out.print ( "Selection: " );
		Scanner in = new Scanner ( System.in );
		switch (in.nextInt()) {
			case 1:
				Game selection=selectjogo();
				if(developers.get(select).isEle(selection.getName())) 
					System.out.println(select+" fez o jogo "+selection.getName());
				else
					System.out.println(select+" não fez o jogo "+selection.getName());
				goback();				
				break;
			default:
				System.err.println ( "Unrecognized option" );
				break;
		  
					}
	}

 static void display_menu() throws java.lang.InterruptedException {
	 System.out.print("Welcome to GOG Database!\n");
	 TimeUnit.SECONDS.sleep(1);
	    System.out.println ( "1) Search by game and get info\n2) Search Similar games\n3) Publisher Database\n4) Developer Database\n5) Reviews Information\n6) See our program data\n7) Test our program\n"
	    		+ "" );
	    System.out.print ( "Selection: " );
	    
	    Scanner in = new Scanner ( System.in );
	    found=false;
	    
	    Game selection;
	    String select;
	    
		switch (in.nextInt()) {
			case 1:
				selection=selectjogo();
		        showgamestats(selection);
		        
		        
		        break;
			case 2:
				selection=selectjogo();
				similar_games_all(selection);
				break;
			case 3:
				select=selectpublisher();
				showpublishertats(select);
	
				break;
			case 4:
				select=selectdeveloper();
				showdeveloperstats(select);
				
				break;
			case 5:
				showreviewsinfo();
		        break;
			case 6:
				System.out.println("Our dataset contains "+jogos.size()+" games.");
				System.out.println("There are "+numRev+" games with reviews and "+(jogos.size()-numRev)+" without reviews.");		
		        break;
			case 7:
				System.out.println ( "You picked option TESTES" );
		        teste();
				break;
			default:
				System.err.println ( "Unrecognized option" );
				break;
	    }
		in.close();
	 }

	private static void ReadJson() {
		json = new ReadJSON();
		json.read();
		jogos = json.getJogos();
		reviewsBloom = new CBloom(jogos.size(), 0.1); // bloom to find out the games with reviews and how many
		reviewsBloom.initialize();	
		noReviewsBloom = new CBloom(jogos.size(), 0.1); // bloom 
		noReviewsBloom.initialize();
		numNoRev=0;
		numRev=0;
		ArrayList<String> publisher = new ArrayList<>();
		ArrayList<String> developer = new ArrayList<>();
		for (int i=0;i<jogos.size();i++) {
			
        	ArrayList<Review> reviews = jogos.get(i).getReviews();
        	if (reviews.size() == 0) {
        		numNoRev++;
            	noReviewsBloom.insertEle(jogos.get(i).getName());
        	} else {
        		numRev++;
        		for (int j=0;j<reviews.size();j++) {
	            	reviewsBloom.insertEle(jogos.get(i).getName());
        		}
        	}
        	
        	
        	
        	//Developer and publisher bloom
    		
    		
    		if(!publisher.contains(jogos.get(i).getPublisher()) && !jogos.get(i).getPublisher().isEmpty()) {
    			publisher.add(jogos.get(i).getPublisher());
    			//Bloom para determinar se um jogo tem um certo publisher 
    			CBloom p = new CBloom(jogos.size(), 0.1); // bloom
    			p.initialize();
    			publishers.put(jogos.get(i).getPublisher(), p);
    			
    		}
    		else if(!jogos.get(i).getPublisher().isEmpty()) {
    		CBloom p = publishers.get(jogos.get(i).getPublisher());
    		p.insertEle(jogos.get(i).getName());
    		}	
    		
    		if(!developer.contains(jogos.get(i).getDeveloper()) && !jogos.get(i).getDeveloper().isEmpty()) {
    			developer.add(jogos.get(i).getDeveloper());
    			//Bloom para determinar se um jogo tem um certo dev 
    			CBloom d = new CBloom(jogos.size(), 0.1); // bloom
    			d.initialize();
    			developers.put(jogos.get(i).getDeveloper(), d);
    			
    		}
    		else if(!jogos.get(i).getDeveloper().isEmpty()) {
    		CBloom d = developers.get(jogos.get(i).getDeveloper());
    		d.insertEle(jogos.get(i).getName());
    		}	

		}
	
	}
	
	public void InterruptedException() {
		System.err.println("Error! Restart the program!");
	}
}