import com.sun.org.apache.xpath.internal.operations.String;

/**
 * @author Munkácsy Gergely
 *
 */
public class Column {
	
	private int startX;
	private int startY;
	private int length;
	private int freeSpaces;
	/**
	 * Vizszintes hasab
	 */
	private boolean isVertical = true;
	
	public Column() {
		
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
	static final int maxLengthOfColumn = 20;
	private String word;
	
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

}
