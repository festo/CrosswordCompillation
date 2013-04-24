import java.io.*; 
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * @author Munkácsy Gergely
 *
 */
public class Grid {
	
	private int height;
	private int width;
	public ArrayList<Column> columns;
	public ArrayList<int[]>[][] gridMatrix;
	private int[][] shape;
	private int[][] indexes; 
	private char[][] chars;
	private int[] lengthStat;
	private int notUsedColumn;
	private HashSet<Word> usedWords;
	
	public int minNotUsed; // Debug
	
	public Grid() {
		this.height = 0;
		this.width = 0;
		this.columns = new ArrayList<Column>();
		usedWords = new HashSet<Word>();

	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	@SuppressWarnings("unchecked")
	public void init(String filename) throws IOException {
		
		lengthStat = new int[Settings.MAX_WORD_LENGTH];
		
		this.loadGrid(filename);
		
		gridMatrix = new ArrayList[this.width][this.height];
		shape = new int[this.width][this.height];
		chars = new char[this.width][this.height];
		indexes = new int[this.width][this.height];
				
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				gridMatrix[i][j] = new ArrayList<int[]>();
				shape[i][j] = 0;
				chars[i][j] = ' ';
			}
		}
		
		Column c;
		int x,y;
		int[] pair;
		int count = 1;
		for (int i = 0; i < this.columns.size(); i++) {
			c = this.columns.get(i);
			x = c.getStartX();
			y = c.getStartY();
			if(indexes[x][y] == 0) {
				indexes[x][y] = count;
				count++;
			}
			for (int j = 0; j < c.getLength(); j++) {
				pair = new int[2];
				pair[0] = i;  // Hanyadik hasab
				pair[1] = j;  // Hanyadik betuje
				gridMatrix[x][y].add(pair);
				shape[x][y] = 1;
				if(c.isVertical()) {
					x++;
				} else {
					y++;
				}
			}
		}
		
	}
	
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
			
			c.setStartX(Integer.parseInt(lineArray[0]));
			c.setStartY(Integer.parseInt(lineArray[1]));
			c.setLength(Integer.parseInt(lineArray[2]));
			
			if(Integer.parseInt(lineArray[3]) == 1) {
				c.setVertical(true);
			} else {
				c.setVertical(false);
			}
			
			this.columns.add(c);
			lengthStat[c.getLength()]++;
		}
		
		in.close();
		this.notUsedColumn = this.columns.size();
		this.minNotUsed = this.notUsedColumn;
	}
	
	public void setChar(int x, int y, char ch) {
		int[] pair = new int[2];
		
		for (int i = 0; i < this.gridMatrix[x][y].size(); i++) {
			pair = this.gridMatrix[x][y].get(i);
			this.columns.get(pair[0]).setChar(pair[1], ch);
		}
		chars[x][y] = ch;
	}
	
	/**
	 * Torol egy karaktert a hasabokbol
	 * @param x A karakter X coordinataja a racsban
	 * @param y A karakter Y coordinataja a racsban
	 */
	public void deleteChar(int x, int y) {
		int[] pair = new int[2];
		int clear = 0;
		boolean deleted = true;
		
		for (int i = 0; i < this.gridMatrix[x][y].size(); i++) {
			pair = this.gridMatrix[x][y].get(i);
			if( this.columns.get(pair[0]).isFilled() ) {
				deleted = false;
			}
		}
		
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
	
	public char getChar(int x, int y) {
		int[] pair = new int[2];
		pair = this.gridMatrix[x][y].get(0);
		return this.columns.get(pair[0]).getChar(pair[1]);
	}
	
	public void setWorToColumn(Word w, Column c) {
		int x = c.getStartX();
		int y = c.getStartY();
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
		
		usedWords.add(w);
		
		this.notUsedColumn--;
	}
	
	public void clearColumn(Word w, Column c) {
		int x = c.getStartX();
		int y = c.getStartY();
		int index = 0;
		
		for (int i = 0; i < columns.size(); i++) {
			if(columns.get(i).equals(c)) {
				index = i;
			}
		}
		
		columns.get(index).clear();
		
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
	
	public boolean isUsedWord(Word w) {
		return usedWords.contains(w);
	}

	/*
	 * Visszaadja a leghosszab hasabot. Ha tobb is egyforma hosszu akkor veletleneul valaszt egyet
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
	
	public double getWordsDifficulty() {
		int sum;
		sum = 0;
		for (int i = 0; i < columns.size(); i++) {
			sum += this.columns.get(i).getWord().getFreq();
		}

		return (sum / this.columns.size());
	}
	
	public double getGridDifficulty() {
		int[] pair = new int[2];
		int sum, cSum;
		int x,y;
		Column c;
		sum = 0;
		for (int i = 0; i < columns.size(); i++) {
			c = columns.get(i);
			cSum = 1;
			for (int j = 0; j < c.getLength(); j++) {
				x = c.getStartX();
				y = c.getStartY();
				
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
	
	public String getVertivalHTML() {
		String html = "";
		int x,y;
		Charset utf8charset = Charset.forName("UTF-8");
		for (int i = 0; i < columns.size(); i++) {
			if(columns.get(i).isVertical()) {
				html += "<tr>";
				
				html += "<td>";
				x = columns.get(i).getStartX();
				y = columns.get(i).getStartY();
				html += indexes[x][y]+".";
				html += "</td>";
				
				html += "<td>";
//				html += new String ( columns.get(i).getWord().getClue().getBytes(), utf8charset );
				html += columns.get(i).getWord().getClue();
				html += "</td>";
				html += "</tr>";
			}
		}
		return html;
	}
	
	public String getHorisontalHTML() {
		String html = "";
		int x,y;
		Charset utf8charset = Charset.forName("UTF-8");
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
	
	public boolean isStart() {
		return (notUsedColumn == columns.size());
	}	

}
