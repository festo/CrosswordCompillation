import java.io.*; 
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * @author Munkácsy Gergely
 * A keresztrejtvény rácsát eltároló adatszerkezet és a rajta végezhető műveletek
 */
public class Grid {
	
	private int height; // a rács magassága
	private int width; // a rács szélessége
	public ArrayList<Column> columns; // ha hasábban lévő oszlpok száma
	public ArrayList<int[]>[][] gridMatrix; // a hasábok kapcsolatait, metszőpontjait elmento mátrix
	private int[][] shape; // a rács mintája, hol van fehér négyzet, 0 - fekete, 1- fehér  
	private int[][] indexes; // a rácsban a hasábok indexei
	private char[][] chars; // a rács karakterenként ábrázolva
	private int[] lengthStat; // Statisztika arról, hogy az egyes hosszúságó hasábokból hány darab van
	private int notUsedColumn; // hány hasáb van amit még nem töltöttünk ki
	private HashSet<Word> usedWords; // melyek azok a szavak, amit már felhasználtunk
	
	/**
	 * Konstruktor, nullázza a változók értékeit
	 */
	public Grid() {
		this.height = 0;
		this.width = 0;
		this.columns = new ArrayList<Column>();
		usedWords = new HashSet<Word>();

	}
	
	/**
	 * Vissaadja a rács magasságát
	 * @return int
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * visszaadja a rács szálességét
	 * @return int
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * Inicualizálja a kezdő értékeket és feltölti a rácsot hasábokkal
	 * @param filename - A hasábokat tartalmazó fájl neve és elérési útja
	 * @throws IOException - Ha nem található a bemeneti fájl akkor kivételt dob
	 */
	@SuppressWarnings("unchecked")
	public void init(String filename) throws IOException {
		
		// hasáb statisztika elmentése
		lengthStat = new int[Settings.MAX_WORD_LENGTH];
		
		// betölti a fájl tartalmát
		this.loadGrid(filename);
		
		// inicalizálja a mátrixokat
		gridMatrix = new ArrayList[this.width][this.height];
		shape = new int[this.width][this.height];
		chars = new char[this.width][this.height];
		indexes = new int[this.width][this.height];
		
		// A mátrixos reprezentációt feltölti üres karakterekkel
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				gridMatrix[i][j] = new ArrayList<int[]>();
				shape[i][j] = 0;
				chars[i][j] = ' ';
			}
		}
		
		// segédváltozók
		Column c;
		int x,y;
		int[] pair;
		int count = 1;
		
		// eltárolom a hasábok közötti kapcsolatokat
		for (int i = 0; i < this.columns.size(); i++) {
			c = this.columns.get(i);
			x = c.getStartX();
			y = c.getStartY();
			if(indexes[x][y] == 0) { // beálítom a hasáb indexét, ha már azon a ponton kezdődik egy másik akkor nem írom felül, mert az biztos ellentétes orientáltságú
				indexes[x][y] = count;
				count++;
			}
			// elmentem a hasáb négyzeteit a rácsba, külün, külön belácoom.
			// Elvileg nemcsak 2D-re működne, azért lett így megalkotva
			for (int j = 0; j < c.getLength(); j++) {
				pair = new int[2]; 
				pair[0] = i;  // Hanyadik hasab
				pair[1] = j;  // Hanyadik betuje
				gridMatrix[x][y].add(pair);
				shape[x][y] = 1; // ha ott van négyzet, akkor az nem fekete
				if(c.isVertical()) {
					x++;
				} else {
					y++;
				}
			}
		}
		
	}
	
	/**
	 * Beolvassa a hasábokat tartalmazó fájlt
	 * A stuktúrája a következő:
	 * A fájl elején:
	 * 	M a hasáb szélessége
	 * 	N a hasáb magassága
	 * Enterrel elválasztva a hasábok listája
	 *  x y hossz függőleges
	 * @param filename a fájl neve és eléréi útja
	 * @throws IOException  - ha  nem található a fájl
	 */
	private void loadGrid(String filename) throws IOException {
		BufferedReader in = new BufferedReader( new FileReader(filename));
		String line;
		String[] lineArray;
		
		// Eslo ket sor a grid merete, szelesseg, magassag
		this.width = Integer.parseInt(in.readLine());
		this.height = Integer.parseInt(in.readLine());
		
		while((line = in.readLine())!= null) {
			lineArray = line.split(" ");
			Column c = new Column();
			
			c.setStartX(Integer.parseInt(lineArray[0])); // kezdő X
			c.setStartY(Integer.parseInt(lineArray[1])); // kezdő Y
			c.setLength(Integer.parseInt(lineArray[2])); // Hossz
			
			if(Integer.parseInt(lineArray[3]) == 1) { // orientáltság
				c.setVertical(true);
			} else {
				c.setVertical(false);
			}
			
			this.columns.add(c);
			lengthStat[c.getLength()]++;
		}
		
		in.close();
		this.notUsedColumn = this.columns.size();
	}
	
	/**
	 * Egy karakter beállítása a megadott pozícíóba, x, y koordináta alapján
	 * Ha több hasáb metszéspontja akkor minden egyes hasábra beállíja ugyan azt.
	 * @param x A karakter X koordinataja a racsban
	 * @param y A karakter Y koordinataja a racsban
	 * @param ch
	 */
	public void setChar(int x, int y, char ch) {
		int[] pair = new int[2];
		
		// végig megyünk a metszeten
		for (int i = 0; i < this.gridMatrix[x][y].size(); i++) {
			pair = this.gridMatrix[x][y].get(i);
			// eltároljuk a karaktert
			this.columns.get(pair[0]).setChar(pair[1], ch);
		}
		// elmentjük a karaktert az ábrázoláshoz
		chars[x][y] = ch;
	}
	
	/**
	 * Torol egy karaktert a hasabokbol
	 * @param x A karakter X koordinataja a racsban
	 * @param y A karakter Y koordinataja a racsban
	 */
	public void deleteChar(int x, int y) {
		int[] pair = new int[2];
		int clear = 0;
		boolean deleted = true;
		
		// Megnézzük, hogy ha a hasáb már egy adott szóval ki van töltve és egy metszetet törlünk akkor ne rontsa el a kés szót.
		for (int i = 0; i < this.gridMatrix[x][y].size(); i++) {
			pair = this.gridMatrix[x][y].get(i);
			if( this.columns.get(pair[0]).isFilled() ) {
				deleted = false;
			}
		}
		
		// Ha törölhető, akkor töröljük
		if(deleted) {
			for (int i = 0; i < this.gridMatrix[x][y].size(); i++) {
				pair = this.gridMatrix[x][y].get(i);
				if (!this.columns.get(pair[0]).isFilled()) {
					this.columns.get(pair[0]).setChar(pair[1], ' ');
					clear++;
				}
			}

			if (clear == this.gridMatrix[x][y].size()) {
				chars[x][y] = ' ';
			}
		}
	}
	
	/**
	 * Visszaadja a megadott koordinátán található karaktert.
	 * @param x A karakter X koordinataja a racsban
	 * @param y A karakter Y koordinataja a racsban
	 * @return char
	 */
	public char getChar(int x, int y) {
		int[] pair = new int[2];
		pair = this.gridMatrix[x][y].get(0);
		return this.columns.get(pair[0]).getChar(pair[1]);
	}
	
	/**
	 * Egy egész szót beállít egy hasábba, gyakorlatilag végigiterál karakterenként
	 * @param w a beírandó szó
	 * @param c a kitöltendő hasáb
	 */
	public void setWorToColumn(Word w, Column c) {
		int x = c.getStartX();
		int y = c.getStartY();
		// karakterenként elmentjük a szót
		for (int i = 0; i < c.getLength(); i++) {
			
			setChar(x, y, w.getChar(i));
			
			if(c.isVertical()) {
				x++;
			} else {
				y++;
			}
		}
		
		//Megkeressuk a megadott oszlopot majd beallitjuk a megfelelo ertekeket
		for (int i = 0; i < columns.size(); i++) {
			if(columns.get(i).equals(c)) {
				columns.get(i).setWord(w); // Eltaroljuk az ID-t
				columns.get(i).setFilled(true); // Megjeloljuk, hogy egy szovel lett kitoltva nem a cellak egyessevel
			}
		}
		
		// ezt a szót má ne használjuk fel többször
		usedWords.add(w);
		
		this.notUsedColumn--;
	}
	
	/**
	 * Egy hasábból kitörli a beleírt szót
	 * @param w a törlendő szó
	 * @param c a kiválasztott hasáb
	 */
	public void clearColumn(Word w, Column c) {
		int x = c.getStartX();
		int y = c.getStartY();
		int index = 0;
		
		// Megkeressük a hasábot
		for (int i = 0; i < columns.size(); i++) {
			if(columns.get(i).equals(c)) {
				index = i;
			}
		}
		
		// Meghívkuk rá a törlést, ezzel lenullázzuk a változókat is, nem a karaktereket töröljük
		columns.get(index).clear();
		
		// majd tötöljük a karaktereket
		for (int i = 0; i < c.getLength(); i++) {
			
			deleteChar(x, y);
			
			if(c.isVertical()) {
				x++;
			} else {
				y++;
			}
		}
		
		usedWords.remove(w);
		
		this.notUsedColumn++;
	}
	
	/**
	 * Veletlen random boolenan generator
	 * @return veletlen igaz vagy hamis ertek
	 */
	public boolean getRandomBoolean() {
	  return Math.random() < 0.5;
	}
	
	/**
	 * Visszaadja, hogy egy szót felhasználtunk-e már a generálás folyamán
	 * @param w a keresett szó
	 * @return true/false
	 */
	public boolean isUsedWord(Word w) {
		return usedWords.contains(w);
	}

	/**
	 * Visszaadja a leghosszab hasabot. Ha tobb is egyforma hosszu akkor veletleneul valaszt egyet
	 * @return Column
	 */
	public Column getLongest() {
		Column longest = columns.get(0); // Az elsonek keresunk hosszabbat
		
		for (int i = 1; i < columns.size(); i++) {
			if(longest.getLength() < columns.get(i).getLength()) {
				longest = columns.get(i);
			} else if(longest.getLength() == columns.get(i).getLength() && getRandomBoolean()) {
				longest = columns.get(i);
			}
		}
		
		return longest;
	}
	
	
	/**
	 * Hanyadik column a listában
	 * @param c amit keresünk
	 * @return sorszáma
	 */
	public int getColumnId(Column c) {
		
		for (int i = 1; i < columns.size(); i++) {
			if(c == columns.get(i)) {
				return i;
			}
		}
		
		return 0;
	}
	
	/*
	 * Hogy külső osztályokból is elérhető legyen
	 */
	
	public int[][] getShape() {
		return shape;
	}
	
	public int[][] getIndexes() {
		return indexes;
	}
	
	public char[][] getChars() {
		return chars;
	}
	
	public int[] getlengthStat() {
		return lengthStat;
	}

	public boolean isFull() {
		return (notUsedColumn == 0);
	}
	
	/**
	 * Visszaadja azt, hogy a felhasznált szavak alapján egy rejtvény milyen neház
	 * @return szavak nehézségének átlaga
	 */
	public double getWordsDifficulty() {
		int sum;
		sum = 0;
		for (int i = 0; i < columns.size(); i++) {
			sum += this.columns.get(i).getWord().getFreq();
		}

		return (sum / this.columns.size());
	}
	
	/**
	 * Visszaadja azt, hogy a rács szempontjából a rejtvény milyen nehéz
	 * Részletes magyarázat a szakdolgozatomban
	 * @return a rács nehézségének arányszáma
	 */
	public double getGridDifficulty() {
		int[] pair = new int[2];
		int sum, cSum;
		int x,y;
		Column c;
		sum = 0;
		for (int i = 0; i < columns.size(); i++) {
			c = columns.get(i);
			cSum = 1;
			x = c.getStartX();
			y = c.getStartY();
			for (int j = 0; j < c.getLength(); j++) {
				
				for (int k = 0; k < this.gridMatrix[x][y].size(); k++) {
					pair = this.gridMatrix[x][y].get(k);
					if(pair[0] != i) {
						cSum *= this.columns.get(pair[0]).getLength();	
					}
				}
				
				if(c.isVertical()) {
					x++;
				} else {
					y++;
				}
			}
			sum += cSum;
		}
		
		return (sum / (Math.pow(this.width, this.height) * columns.size()));
	}
	
	/**
	 * A PDF elmentséhez kinegenrálja a rácsot egy HTML táblázatba
	 * @return String
	 */
	public String toHTML() {
		String html = "";
		for(int x=0; x<this.width; x++){
			html += "<tr>";
            for(int y=0; y<this.height; y++){
            	html += "<td";           
            	if(shape[x][y] == 0) {
            		html +=" class='blank'";
            	}
            	html += ">"; 
            	
            	if(indexes[x][y] != 0) {
            		html +=indexes[x][y];
            	}
            			
            	html += "</td>";
            }
            html += "</tr>";
		}
		return html;
	}
	
	/**
	 * Visszaadja a függőlegesen felsorot szavakat egy HTML táblázatban a PDF generáláshoz
	 * @return String
	 */
	public String getVertivalHTML() {
		String html = "";
		int x,y;
		for (int i = 0; i < columns.size(); i++) {
			if(columns.get(i).isVertical()) {
				html += "<tr>";
				
				html += "<td>";
				x = columns.get(i).getStartX();
				y = columns.get(i).getStartY();
				html += indexes[x][y]+".";
				html += "</td>";
				
				html += "<td>";
				html += columns.get(i).getWord().getClue();
				html += "</td>";
				html += "</tr>";
			}
		}
		return html;
	}
	
	/**
	 * Visszaadja a vízszintes felsorot szavakat egy HTML táblázatban a PDF generáláshoz
	 * @return String
	 */
	public String getHorisontalHTML() {
		String html = "";
		int x,y;
		for (int i = 0; i < columns.size(); i++) {
			if(!columns.get(i).isVertical()) {
				html += "<tr>";
				
				html += "<td>";
				x = columns.get(i).getStartX();
				y = columns.get(i).getStartY();
				html += indexes[x][y]+".";
				html += "</td>";
				
				html += "<td>";
				html += columns.get(i).getWord().getClue();
				html += "</td>";
				html += "</tr>";
			}
		}
		return html;
	}
	
	/**
	 * Történt-e már beszúrás
	 * @return
	 */
	public boolean isStart() {
		return (notUsedColumn == columns.size());
	}	

}
