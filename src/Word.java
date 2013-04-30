import java.util.Arrays;

/**
 * A szavakat reprezentáló osztály
 * @author Munkácsy Gergely
 *
 */
public class Word {

	private String answer; // a szó
	private int length; // a szó hossza
	private int id; // ID
	private char[] chars; // karakterenként reprezentálva
	private int lookAhead; // előretekintési érték
	private String clue; // a kérdés
	private int freq; // gyakoriság
	
	/**
	 * KOnstruktor
	 * @param id 
	 * @param answer
	 * @param clue
	 * @param freq
	 */
	public Word(int id, String answer, String clue, int freq) {
		
		this.id = id;
		this.answer = answer;
		this.length = answer.length();
		this.chars = new char[this.length];
		this.answer.getChars(0, this.length, this.chars, 0);
		this.lookAhead = 0; 
		this.clue = clue;
		this.freq = freq;
	}

	/* 
	 * Getter/setters 
	 */
	
	public int getFreq() {
		return this.freq;
	}
	
	public String getClue(){
		return this.clue;
	}
	
	public int getLookAhead() {
		return lookAhead;
	}

	public void setLookAhead(int lookAhead) {
		this.lookAhead = lookAhead;
	}

	public String getAnswer() {
		return answer;
	}

	public int getLength() {
		return length;
	}

	public int getId() {
		return id;
	}

	public char getChar(int index) {
		return chars[index];
	}
	
	/**
	 * Összehasonlít két szót 
	 */
	@Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Word w = (Word) obj;
        return (id == w.getId());
    }
	
	 @Override public int hashCode() {
	        return (41 * (41 + getId()) + getLength());
	    }

	@Override
	public String toString() {
		return "Word [answer=" + answer + ", length=" + length + ", id=" + id
				+ ", chars=" + Arrays.toString(chars) + "]";
	}
	
	
	
}
