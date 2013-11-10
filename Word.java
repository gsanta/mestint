
public class Word {
	public String word = "";
	public int negCount;
	public int posCount;
	
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
	
	public int hashCode() {
		return word.hashCode();
	}
}
