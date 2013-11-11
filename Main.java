import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {
	
	
	public static void main(String[] args) throws Exception{
		Statistic statistic = new Statistic();
		
		String trainingFile = "train.txt";//args[0];
		String testFile = "dev.txt";//args[1];
		
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
			}
		}
		
		br.close();
		
		statistic.countProbForWords();
		
		System.out.println("Training done!");
		System.out.println("Pos: " + statistic.posCommentCount + ", Neg: " + statistic.negCommentCount);
		System.out.println("Osszes szavak szama: " + statistic.words.keySet().size());

		br = new BufferedReader(new FileReader(testFile));
		Comment comment = null;
		
		int testCounter = 0;
		
		while ((line = br.readLine()) != null) {
			split = line.split("\\t");
			if (split.length == 2) {
//				testCounter++;
				
				if(comment != null) {
					statistic.addComment(comment);
				}
				
				comment = new Comment();
				if (split[1].equals("POS")) {
					comment.original = true;
				} else {
					comment.original = false;
				}
				
//				if(testCounter == 10) {
//					break;
//				}
			} else {
				split = line.split("\\s");

				for (int i = 0; i < split.length; i++) {
					
					Word word;
					String stringWord = split[i].toLowerCase();

					// If the String is in our HashMap get the word out
					if ((word = statistic.getWord(stringWord)) != null) {
						comment.addWord(word);
					} 
				}

			}

		}
		
		System.out.println("Eles kommentek szama: " + statistic.comments.size());
		System.out.println("Program vege!");
		
		int helyes = 0;
		int hibas = 0;
		for(Comment c: statistic.comments) {
//			System.out.println("Poz valszin: " + c.posProb + ", eredeti: " + c.original + ", szamolt: " + c.result);
			if(c.original == c.result) {
				helyes += 1;
			} else {
				hibas += 1;
			}
		}
		
		double arany = (double) helyes / (double) (hibas + helyes);
		System.out.println("Helyes: " + helyes + ", Hibas: " + hibas + ", Helyes arany: " + arany);
	}
}

