import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A statisztikákat tartalmazó osztály.
 * Tartalmazza a szavakat, amiket a train-ből nyer.
 * Illetve a teszt adatbázis kommentjeit.
 * 
 * @author gsanta
 *
 */
public class Statistic {
	/**
	 * A szavak a train-ből
	 */
	public HashMap<String,Word> words = new HashMap<String,Word>();
	
	public HashMap<String,Word> newWords = new HashMap<String,Word>();
	
	/**
	 * A kommenteket tartalmazza a teszt adatb-ből
	 */
	public ArrayList<Comment> comments = new ArrayList<Comment>();
	/**
	 * 
	 */
	public int posCommentCount;
	public int negCommentCount;
	
	/**
	 * Szó hozzáadása, illetve hogy poz kommentben van-e vagy neg-ben.
	 * Ha még nem volt ilyen szó, új létrehozása, egyébként meg csak a
	 * pos vagy neg counter növelése.
	 * @param word
	 * @param positive
	 * @return új szó == true, már benne van == false
	 */
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
	
	/**
	 * Word osztály kivétele a szó alapján
	 * @param word
	 * @return
	 */
	public Word getWord(String word) {
		return words.get(word);
	}
	
	/**
	 * Új komment hozzáadása, illetve a comment jóságának számítása
	 * @param comment
	 */
	public void addComment(Comment comment) {
		comment.countProb();
		comments.add(comment);
		///////////////////////////
		for(String w : comment.newWords) {
			if(newWords.containsKey(w)) {
				newWords.get(w).increaseNum(comment.result);
			} else {
				Word newWord = new Word(w, comment.result);
				newWords.put(w,newWord);
			}
			
			newWords.get(w).countProb(posCommentCount, negCommentCount);
		}
		
	}
	
	public void addWithoutCountProb(Comment comment) {
		comments.add(comment);
	}
	
	
	/**
	 * A pos illetve neg kommentek száma (a train alapján)
	 * @param positive
	 */
	public void increaseCommentCount(boolean positive) {
		if(positive) {
			posCommentCount += 1;
		} else {
			negCommentCount += 1;
		}
	}
	
	/**
	 * szavak feltöltése után valszín számítása.
	 * Meg kell adni hogy hány pos illetve hány neg komment volt összesen a normalizálás miatt (a train alapján).
	 */
	public void countProbForWords() {
		Iterator<Word> iterator = words.values().iterator();
		while (iterator.hasNext()) {
			Word word = iterator.next();
			word.countProb(posCommentCount,negCommentCount);
		}
	}
}
