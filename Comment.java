import java.util.ArrayList;
import java.util.Iterator;

/**
 * Komment osztály, ami a kommenteket tartalmazza szavakként felbontva.
 * Miután a szavak fel lettek töltve, futtatni kell a countProb() metódust,
 * ami kiszámolja a komment jósági valószínűségét (0% - 100%).
 * @author sagtabt.sze
 *
 */
public class Comment {
	/**
	 * A komment szavai
	 */
	private ArrayList<Word> words = new ArrayList<Word>();
	
	public ArrayList<String> newWords = new ArrayList<String>();
	/**
	 * A jósági valszín
	 */
	public double posProb = 1;//4000.0 / (4000.0 + 2000.0) ;
	
	public double negProb = 1;//2000.0 / (4000.0 + 2000.0);
	
	/**
	 * tesztelésre, az komment valódi jósága
	 */
	public boolean original;
	/**
	 * A jósági valszín alapján számított eredmény (true == pos, false == neg)
	 */
	public boolean result;
	
	public String code;
	
	public boolean setPrev = false;
	
	/**
	 * szó hozzáadása
	 * @param word
	 */
	public void addWord(Word word) {
		words.add(word);
	}
	
	public void addNewWord(String word) {
		newWords.add(word);
	}
	
	/**
	 * valszín kiszámítása
	 * végigmegy a szavakon, összeadja a valszíneket,
	 * majd leosztja a szavak számával.
	 */
	public void countProb() {
		Iterator<Word> iterator = words.iterator();

		int wordCount = 1;
		double probSum = 0.5;
		
		
		while (iterator.hasNext()) {
			Word word = iterator.next();
//			if(word.posProb >= 0.6 || word.posProb <= 0.4) {
//				wordCount += 1;
//				
//				probSum += word.posProb;
				if(word.posProb != 0) {
					posProb *= word.posProb;
				}
				if(word.negProb != 0) {
					negProb *= word.negProb;
				}
//			}
		}
		
//		this.posProb = probSum / (double) wordCount;
//		
//		if(this.posProb >= 0.5) {
//			result = true;
//		} else {
//			result = false;
//		}
//		System.out.println(posProb);
		if(posProb > negProb) {
			result = true;
		} else {
			result = false;
		}
	}
}
