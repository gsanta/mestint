import java.io.BufferedReader;
import java.io.FileReader;

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
				
			} else {
				split = line.split("\\s+");

				for (int i = 0; i < split.length; i++) {
					String word = split[i].toLowerCase();
					
					statistic.addWord(word, positive);
					
				}
			}
		}
		br.close();
	}
}

