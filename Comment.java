import java.util.ArrayList;
import java.util.Iterator;


public class Comment {
	private ArrayList<Word> words = new ArrayList<Word>();
	public double posProb;
	public boolean original;
	public boolean result;
	
	public void addWord(Word word) {
		words.add(word);
	}
	
	public void countProb() {
		Iterator<Word> iterator = words.iterator();

		int wordCount = 1;
		double probSum = 0.5;
		
		while (iterator.hasNext()) {
			Word word = iterator.next();
			if(word.posProb >= 0.7 || word.posProb <= 0.3) {
				wordCount += 1;
				probSum += word.posProb;
			}
		}
		
		this.posProb = probSum / (double) wordCount;
		
		if(this.posProb >= 0.5) {
			result = true;
		} else {
			result = false;
		}
	}
}
