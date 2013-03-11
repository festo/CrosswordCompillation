import java.io.*; 
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Munkácsy Gergely
 *
 */
public class Grid {
	
	private int height;
	private int width;
	public ArrayList<Column> columns;
	private ArrayList<int[]>[][] gridMatrix;
	private int[][] shape;
	private int[][] indexes; 
	private char[][] chars;
	private int[] lengthStat;
	
	public Grid() {
		this.height = 0;
		this.width = 0;
		this.columns = new ArrayList<Column>();

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
	}
	
	public void setChar(int x, int y, char ch) {
		int[] pair = new int[2];
		
		for (int i = 0; i < this.gridMatrix[x][y].size(); i++) {
			pair = this.gridMatrix[x][y].get(i);
			this.columns.get(pair[0]).setChar(pair[1], ch);
		}
		chars[x][y] = ch;
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
		
		//Set ID
		for (int i = 0; i < columns.size(); i++) {
			if(columns.get(i).equals(c)) {
				columns.get(i).setId(w.getId());
				columns.get(i).setFilled(true);
			}
		}
	}
	
	/**
	 * Veletlen random boolenan generator
	 * @return veletlen igaz vagy hamis ertek
	 */
	public boolean getRandomBoolean() {
	  return Math.random() < 0.5;
	  
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

	
	/**
	 * Debug functions
	 */
	
	public void debug() {
//		for (Column c : this.columns) {
//			System.out.println(c);
//		}
		for (int i = 0; i < lengthStat.length; i++) {
			System.out.println(lengthStat[i]);
		}
	}

}
