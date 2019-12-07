package mpei_p1;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
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
	
	public static ArrayList<String> language() {	    
		//Bloom para determinar se um jogo tem uma certa linguagem 
			   
		ArrayList<String> languages = new ArrayList<>();
		for(Game jogo:jogos) {
			for(Language lang:jogo.getLanguages()) {
				if(!languages.contains(lang.getName()) && !lang.getName().isEmpty()) {
					languages.add(lang.getName());
					//Bloom para determinar se um jogo tem uma certa linguagem 
					CBloom l = new CBloom(jogos.size(), 0.1); // bloom
					linguagens.put(lang.getName(), l);
					//Bloom para determinar se um jogo tem uma certa linguagem e nessa linguagem tem texto
					CBloom l_textBloom = new CBloom(jogos.size(), 0.1); // bloom 
					linguagens_text.put(lang.getName(), l_textBloom);
					//Bloom para determinar se um jogo tem uma certa linguagem e nessa linguagem tem audio
					CBloom l_audioBloom = new CBloom(jogos.size(), 0.1); // bloom 
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
		System.out.println("Voltar para o menu principal? (s/n)");
		String goback=in.next();
		while(!goback.equals("s")&&!goback.equals("n")) {
			System.out.printf("Insira a resposta correta:");
			goback=in.next();
		}
		if(goback.equals("s")) {
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
			System.out.printf("%d. %s - Similaridade: %.2f%%\n",i,j.getName(),games.get(j));
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
			System.out.printf("%d. %s - Similaridade: %.2f%%\n",i,g.getName(),games.get(g));
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
			System.out.println("Nenhuma editora encontrada!\n");	
			
		}else {
		found=true;
		for(String g: games.keySet()) {
			System.out.printf("%d. %s - Similaridade: %.2f%%\n",i,g,games.get(g));
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
			System.out.printf("%d. %s - Similaridade: %.2f%%\n",i,g,games.get(g));
			developerselect.add(g);
			i++;
		}
		}
		return developerselect;
	}
	
	public static void showgamestats(Game jogo) throws java.lang.InterruptedException {
    System.out.println ( "1) Informação sobre as Reviews \n2) Informação sobre as Linguagens\n3) Ver todas as informações");
    System.out.print ( "Selection: " );
	Scanner in = new Scanner ( System.in );

	switch (in.nextInt()) {
		case 1:
			if(reviewsBloom.isEle(jogo.getName())) {
				System.out.println("O "+jogo.getName()+" tem aproximadamente "+reviewsBloom.numEle(jogo.getName())+" reviews \n");
				System.out.println ( "1) Ver reviews consideradas como spam\n2) Ver reviews similares sem spam\n3) Inserir uma frase para ver reviews similares\n4) Ver utilizadores com reviews consideradas como spam");
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
					System.err.println ( "Opção não reconhecida." );
					goback();
				}
				
				
			}else {
				System.out.println("O "+jogo.getName()+" não tem reviews\n");
			}
		    
			in.close();
			goback();
			
			
			
			
			

		case 2:
			System.out.println("Selecione a Linguagem para obter informação:");
			TimeUnit.SECONDS.sleep(1);
			ArrayList<String> languages = language();
			int i=1;
			for (String Lang: languages) {
				System.out.println(i+"."+Lang);
				i++;
			}
			System.out.println("Selecione (pelo número): ");
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
	System.out.println("Informação do jogo "+jogo.getName()+":\n");
	System.out.println("Géneros:");
	for(String genre:jogo.getGenres()) {
		System.out.println(genre);
	}
	System.out.println();
	System.out.println("Linguagens:");
	for(Language lang:jogo.getLanguages()) {
		System.out.println(lang.getName());
	}
	System.out.println();
	System.out.println("Preço: "+jogo.getPrice());
	System.out.println("Classificação: "+jogo.getRating());
	System.out.println("Editora: "+jogo.getPublisher());
	System.out.println("Publicadora: "+jogo.getDeveloper());
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
	    System.out.println ( "1) Mostrar a lista total de Spammers\n2) Procurar por frase todas as reviews parecidas de todos os jogos\n");
	    System.out.print ( "Selecione: " );
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
					System.out.printf("%d) Similaridade: %.2f%%\nUtilizador: %s\nReview: %s\n",(index+1),similarRevs.get(rev),rev.getUser(),rev.getReview());
				}
				System.out.println();
				
				goback();
				break;
			default:
				System.err.println ( "Opção não reconhecida." );
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
	        System.out.printf( "Insira um jogo: " );
	       
	        name = j.nextLine();
	        System.out.println();
	        jogoselect = similar_games_name(name);
	        }
		found=false;
        System.out.printf( "\nSelecione o jogo (pelo número): ");
        int select = j.nextInt();
        return jogoselect.get(select-1);
	}
	
	public static String selectpublisher() {
		ArrayList<String> publisherselect = new ArrayList<>();
		ArrayList<String> publishers;
		String publisher;
		Scanner j = new Scanner ( System.in );
		while(found!=true) {
	        System.out.printf( "Insira uma editora: " );
	       
	        publisher = j.nextLine();
	        System.out.println();
	        publishers=ReadJSON.getPublishers();
	        publisherselect = similar_publisher(publisher,publishers);
	        }
		found=false;
        System.out.printf( "\nSelecione uma editora (pelo número): ");
        int select = j.nextInt();
        return publisherselect.get(select-1);
		}
	
	public static String selectdeveloper() {
		ArrayList<String> developerselect = new ArrayList<>();
		ArrayList<String> developers;
		String developer;
		Scanner j = new Scanner ( System.in );
		while(found!=true) {
	        System.out.printf( "Insira uma desenvolvedora: " );
	       
	        developer = j.nextLine();
	        System.out.println();
	        developers=ReadJSON.getDevelopers();
	        developerselect = similar_developer(developer,developers);
	        }
		found=false;
        System.out.printf( "\nSelecione uma desenvolvedora (pelo número): ");
        int select = j.nextInt();
        return developerselect.get(select-1);
		}
	
	
	
	public static void showpublishertats(String select) throws java.lang.InterruptedException {
		System.out.println("Verifique se a editora é dona de um jogo");
		Game selection=selectjogo();
		if(publishers.get(select).isEle(selection.getName())) 
			System.out.println(select+" possui o jogo "+selection.getName());
		else
			System.out.println(select+" não possui o jogo "+selection.getName());
		goback();
				
	}
	
	public static void showdeveloperstats(String select) throws java.lang.InterruptedException {
	    System.out.println( "Verifique se desenvolvedora fez um jogo " );

		Game selection=selectjogo();
		if(developers.get(select).isEle(selection.getName())) 
			System.out.println(select+" fez o jogo "+selection.getName());
		else
			System.out.println(select+" não fez o jogo "+selection.getName());
		goback();
	}

 static void display_menu() throws java.lang.InterruptedException {
	 System.out.print("Bem-vindo à base de dados do GOG!\n");
	 TimeUnit.SECONDS.sleep(1);
	    System.out.println ( "1) Pesquisa de um jogo para obter informações\n2) Pesquisa de jogos similares\n3) Pesquisa de uma editora para obter informação\n4) Pesquisa de uma desenvolvedora para obter informação\n5) Informações relativas a todas as reviews\n6) Testes aos módulos" );
	    System.out.printf( "Opção:" );
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
				goback();
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
				Tests test = new Tests(jogos);
			    System.out.println ( "1) Testes ao Counting Bloom Filter\n2) Testes ao MinHash e MinHashLSH\n");
		        Scanner in2 = new Scanner ( System.in );
		        switch(in2.nextInt()) {
		        case 1:
		        	test.bloomTests();
		        	break;
		        case 2:
		        	test.minTests();
		        	break;
		        default:
					System.err.println ( "Opção não reconhecida." );
					break;
		        }
		        goback();
				break;
			default:
				System.err.println ( "Opção não reconhecida." );
				break;
	    }
		in.close();
	 }

	private static void ReadJson() {
		json = new ReadJSON();
		json.read();
		jogos = json.getJogos();
		reviewsBloom = new CBloom(jogos.size(), 0.1); // bloom to find out the games with reviews and how many
		noReviewsBloom = new CBloom(jogos.size(), 0.1); // bloom 
		ArrayList<String> publisher = new ArrayList<>();
		ArrayList<String> developer = new ArrayList<>();
		for (int i=0;i<jogos.size();i++) {
			
        	ArrayList<Review> reviews = jogos.get(i).getReviews();
        	if (reviews.size() == 0) {
            	noReviewsBloom.insertEle(jogos.get(i).getName());
        	} else {
        		for (int j=0;j<reviews.size();j++) {
	            	reviewsBloom.insertEle(jogos.get(i).getName());
        		}
        	}
        	
        	
        	
        	//Developer and publisher bloom
    		
    		
    		if(!publisher.contains(jogos.get(i).getPublisher()) && !jogos.get(i).getPublisher().isEmpty()) {
    			publisher.add(jogos.get(i).getPublisher());
    			//Bloom para determinar se um jogo tem um certo publisher 
    			CBloom p = new CBloom(jogos.size(), 0.1); // bloom
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
    			developers.put(jogos.get(i).getDeveloper(), d);
    			
    		}
    		else if(!jogos.get(i).getDeveloper().isEmpty()) {
    		CBloom d = developers.get(jogos.get(i).getDeveloper());
    		d.insertEle(jogos.get(i).getName());
    		}	

		}
	
	}
	
	public void InterruptedException() {
		System.err.println("Erro! Reinicie o programa!");
	}
}