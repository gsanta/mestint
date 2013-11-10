import java.util.HashMap;


public class Statistic {
	private HashMap<String,Word> words = new HashMap<String,Word>();
	
	
	public boolean addWord(String word, boolean positive) {
		if(!words.containsKey(word)) {
			
			Word w = new Word(word,positive);
			words.put(w.word, w);
			
			return true;
		} else {
			
			Word w = words.get(word);
			w.increaseNum(positive);
			
			return false;
		}
	}
	
}
