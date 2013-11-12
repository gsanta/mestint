import java.util.Iterator;

/**
 * A szavakat tárolja, illetve hogy hány pos, illetve hány neg
 * kommentben szerepelt a train adatbázisban.
 * A végén ezek alapján egy jósági valszín számítása.
 * @author gsanta
 *
 */
public class Word {
	/**
	 * A szó
	 */
	public String word = "";
	/**
	 * Hány negatív kommentben szerepel (ha 1 kommentben többször előfordul, akkor többször van hozzáadva)
	 */
	public int negCount;
	/**
	 * Hány pozitív kommentben szerepel (ha 1 kommentben többször előfurdul, akkor többször van hozzáadva)
	 */
	public int posCount;
	/**
	 * A számított jósági valszín
	 */
	public double posProb;
	
	/**
	 * Szó első előfordulásakor, maga a szó megadása a konstruktornak, illetve, hogy épp neg vagy poz komment-e.
	 * @param word
	 * @param positive
	 */
	public Word(String word, boolean positive) {
		this.word = word;
		if(positive) {
			posCount = 1;
		} else {
			negCount = 1;
		}
	}
	
	/**
	 * Hányszor szerepelt a szó
	 * @return
	 */
	public int getAllCount() {
		return negCount + posCount;
	}
	
	/**
	 * Ha újra előfurdul a szó, a neg vagy pos számláló növelése, attól függően milyen kommentben volt.
	 * @param positive
	 */
	public void increaseNum(boolean positive) {
		if(positive) {
			posCount += 1;
		} else {
			negCount += 1;
		}
	}
	
	/**
	 * Csak a szavakat hasonlítja össze
	 */
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		
		return word.equals(((Word) other).word);
	}
	
	/**
	 * Miután lefutott a train, jósági valszín számítása.
	 * Az elején normalizál, mert lehet hogy a pos és neg kommentek száma nem egyenlő
	 * @param allPos
	 * @param allNeg
	 */
	public void countProb(int allPos, int allNeg) {
		double normPos = (double) posCount / (double) allPos;
		double normNeg = (double) negCount / (double) allNeg;
		
		posProb = normPos / (normPos + normNeg);
	}
	
	/**
	 * A szó hashCode-ját adja vissza.
	 */
	public int hashCode() {
		return word.hashCode();
	}
	
}
