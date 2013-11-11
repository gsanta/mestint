import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {
	
	
	public static void main(String[] args) throws Exception{
		Statistic statistic = new Statistic();
		
		String trainingFile = args[0];
		String testFile = args[1];
		
		BufferedReader br = new BufferedReader(new FileReader(testFile));
		
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
		
		System.out.println("Training done!");

		br = new BufferedReader(new FileReader(testFile));
		Comment comment = null;
		
		while ((line = br.readLine()) != null) {
			split = line.split("\\t");
			if (split.length == 2) {
				
				
				if(comment != null) {
					statistic.addComment(comment);
				}
				
				comment = new Comment();
				if (split[1].equals("POS")) {
					comment.original = true;
				} else {
					comment.original = false;
				}
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
	}
}

