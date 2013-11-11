import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Statistic {
	public HashMap<String,Word> words = new HashMap<String,Word>();
	public ArrayList<Comment> comments = new ArrayList<Comment>();
	public int posCommentCount;
	public int negCommentCount;
	
	
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
	
	public Word getWord(String word) {
		return words.get(word);
	}
	
	public void addComment(Comment comment) {
		comment.countProb();
		comments.add(comment);
	}
	
	public void increaseCommentCount(boolean positive) {
		if(positive) {
			posCommentCount += 1;
		} else {
			negCommentCount += 1;
		}
	}
	
	public void countProbForWords() {
		Iterator<Word> iterator = words.values().iterator();
		while (iterator.hasNext()) {
			Word word = iterator.next();
			word.countProb(posCommentCount,negCommentCount);
		}
	}
}
