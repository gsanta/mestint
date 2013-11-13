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
	
	
	public static void main(String[] args) throws Exception{
		Statistic statistic = new Statistic();
		
		String trainingFile = "train.txt";//args[0];
		String testFile = "test.txt";//args[1];
		
		/**
		 * train fájl olvasása, szavakra bontás, statistic feltöltése
		 */
		BufferedReader br = new BufferedReader(new FileReader(trainingFile));
		
		String line;
		String[] split;
		boolean positive = true;
		
		while ((line = br.readLine()) != null) {
			split = line.split("\\t");
			if (split.length == 2) {
				//System.out.println(split[0] + "\tPOS");
				if (split[1].equals("POS")) {
					positive = true;
				} else {
					positive = false;
				}
				
				statistic.increaseCommentCount(positive);
			} else {
				split = line.split("\\s+");

				for (int i = 0; i < split.length; i++) {
					String word = split[i].toLowerCase();
					
					statistic.addWord(word, positive);
				}
				
				if(split.length > 1) {
					for (int i = 0; i < split.length - 1; i++) {
						String word = split[i].toLowerCase() + " " + split[i + 1].toLowerCase(); 
						
						statistic.addWord(word, positive);
					}
				}
			}
		}
		
		
		
		br.close();
		
		statistic.countProbForWords();
		
//		System.out.println("Training done!");
//		System.out.println("Pos: " + statistic.posCommentCount + ", Neg: " + statistic.negCommentCount);
//		System.out.println("Osszes szavak szama: " + statistic.words.keySet().size());

		br = new BufferedReader(new FileReader(testFile));
		Comment comment = null;
		
		int testCounter = 0;
		
		/**
		 * teszt adatbázis olvasása, statistic feltöltése comment-ekkel
		 */
		while ((line = br.readLine()) != null) {
			split = line.split("\\t");
			if (split.length == 2) {
//				testCounter++;
				
				if(comment != null) {
					if(comment.setPrev == false) {
						statistic.addComment(comment);
					} else {
						System.out.println("withouth");
						statistic.addWithoutCountProb(comment);
//						statistic.addComment(comment);
					}
				}
				
				comment = new Comment();
				if (split[1].equals("POS")) {
					comment.original = true;
				} else {
					comment.original = false;
				}
				
				comment.code = split[0];
//				if(testCounter == 10) {
//					break;
//				}
			} else {
				split = line.split("\\s");
				boolean next = true;
				for (int i = 0; i < split.length; i++) {
					
					Word word;
					String stringWord = split[i].toLowerCase();

					// If the String is in our HashMap get the word out
					if ((word = statistic.getWord(stringWord)) != null) {
						comment.addWord(word);
					} else {
						if((word = statistic.newWords.get(stringWord)) != null) {
							comment.addWord(word);
						}
						comment.addNewWord(stringWord);
					}
					
//					Word word;
//					String stringWord = split[i].toLowerCase();
//					if(stringWord.indexOf("n't") != -1) { 
//
//						
//						next = false;
//					} else {
//						if(next) {
//							// If the String is in our HashMap get the word out
//							if ((word = statistic.getWord(stringWord)) != null) {
//								comment.addWord(word);
//							} 
//						} else {
//							next = true;
//						}
//					}
					
//					if(stringWord.equals("worst")) {
//						comment.result = false;
//						comment.setPrev = true;
//						
//						break;
//					}
				}
				
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
		
		System.out.println("Eles kommentek szama: " + statistic.comments.size());
		System.out.println("Program vege!");
		
		int helyes = 0;
		int hibas = 0;
		int neg = 0;
		for(Comment c: statistic.comments) {
//			System.out.println("Poz valszin: " + c.posProb + ", eredeti: " + c.original + ", szamolt: " + c.result);
			if(c.original == c.result) {
				helyes += 1;
			} else {
				hibas += 1;
				
				System.out.println(c.code + " " + c.posProb);
			}
			
			if(c.result == false) {
				neg++;
			}
		}
		
		double arany = (double) helyes / (double) (hibas + helyes);
		System.out.println("Helyes: " + helyes + ", Hibas: " + hibas + ", Helyes arany: " + arany);
		System.out.println("neg: " + neg);
//		
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
//		arany = (double) helyes / (double) (hibas + helyes);
//		System.out.println("Helyes: " + helyes + ", Hibas: " + hibas + ", Helyes arany: " + arany);
		
		
		
		br.close();
		br = new BufferedReader(new FileReader(testFile));
		
		/**
		 * A végső eredmény kiírása
		 */
		BufferedWriter bw = new BufferedWriter(new FileWriter("predictions.txt"));
		
		System.out.println("size: " + statistic.comments.size());
		
		int i = 0;
		
		boolean writeOut = false;
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
				
//				if(c.original != c.result && c.original == false) {
//					writeOut = true;
//				
//				
//					String newLine = "";
//					if(statistic.comments.get(i).result == true) {
//						newLine = line.replace("?", "POS");
//					} else {
//						newLine = line.replace("?", "NEG");
//					}
//					bw.write(newLine + "\n");
//				} else {
//					writeOut = false;
//				}
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

