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
	private boolean isFilled;
	private boolean isVertical = true;
	private char[] word;
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Column() {
		this.startX = 0;
		this.startY = 0;
		this.length = 0;
		this.freeSpaces = 0;
		this.isFilled = false;
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
		this.word = new char[this.length];
		for (int i = 0; i < this.length; i++) {
			this.word[i] = ' ';
		}
	}

	public void setChar(int i, char c) {
//		if(this.freeSpaces > 0 && word[i] == ' ') {
//			this.freeSpaces--;
//		} else if (this.freeSpaces < this.length  && word[i] != ' ') {
//			this.freeSpaces++;
//		}
		
		if(c == ' ' && this.freeSpaces < this.length) {
			this.freeSpaces++;
		} else if (c != ' ' && this.freeSpaces > 0) {
			this.freeSpaces--;
		}
		
		this.word[i] = c;
	}
	
	public char getChar(int i) {
		return this.word[i];
	}
	
	public String getWord() {
		return new String(word);
	}
	
	public int getFreeSpaces() {
		return freeSpaces;
	}
	
	public int getFilledSpaaces() {
		return this.length - this.freeSpaces;
	}
	
	public boolean isStarted() {
		return (this.length > this.freeSpaces);
	}

	public boolean isFilled() {
		return isFilled;
	}

	public void setFilled(boolean isFilled) {
		this.isFilled = isFilled;
	}

	public String getSQL() {
//		System.out.println(Arrays.toString(word));
		String SQL = "";
		for (int i = 0; i < word.length; i++) {
			if(word[i] == ' ') {
				SQL += "c"+ (i+1) +" like '_'";
			} else {
				SQL += "c"+ (i+1) +" like '"+word[i]+"'";
			}
			
			if(i+1 != word.length)
				SQL += " and ";
		}
		return SQL;
	}

	@Override
	public String toString() {
		return "Column [isFilled="
				+ isFilled() + ", isStarted=" + isStarted() + " word=" + Arrays.toString(word) + "]";
	}

	public void clear() {
		this.isFilled = false;
		this.id = 0;
		
	}
	
	
}
