import java.util.Arrays;
import java.lang.String;

/**
 * @author Munkácsy Gergely
 *
 */
public class Column {
	
	private int startX;
	private int startY;
	private int length;
	private int freeSpaces;
	private boolean isVertical = true;
	private char[] word;
	
	public Column() {
		this.startX = 0;
		this.startY = 0;
		this.length = 0;
		this.freeSpaces = 0;
		this.word = new char[Settings.MAX_WORD_LENGTH];
		for (int i = 0; i < Settings.MAX_WORD_LENGTH; i++) {
			this.word[i] = ' ';
		}
	}
	
	/**
	 * @return the isVertical
	 */
	public boolean isVertical() {
		return isVertical;
	}
	/**
	 * @param isVertical the isVertical to set
	 */
	public void setVertical(boolean isVertical) {
		this.isVertical = isVertical;
	}
	
	/**
	 * @return the startX
	 */
	public int getStartX() {
		return startX;
	}
	/**
	 * @param startX the startX to set
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}
	/**
	 * @return the startY
	 */
	public int getStartY() {
		return startY;
	}
	/**
	 * @param startY the startY to set
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
		this.freeSpaces = length;
	}

	public void setChar(int i, char c) {
		this.word[i] = c;
		this.freeSpaces--;
	}
	
	public char getChar(int i) {
		return this.word[i];
	}
	
	public String getWord() {
		return new String(word);
	}

	@Override
	public String toString() {
		return "Column [startX=" + startX + ", startY=" + startY + ", length="
				+ length + ", freeSpaces=" + freeSpaces + ", isVertical="
				+ isVertical + ", word=" + Arrays.toString(word) + "]";
	}
	
	
}
