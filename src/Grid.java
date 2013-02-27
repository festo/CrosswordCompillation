import java.io.*; 
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.text.html.HTMLDocument.Iterator;

/**
 * @author Munkácsy Gergely
 *
 */
public class Grid {
	
	private int height;
	private int width;
	private ArrayList<Column> columns;
	private ArrayList<int[]>[][] gridMatrix;
	private int[][] shape; 
	private char[][] chars;
	
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
		
		this.loadGrid(filename);
		
		gridMatrix = new ArrayList[this.width][this.height];
		shape = new int[this.width][this.height];
		chars = new char[this.width][this.height];
				
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				gridMatrix[i][j] = new ArrayList<int[]>();
				shape[i][j] = 0;
			}
		}
		
		Column c;
		int x,y;
		int[] pair;
		for (int i = 0; i < this.columns.size(); i++) {
			c = this.columns.get(i);
			x = c.getStartX();
			y = c.getStartY();
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
	
	public int[][] getShape() {
		return shape;
	}
	
	public char[][] getChars() {
		return chars;
	}
	
	/**
	 * Debug functions
	 */
	
	public void showColumns() {
		for (Column c : this.columns) {
			System.out.println(c);
		}
	}

}
