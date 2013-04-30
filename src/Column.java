import java.util.Arrays;
import java.lang.String;

/**
 * @author Munkácsy Gergely
 * A hasábok tárolására szolgáló osztály
 */
public class Column {
	
	private int startX; // a hasáb kezdő X pozíciója
	private int startY; // a hasáb kezdő Y pozíciója
	private int length; // a hasáb hossza
	private int freeSpaces; // a hasábban lévő üres négyzetek száma
	private boolean isFilled; // Igaz, ha  ahasáb teljesen ki van töltve egy egész szóval
	private boolean isVertical = true; // Függőleges-e?
	private char[] word; // A Word osztály egy objektuma, az amit beszúrtunk a hasábba
	private Word w; // a hasáb tartalma karakterenként
	private int index; // a hasáb indexe
	
	/**
	 * Konstruktor, beállítja az alapértelmezett értékeket
	 */
	public Column() {
		this.startX = 0;
		this.startY = 0;
		this.length = 0;
		this.freeSpaces = 0;
		this.isFilled = false;
	}
	
	/**
	 * Beállítja az index értékét
	 * @param i - (int) index
	 */
	public void setIndex(int i) {
		this.index = i;
	}
	
	/**
	 * Visszaadja az inde értékét
	 * @return int
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Beállítja a w értékét
	 * @param word = (Word) a hasábot kitöltő szó
	 */
	public void setWord(Word word) {
		this.w = word;
	}
	
	/**
	 * Visszaadja a w értékét
	 * @return Word
	 */
	public Word getWord() {
		return this.w;
	}
	
	/**
	 * @return true, ha a hasáb függőleges, különben false
	 */
	public boolean isVertical() {
		return isVertical;
	}
	
	/**
	 * Beállítja  ahasáb orientáltságát
	 * @param true ha a hasáb függőleges
	 */
	public void setVertical(boolean isVertical) {
		this.isVertical = isVertical;
	}
	
	/**
	 * A hasáb kezdő X koordinátája
	 * @return int
	 */
	public int getStartX() {
		return startX;
	}
	
	/** 
	 * Beállítja a hasáb kezdő X értékét
	 * @param int
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}
	
	/**
	 * A hasáb kezdő Y koordinátája
	 * @return 
	 */
	public int getStartY() {
		return startY;
	}
	
	/**
	 * Beállítja a hasáb kezdő X értékét
	 * @param int
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}
	
	/**
	 * Visszaadja a hasáb hosszát
	 * @return int
	 */
	public int getLength() {
		return length;
	}
	
	/** Beállítja a hasáb hosszát, inicializáláskor használhatjuk
	 * @param int
	 */
	public void setLength(int length) {
		this.length = length;
		this.freeSpaces = length; // a szabad helyek szám maximális
		this.word = new char[this.length]; 
		for (int i = 0; i < this.length; i++) {
			this.word[i] = ' '; // üres karakterrel feltöltjük
		}
	}

	/**
	 * Beállítja a hasáb egy adott négyzetét a megadot karakterre
	 * @param i a négyzet sorszáma
	 * @param c a beírandó karakter
	 */
	public void setChar(int i, char c) {
		
		// ha üres karakterre álítjuk vissza (töröljük) akkor a szabad helyek száma nől, különben fordítva
		if(c == ' ' && this.freeSpaces < this.length) {
			this.freeSpaces++;
		} else if (c != ' ' && this.freeSpaces > 0) {
			this.freeSpaces--;
		}
		
		this.word[i] = c;
	}
	
	/**
	 * Visszaadja egy adott négyzetben található karaktert
	 * @param i a négyzet indexe
	 * @return char
	 */
	public char getChar(int i) {
		return this.word[i];
	}
	
	/**
	 * A hasábban lévő üres négyzetek száma
	 * @return int
	 */
	public int getFreeSpaces() {
		return freeSpaces;
	}
	
	/**
	 * A kitöltött négyzetek száma
	 * @return int
	 */
	public int getFilledSpaaces() {
		return this.length - this.freeSpaces;
	}
	
	/**
	 * true, ha már legalább egy karaktert beszúrtunk a hasábba
	 * @return boolean
	 */
	public boolean isStarted() {
		return (this.length > this.freeSpaces);
	}

	/**
	 * true, ha a hasáb teljesen kitöltött egy szó által
	 * @return boolean
	 */
	public boolean isFilled() {
		return isFilled;
	}

	/**
	 * Beállítja az isFilled értékét
	 * @param isFilled - igaz, ha  ahasáb egy szó által teljesen ki van töltve
	 */
	public void setFilled(boolean isFilled) {
		this.isFilled = isFilled;
	}

	/**
	 * A hasáb tartalmának megfeleően kigenerálja az SQL lekérdezés WHERE feltételébe ilesztendő feltételek listáját 
	 * @return String
	 */
	public String getSQL() {
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
	
	/**
	 * Törli a hasábba beírt szót
	 */
	public void clear() {
		this.isFilled = false;
		this.w = null;
		
	}

	@Override
	public String toString() {
		return "Column [isFilled="
				+ isFilled() + ", isStarted=" + isStarted() + " word=" + Arrays.toString(word) + "]";
	}
	
}
