import java.util.Arrays;


public class Word {

	private String answer;
	private int length;
	private int id;
	private char[] chars;
	private int lookAhead;
	private String clue;
	
	public Word(int id, String answer, String clue) {
		
		this.id = id;
		this.answer = answer;
		this.length = answer.length();
		this.chars = new char[this.length];
		this.answer.getChars(0, this.length, this.chars, 0);
		this.lookAhead = 0; 
		this.clue = clue;
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
