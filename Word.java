import java.util.Iterator;


public class Word {
	public String word = "";
	public int negCount;
	public int posCount;
	public double posProb;
	
	public Word(String word, boolean positive) {
		this.word = word;
		if(positive) {
			posCount = 1;
		} else {
			negCount = 1;
		}
	}
	
	public int getAllCount() {
		return negCount + posCount;
	}
	
	public void increaseNum(boolean positive) {
		if(positive) {
			posCount += 1;
		} else {
			negCount += 1;
		}
	}
	
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		
		return word.equals(((Word) other).word);
	}
	
	public void countProb(int allPos, int allNeg) {
		double normPos = (double) posCount / (double) allPos;
		double normNeg = (double) negCount / (double) allNeg;
		
		posProb = normPos / (normPos + normNeg);
	}
	
	public int hashCode() {
		return word.hashCode();
	}
	
}
