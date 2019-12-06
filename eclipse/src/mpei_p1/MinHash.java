package mpei_p1;

import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;



public class MinHash {

	private ArrayList<Review> reviews = new ArrayList<Review>();
	private ArrayList<Game> jogos = new ArrayList<Game>();
	private  ArrayList<String> elements;
	private int s_shingle;// tamanho das shingles
	private HashFunction hash_shingle;
	private double[][] similar; // array multidimensional com 100 rows e x colunas (cada coluna representa uma review)
	

	// create matrix of similarities from arraylist
		public MinHash(ArrayList<Review> reviews, int s_shingle) {
			this.reviews = reviews;
			this.s_shingle = s_shingle;
			this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
			for (int i = 0; i < reviews.size(); i++) {
				reviews.get(i).createShingles(s_shingle); // criar shingles de todas as reviews
				reviews.get(i).setminHash_shingles(this.hash_shingle.generateSignatures(reviews.get(i).getShingles())); // gerar assinaturas de todas as reviews
			}
			createMatrix();
		}
		
		// read from a file the matrix of similarities of the given array
				public MinHash(ArrayList<Review> reviews, String fileName) {
					 try {
						 this.reviews = reviews;
						 this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
						 Scanner sc2 = null;
						 Scanner sc3 = null;
						 Scanner sc4= null;
						sc2 = new Scanner( new BufferedReader(new FileReader("RandA.txt")));
						sc3 = new Scanner( new BufferedReader(new FileReader("RandB.txt")));
						sc4 = new Scanner( new BufferedReader(new FileReader("Prime.txt")));
						  int prime;
						  int[] randsA=new int[100];
						  int[] randsB=new int[100];
						  
						  String[] line=sc2.nextLine().trim().split(" ");
						  for (int j=0; j<line.length; j++) {
						       randsA[j] = Integer.parseInt((line[j]));
						  }
						  
						  String[] line2=sc3.nextLine().trim().split(" ");
						  for (int j=0; j<line.length; j++) {
						       randsB[j] = Integer.parseInt((line2[j]));
						  }
						  
						  String line3=sc4.nextLine().trim();
						  prime=Integer.parseInt(line3);
						  this.getHash_shingle().setPrime(prime);
						this.getHash_shingle().setRandA(randsA);
						this.getHash_shingle().setRandB(randsB);
						  Scanner sc = new Scanner( new BufferedReader(new FileReader(fileName)));
						 
						  
					      int rows = 100;
					      int columns = reviews.size();
					      this.similar = new double[rows][columns];
					      while(sc.hasNextLine()) {
					         for (int i=0; i<this.similar.length; i++) {
					            String[] line1 = sc.nextLine().trim().split(" ");
					            for (int j=0; j<line1.length; j++) {
					               this.similar[i][j] = Double.parseDouble((line1[j]));
					            }
					         }
					      }
					      sc2.close();
					      sc3.close();
					      sc4.close();
					      sc.close();
					 } catch (IOException e) {}
				}	
				
				// used to print the matrix of all reviews to a file, because not enought memory to store all shingles in each Review had to create a temporary shingle arrayList
				public MinHash(int s_shingle, ArrayList<Review> reviews, String print) {
						this.reviews = reviews;
						this.s_shingle = s_shingle;
						this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
						for (int i = 0; i < reviews.size(); i++) {
							String string = reviews.get(i).getReview();
							String string_no_space = string.replaceAll("\\s+", "");
							int num_shingles = string_no_space.length() - s_shingle + 1;
							ArrayList<String> shingles = new ArrayList<>();
							for (int j = 0; j < num_shingles; j++) {
								shingles.add(string_no_space.substring(j, j + s_shingle));
							}
							reviews.get(i).setminHash_shingles(this.hash_shingle.generateSignatures(shingles)); // gerar assinaturas de todas as reviews
						}
						createMatrix();
					 try {
						 
						File file = new File ("RevSignatures.txt");
						PrintWriter printWriter = new PrintWriter (file);
						File file2 = new File ("RandA.txt");
						PrintWriter printWriter2 = new PrintWriter (file2);
						File file3 = new File ("RandB.txt");
						PrintWriter printWriter3 = new PrintWriter (file3);
						File file4 = new File ("Prime.txt");
						PrintWriter printWriter4 = new PrintWriter (file4);
						
						for (int i = 0; i < this.getHash_shingle().getRandA().length; i++) {
							printWriter2.printf("%d ", this.getHash_shingle().getRandA()[i]);
						}
						for (int i = 0; i < this.getHash_shingle().getRandA().length; i++) {
							printWriter3.printf("%d ", this.getHash_shingle().getRandB()[i]);
						}
						printWriter4.printf("%d ", this.getHash_shingle().getPrime());
						
						
					    for (int i = 0; i < similar.length; i++) {
					        for (int j = 0; j < similar[i].length; j++) {
					            printWriter.printf("%.0f ",similar[i][j]);
					        }
					        printWriter.printf("\n");
					    }
					    printWriter2.close();
					    printWriter4.close();
					    printWriter3.close();
					    printWriter.close();
					 } catch (IOException e) {}
				}


	public MinHash(int s_shingle, ArrayList<Game> jogos) {
		this.jogos = jogos;
		this.s_shingle = s_shingle;
		this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
		for (int i = 0; i < jogos.size(); i++) {
			jogos.get(i).createShingles(s_shingle); // criar shingles de todas os jogos
			jogos.get(i).setminHash_shingles_name(this.hash_shingle.generateSignatures(jogos.get(i).getShingles_name())); // gerar assinaturas de todas os jogos
		}
		createMatrix_jogo();
	}
	
	//Publishers e developers similares
	//pubdev - false para publishers
	//pubdev - true para developers
	//Não tem diferença na realidade, é para conseguir criar o construtor
	public MinHash(int s_shingle, Boolean pubdev, ArrayList<String> array_pubdev) {
		this.elements=array_pubdev;
		this.s_shingle = s_shingle;
		this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
		ArrayList<String> shingles_a = new ArrayList<>();
		ArrayList<int[]> minhash = new ArrayList<>();
		for (String p: array_pubdev) {
			shingles_a.clear();
			String string=p;
			String string_no_space = string.replaceAll("\\s+", "");
			int num_shingles = string_no_space.length() - s_shingle + 1;
			for (int i = 0; i < num_shingles; i++) {
				shingles_a.add(string_no_space.substring(i, i + s_shingle));
			}	
			minhash.add(this.hash_shingle.generateSignatures(shingles_a));
		}
		this.similar=new double[100][array_pubdev.size()];
		int i=0;
		for (int[] j: minhash) {
			for (int row=0;row<100;row++) {
				this.similar[row][i]=j[row];
			}
			i++;
		}
	}
	
	// Para ver jogos similares
	public MinHash( ArrayList<Game> jogos) {
		this.jogos = jogos;
		this.hash_shingle = new HashFunction(100, Integer.MAX_VALUE);
		for (int i = 0; i < jogos.size(); i++) {
			jogos.get(i).createShingles_all(); // criar shingles de todas as reviews
			jogos.get(i).setMinhash_att(this.hash_shingle.generateSignatures(jogos.get(i).getAttributes())); // gerar assinaturas de todos os jogos
		}
		this.similar=new double[100][jogos.size()];
		for (int i=0; i<this.jogos.size();i++) {
			int[] minHashShingles=jogos.get(i).getMinhash_att();
			for (int row=0;row<100;row++) {
				this.similar[row][i]=minHashShingles[row];
			}
		}
	}
	
	// Criar matrizes depende do que é introduzido na minhash
	private void createMatrix() {
		this.similar=new double[100][reviews.size()];
		for (int i=0; i<this.reviews.size();i++) {
			int[] minHashShingles=reviews.get(i).getminHash_shingles();
			for (int row=0;row<100;row++) {
				this.similar[row][i]=minHashShingles[row];
			}
		}		
	}
	
	private void createMatrix_jogo() {
		this.similar=new double[100][jogos.size()];
		for (int i=0; i<this.jogos.size();i++) {
			int[] minHashShingles=jogos.get(i).getminHash_shingles_name();
			for (int row=0;row<100;row++) {
				this.similar[row][i]=minHashShingles[row];
			}
		}
	}
	
	
	// Print dos similares
	public void printSimilars_r(double limiar) {
		double intersections;
		for (int i=0; i<reviews.size()-1;i++) {
			for (int j=i+1;j<reviews.size();j++) {
				intersections =intersect_r	(i, j) ;
				if (intersections >= limiar) {
					System.out.printf("\nReviews com Grau Similariedade de %f.\n", intersections);
					System.out.printf("\tUtilizador: %s\n\t\tConte�do da review: %s\n\tUtilizador: %s\n\t\tConte�do da review: %s\n",reviews.get(i).getUser(),reviews.get(i).getReview(),reviews.get(j).getUser(),reviews.get(j).getReview());
				}
			}
		}
	}
	
	public void printSimilarsNoSpam(double limiar, HashSet<Review> spam) {
		double inters;
		boolean found=false;
		for (int i=0; i<reviews.size()-1;i++) {
			for (Review rev: spam) {
				if (reviews.get(i).getReview().equals(rev.getReview())) {
					found=true;
					break;
				}
			}
			if (found) {
				found=false;
				continue;
			}
			for (int j=i+1;j<reviews.size();j++) {
				inters =intersect_r(i, j);
				if (inters>= limiar) {
					System.out.printf("\nReviews com Grau Similariedade de %.2f%%.\n",inters);
					System.out.printf("\tUtilizador: %s\n\t\tConte�do da review: %s\n\tUtilizador: %s\n\t\tConte�do da review: %s\n",reviews.get(i).getUser(),reviews.get(i).getReview(),reviews.get(j).getUser(),reviews.get(j).getReview());
				}
			}
		}
	}
	
	public HashSet<Review> removeSpam() {
		double limiar=95;
		double inters;
		
		HashSet<Review> spam= new HashSet<>();
		
		for (int i=0; i<this.reviews.size()-1;i++) {
			for (int j=i+1;j<this.reviews.size();j++) {
				inters =intersect_r(i, j);
				if (inters>= limiar) {
					if (reviews.get(i).getUser().equals(reviews.get(j).getUser())) {
						spam.add(reviews.get(j));
						spam.add(reviews.get(i));
					}
				}
            }
		}
		
		return spam;
	}
	
	
	
	
	public HashMap<Review, Double> findSimilars(String rev, double limiar){
		HashMap<Review,Double> simRevs=new HashMap<>();
		
		String string_no_space = rev.replaceAll("\\s+", "");
		int num_shingles = string_no_space.length() - s_shingle + 1;
		ArrayList<String> shingles = new ArrayList<>();
		for (int j = 0; j < num_shingles; j++) {
			shingles.add(string_no_space.substring(j, j + s_shingle));
		}
		
		int[] sign;
		sign=this.hash_shingle.generateSignatures(shingles);
		
		for (int i=0;i<reviews.size();i++) {
			double inter=0;
	        for (int row = 0; row < 100; row++) {
	            if (similar[row][i] == sign[row]) {
	                inter++;
	            }
	        }
			if (inter>= limiar) {
				simRevs.put(reviews.get(i), inter);
			}
		}
		
		return simRevs;
	}	
	
	public Map<Game, Double> printSimilars_j(double limiar, String name) {
		Map<Game, Double> games = new HashMap<Game, Double>();
		
		double intersections;
		
		//criar shingle para o nome
		ArrayList<String> shingles_name_j = new ArrayList<>();
		String string = name;
		String string_no_space = string.replaceAll("\\s+", "");
		int num_shingles = string_no_space.length() - s_shingle + 1;
		
		for (int i = 0; i < num_shingles; i++) {
			shingles_name_j.add(string_no_space.substring(i, i + s_shingle));
		}
		//criar o array hash para o nome
		int sign[];
		sign=this.hash_shingle.generateSignatures(shingles_name_j);
		
		//Ver interseções e criar 
		for (int i=0; i<jogos.size();i++) {
				intersections =intersect_ele(i, sign) ;
				if (intersections >= limiar) { // 0 a 100
					games.put(jogos.get(i),intersections);
					
				}
			}
		return games;
		}
	
	public Map<String, Double> printSimilars_string(double limiar, String name) {
		Map<String, Double> games = new HashMap<String, Double>();
		
		double intersections;
		
		//criar shingle para o nome
		ArrayList<String> shingles_name_j = new ArrayList<>();
		String string = name;
		String string_no_space = string.replaceAll("\\s+", "");
		int num_shingles = string_no_space.length() - s_shingle + 1;
		
		for (int i = 0; i < num_shingles; i++) {
			shingles_name_j.add(string_no_space.substring(i, i + s_shingle));
		}
		//criar o array hash para o nome
		int sign[];
		sign=this.hash_shingle.generateSignatures(shingles_name_j);
		
		//Ver interseções e criar 
		for (int i=0; i<elements.size();i++) {
				intersections =intersect_ele(i, sign);
				if (intersections >= limiar) { // 0 a 100
					games.put(elements.get(i),intersections);
					
				}
			}
		return games;
		}
	
	public Map<Game, Double> printSimilars_j_all(double limiar, Game jogo) {
		Map<Game, Double> games = new HashMap<Game, Double>();
		int num_shingles;
		double intersections;
		
		//criar shingle para o nome
		ArrayList<String> attributes = new ArrayList<>();
		for(String genre:jogo.getGenres())
			attributes.add(genre);
		
		//shingles de publisher
				num_shingles = jogo.getPublisher().length() - 3 + 1;
				
				for (int i = 0; i < num_shingles; i++) {
					attributes.add(jogo.getPublisher().substring(i, i + 3));
				}
				
				//shingles de developer
				num_shingles = jogo.getDeveloper().length() - 3 + 1;
				
				for (int i = 0; i < num_shingles; i++) {
					attributes.add(jogo.getDeveloper().substring(i, i + 3));
				}

		attributes.add(Double.toString(jogo.getPrice()));
		attributes.add(Double.toString(jogo.getRating()));
		for(Language lang:jogo.getLanguages()) {
			attributes.add(lang.getName());
		}
		//criar o array hash para o nome
		int sign[];
		sign=this.hash_shingle.generateSignatures(attributes);
		
		//Ver interseções e criar 
		for (int i=0; i<jogos.size();i++) {
				intersections =intersect_ele(i, sign) ;
				if (intersections >= limiar) { // 0 a 100
					games.put(jogos.get(i),intersections);
					
				}
			}
		return games;
		}
	
	
	public double intersect_r(int i, int j) {
        double intersect = 0;
        for (int row = 0; row < 100; row++) {
            if (similar[row][i] == similar[row][j]) {
                intersect++;
            }
        }
        return intersect;
    }
	
	public double intersect_ele(int i, int[] sign) {
        double intersect = 0;
        for (int row = 0; row < 100; row++) {
            if (similar[row][i] == sign[row]) {
                intersect++; 
            }
        }
        return intersect;
    }

	public void minHashShingles(Review review) {
		String string = review.getReview();
		String string_no_space = string.replaceAll("\\s+", "");
		int num_shingles = string_no_space.length() - s_shingle + 1;
		
		int[] randA=this.hash_shingle.getRandA();
		int[] randB=this.hash_shingle.getRandB();
		int prime=this.hash_shingle.getPrime();
		int n=this.hash_shingle.getN();
		
		for (int j=0;j<100;j++) {
			int min=Integer.MAX_VALUE;
			for (int i = 0; i < num_shingles; i++) {
				String s=string_no_space.substring(i, i + s_shingle);
				int hash=0;
				for (int l=0;l<s.length();l++) {
					hash+=((randA[j]*(int) s.charAt(l) + randB[j]) % prime) % n;
				}
				if (hash<0) {
					hash+=n;
				}
				int hashCode= (hash % n);
				if (hashCode<min) {
					min=hashCode;
				}
			}
			review.addminHash_shingles(min, j);
		}
	}

	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	public HashFunction getHash_shingle() {
		return hash_shingle;
	}

	public void setHash_shingle(HashFunction hash_shingle) {
		this.hash_shingle = hash_shingle;
	}

	public double[][] getSimilar() {
		return similar;
	}

	public void setSimilar(double[][] similar) {
		this.similar = similar;
	}

	
	
	

	
	


}