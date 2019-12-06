package mpei_p1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Tests {
	private ArrayList<Game> jogos;
	
	public Tests(ArrayList<Game> jogos) {
		this.jogos=jogos;
	}
	
	public void bloomTests() throws InterruptedException {
		System.out.println("------------------Testes ao BLOOM------------------");
		 System.out.println("\n1) Inicializar e Inserir Strings Randoms ao Bloom...\n");

		    // Criaçao do Bloom
		    int maxEle=10000;
		    CBloom testeBloom = new CBloom(maxEle, 0.01);
		    
		    // Inserir strings ao bloom

		    int targetStringLength = 100;
		    char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		    	    
		    Random rand = new Random();
	        List<String> strings = new ArrayList<>();
	        List<String> strings2 = new ArrayList<>();
	        
	        for(int i = 0; i < maxEle; i++) {
	            String randString = "";
	            for(int j = 0; j < targetStringLength; j++) {
	                randString += Character.toString(alphabet[rand.nextInt(alphabet.length-1)]);
	            }
	            strings.add(randString);
	            testeBloom.insertEle(randString);
	        }
	        
			System.out.println("2) Criar Outro Conjunto de Strings...\n");

	        for(int i = 0; i < maxEle; i++) {
	            String randString2 = "";
	            for(int j = 0; j < targetStringLength; j++) {
	                randString2 += Character.toString(alphabet[rand.nextInt(alphabet.length-1)]);
	            }
	            strings2.add(randString2);
	        }
			 System.out.println("3) Verificar se as strings do segunto conjunto pertencem ao bloom...\n");
	        double falsePos = 0;
	        for(String s : strings2) {
	            if(testeBloom.isEle(s)==true) {
	                falsePos++;
	            }
	        }        
			System.out.println("\tDados Obtidos do Bloom:");
		    double pTeor=Math.pow(1-Math.pow(1-(double)1/testeBloom.getN(), testeBloom.getK()*testeBloom.getM()), testeBloom.getK());
		    System.out.printf("Número de falsos positivos: %.0f\n", falsePos);
			System.out.println("Probabilidade de falsos positivos: " + (falsePos)/(maxEle));
			System.out.println("Probabilidade teórica de falsos positivos: " + pTeor);		    
			
			TimeUnit.SECONDS.sleep(1);
			System.out.println();
			System.out.println("------------------BLOOM COM NOSSO DATASET------------------");
		    long startTime = System.nanoTime();
			CBloom reviewsBloom = new CBloom(jogos.size(), 0.1);
			long stopTime = System.nanoTime();
			long elapsedTime = stopTime-startTime;
			double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
			System.out.println("\nTime elapsed to create bloom (s): " + elapsedTimeInSecond+" seconds.");
			
			System.out.println("\n\tDados do DataSet:");

			double numIns=0;
			for (int i=0;i<jogos.size();i++) {
	        	ArrayList<Review> reviews = jogos.get(i).getReviews();
	        	if (jogos.get(i).getName().equals("Overlord + Raising Hell")) {
	            	System.out.println("Número de reviews de Overlord Raising Hell: "+reviews.size());
	        	}
	        	if (jogos.get(i).getName().equals("Galactic Civilizations III - Revenge of the Snathi DLC")) {
	    			System.out.println("Número de reviews reais de Galactic Civilizations: "+reviews.size());
	    		}
	        	if (jogos.get(i).getName().equals("Enclave")) {
	    			System.out.println("Número de reviews reais de Enclave: "+reviews.size());
	    		}

	        	if (reviews.size() > 0) {
	        		for (int j=0;j<reviews.size();j++) {
		            	reviewsBloom.insertEle(jogos.get(i).getName());
	        		}
	        		numIns++;
	        	}
			}
			
			TimeUnit.SECONDS.sleep(1);
			System.out.println("\n\tDados do Bloom:");
			System.out.println("Enclave tem reviews? "+reviewsBloom.isEle("Enclave")); // tem de dar true
			System.out.println("Galactic Civilizations tem reviews? "+reviewsBloom.isEle("Galactic Civilizations III - Revenge of the Snathi DLC")); // tem de dar false
			System.out.println("Overlord Raising Hell tem reviews? "+ reviewsBloom.isEle("Overlord + Raising Hell")); // tem de dar true

			
			System.out.println();
			TimeUnit.SECONDS.sleep(1);
			System.out.println("Enclave tem quantas reviews? "+reviewsBloom.numEle("Enclave")); // tem de dar 68
			System.out.println("Galactic Civilizations tem quantas reviews? "+reviewsBloom.numEle("Galactic Civilizations III - Revenge of the Snathi DLC")); // tem de dar 0
			System.out.println("Overlord Raising Hell tem quantas reviews? "+reviewsBloom.numEle("Overlord + Raising Hell")); // tem de dar 133

			
			System.out.println("\n\tEliminar o jogo Enclave do Bloom:");
			reviewsBloom.deleteEle("Enclave");
			System.out.println("Enclave tem reviews? "+reviewsBloom.isEle("Enclave"));

			
			double pos=0;
			for (int i=0;i<jogos.size();i++) {
				if (reviewsBloom.isEle(jogos.get(i).getName())) {
					pos++;
				}
			}
			double pfp=Math.pow(1-Math.pow(1-(double)1/reviewsBloom.getN(), reviewsBloom.getK()*reviewsBloom.getM()), reviewsBloom.getK());
			
			System.out.println("\n\tTeste de Falsos Positivos:");
			System.out.println("Numero de elementos inseridos no bloom (sem contar com o jogo eliminado): " + (numIns-1));
			System.out.println("Numero de elementos que estão no bloom: " + pos);
			System.out.println("Numero de colisoes: "+ (pos-numIns-1));
			System.out.println("Probabilidade de falsos positivos: " + (pos-numIns-1)/(numIns-1));
			System.out.println("Probabilidade teórica de falsos positivos: " + pfp);
	}
}
