import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * A program fő osztálya, fájlok olvasása, írása
 * @author gsanta
 *
 */
public class Main {
	
	
	public static void main(String[] args) throws Exception {
		Statistic statistic = new Statistic();
		
		String trainingFile = "train.txt";//args[0];
		String testFile = "dev.txt";//args[1];
		
		/**
		 * train fajl olvasasa, szavakra bontas, statistic feltoltese
		 */
		
		/**
		 * input fajl, ami a train-t olvassa
		 */
		BufferedReader br = new BufferedReader(new FileReader(trainingFile));
		
		/**
		 * A train fajl beolvasott sorait fogja tartalmazni
		 */
		String line;
		
		/**
		 * A szavakat tartalmazza a sorban
		 */
		String[] split;
		
		/**
		 * pozitiv-e a komment (pos: true, neg: false)
		 */
		boolean positive = true;
		
		/**
		 * soronkenti beolvasas
		 */
		while ((line = br.readLine()) != null) {
			/**
			 * tab-nal vagja el
			 */
			split = line.split("\\t");
			
			/**
			 * sorszam es josag-ot tartalmazo sor
			 * itt beallitodik a positive valtozo attol fuggoen hogy pos vagy neg
			 * es ezutan a kommentben levo szavak pos/negCount erteke ez alapjan fog noni
			 */
			if (split.length == 2) {
				//System.out.println(split[0] + "\tPOS");
				if (split[1].equals("POS")) {
					positive = true;
				} else {
					positive = false;
				}
				
				/**
				 * a statistic tarolja, hany pos illetve neg komment van (kesobb ez alapjan
				 * fogja "normalizalni" az aranyokat)
				 */
				statistic.increaseCommentCount(positive);
			} else {
				/**
				 * space-eknel vag
				 */
				split = line.split("\\s+");
				
				/**
				 * szavakon vegigfut, statisticba szot belerak es hogy pos, vagy neg-e 
				 */
				for (int i = 0; i < split.length; i++) {
					/**
					 * kis, nagybetu nem szamit
					 */
					String word = split[i].toLowerCase();
					
					statistic.addWord(word, positive);
				}
				
				/**
				 * Az egymas utani ket szot is belerakja, mintha egy szo lenne
				 * a nagyobb pontossag erdekeben
				 */
				if(split.length > 1) {
					for (int i = 0; i < split.length - 1; i++) {
						String word = split[i].toLowerCase() + " " + split[i + 1].toLowerCase(); 
						
						statistic.addWord(word, positive);
					}
				}
			}
		}
		
		
		
		br.close();
		
		/**
		 * miutan beolvasta a traint, valoszinuseget szamol a szavakra
		 */
		statistic.countProbForWords();
		
//		System.out.println("Training done!");
//		System.out.println("Pos: " + statistic.posCommentCount + ", Neg: " + statistic.negCommentCount);
//		System.out.println("Osszes szavak szama: " + statistic.words.keySet().size());

		/**
		 * A tesztfajl olvasasa
		 */
		br = new BufferedReader(new FileReader(testFile));
		Comment comment = null;
				
		/**
		 * teszt adatbázis olvasása, statistic feltöltése comment-ekkel
		 */
		while ((line = br.readLine()) != null) {
			/**
			 * ha tab van uj komment kezdete
			 */
			split = line.split("\\t");
			if (split.length == 2) {
				
				/**
				 * komment hozzaadasa a statistichez
				 */
				if(comment != null) {
					if(comment.setPrev == false) {
						statistic.addComment(comment);
					} else {
						//System.out.println("withouth");
						statistic.addWithoutCountProb(comment);
					}
				}
				
				
				comment = new Comment();
				/**
				 * csak a dev-nel van ertelme, a comment igazi pos vagy neg erteke
				 */
				if (split[1].equals("POS")) {
					comment.original = true;
				} else {
					comment.original = false;
				}
				
				comment.code = split[0];

			} else {
				split = line.split("\\s");
				boolean next = true;
				
				/**
				 * Szavak bevétele
				 */
				for (int i = 0; i < split.length; i++) {
					
					Word word;
					String stringWord = split[i].toLowerCase();

					// If the String is in our HashMap get the word out
					/**
					 * ha a szo benne van a tanuloadatbazisban, akkor kivesszuk, belerak a kommentbe
					 */
					if ((word = statistic.getWord(stringWord)) != null) {
						comment.addWord(word);
					} else {
						/**
						 * Ha a tanuloban nincs benne, belevesszuk a "newWords"-be, hogy kozben is tanuljon
						 * Ezt nem lehet keverni a mar tanuloban levokkel, mert ezekre allandoan valszint kell
						 * szamolni
						 */
						
						if((word = statistic.newWords.get(stringWord)) != null) {
							comment.addWord(word);
						}
						comment.addNewWord(stringWord);
					}
				}
				
				/**
				 * két egymás után levő szavak bevétele
				 */
				if(split.length > 1) {
					for (int i = 0; i < split.length - 1; i++) {
						String stringWord = split[i].toLowerCase() + " " + split[i + 1].toLowerCase(); 
						
						Word word;

						// If the String is in our HashMap get the word out
						if ((word = statistic.getWord(stringWord)) != null) {
							comment.addWord(word);
						}
						
					}
				}

			}

		}
		
		if(comment != null) {
			statistic.addComment(comment);
		}
		
		
		/**
		 * csak debugra
		 */
		System.out.println("Eles kommentek szama: " + statistic.comments.size());
		System.out.println("Program vege!");
		
		int helyes = 0;
		int hibas = 0;
		int neg = 0;
		for(Comment c: statistic.comments) {
			if(c.original == c.result) {
				helyes += 1;
			} else {
				hibas += 1;
				
			}
			
			if(c.result == false) {
				neg++;
			}
		}
		
		double arany = (double) helyes / (double) (hibas + helyes);
		System.out.println("Helyes: " + helyes + ", Hibas: " + hibas + ", Helyes arany: " + arany);
		System.out.println("neg: " + neg);
		
//		System.out.println("csak a 0.55 feletti es a 0.45 alattiakat beveve:");
//		
//		helyes = 0;
//		hibas = 0;
//		for(Comment c: statistic.comments) {
////			System.out.println("Poz valszin: " + c.posProb + ", eredeti: " + c.original + ", szamolt: " + c.result);
//			if(c.posProb > 0.53 || c.posProb < 0.47) {
//				if(c.original == c.result) {
//					helyes += 1;
//				} else {
//					hibas += 1;
//				}
//			} else {
////				if(c.original == true) {
////					helyes += 1;
////				} else {
////					hibas += 1;
////				}
//			}
//		}
//		
		arany = (double) helyes / (double) (hibas + helyes);
		System.out.println("Helyes: " + helyes + ", Hibas: " + hibas + ", Helyes arany: " + arany);
		
		
		
		br.close();
		br = new BufferedReader(new FileReader(testFile));
		
		/**
		 * A végső eredmény kiírása
		 */
		BufferedWriter bw = new BufferedWriter(new FileWriter("predictions.txt"));
		
		System.out.println("size: " + statistic.comments.size());
		
		int i = 0;
		
		/**
		 * fajlt beolvas, csak a code-ot es a hozza tartozo komment josagat irja ki
		 * (lehetne egyszerubben, nem kell beolvasni fajlt)
		 * 
		 */
		while ((line = br.readLine()) != null) {
			split = line.split("\\t");
			if (split.length == 2) {
				
				
				Comment c = statistic.comments.get(i);
				String newLine = "";
				
				if(c.result == true) {
					newLine = line.replace("?", "POS");
				} else {
					newLine = line.replace("?", "NEG");
				}
				bw.write(newLine + "\n");
				i++;
			} else {
//				if(writeOut) {
//					bw.write(line + "\n");
//				}
			}
			
		}
		
		bw.close();
		br.close();
	}
}

