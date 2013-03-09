import java.util.Arrays;


public class Word {

	private String answer;
	private int length;
	private int id;
	private char[] chars;
	
	public Word(int id, String answer) {
		
		this.id = id;
		this.answer = answer;
		this.length = answer.length();
		this.chars = new char[this.length];
		this.answer.getChars(0, this.length, this.chars, 0);

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
	public String toString() {
		return "Word [answer=" + answer + ", length=" + length + ", id=" + id
				+ ", chars=" + Arrays.toString(chars) + "]";
	}
	
	
	
}
