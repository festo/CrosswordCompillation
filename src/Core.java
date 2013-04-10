import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;


public class Core {
	
	private Grid grid;
	private Grid filledGrid;
	private WordsDAO words;
	private boolean end = false;
	private int tryCounter = 0;

	public Core() throws IOException {
		try {
			grid = new Grid();
			grid.init("resources/grids/grid3.txt");
			System.out.println("A rács nehézsége: " + grid.getDifficulty());
			GUI.createAndShowGUI(grid);
			
			words = new WordsDAO();
			words.setLengthStat(grid.getlengthStat());
			words.fillTheMemory();
			
//			System.out.println("-- START --");
			long startTime = System.currentTimeMillis();
			
			generate();
			
			long stopTime = System.currentTimeMillis();
			System.out.print("Beszúrások száma: " + tryCounter +" ");
			printRunTime((stopTime - startTime));
			
		} catch(SQLException e) {
		      System.err.println(e.getMessage());
		} finally {
			if(words != null)
				words.closeMemoryConnection();
		}
		return;
	}
	
	public void printRunTime(long timestamp) {
		Date date = new Date(timestamp);
		DateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
		String dateFormatted = formatter.format(date);
		System.out.println("Futásidő: " + dateFormatted );
	}
	
	public void generate() throws SQLException {
		
		// Ha teljes a racs akkor keszen vagyunk
		if(this.grid.isFull() || end) {
			end = true;
			filledGrid = this.grid;
			return;
		}
		
		ArrayList<Word> words;
		
		// Ha most kezdunk akkor elteroen mukodik az algoritmus 
		if(this.grid.isStart()) {
			// A leghosszab kivalasztasa
			Column longest = this.grid.getLongest();
			
			// A beleillo szavak kivalasztasa
			words = getBestsWord(longest);
			
			// Vegigiteralunk rajtuk
			for (int i = 0; i < words.size(); i++) {
				this.grid.setWorToColumn(words.get(i), longest);
				tryCounter++;
				GUI.refresh(this.grid);
				generate();
				if(end) {
					words = null;
					longest = null;
					return;
				}
				this.grid.clearColumn(words.get(i), longest);				
				GUI.refresh(this.grid);
			}
			
			words = null;
			longest = null;
			return;
		}
		
		// Kivalasztjuk a megfelelo oszlopot
		Column bestColumn = getBestColumn();
		
		// Lekerjuk a beleillesztheto szavakat
		words = getBestsWord(bestColumn);
//		words = getBestsWordWithLookAhead(bestColumn);
		
		for (int i = 0; i < words.size(); i++) {
			if( !this.grid.isUsedWord(words.get(i)) ) {

				this.grid.setWorToColumn(words.get(i), bestColumn);
				tryCounter++;
				if(isNotFillable(bestColumn)) {
					this.grid.clearColumn(words.get(i), bestColumn);
					continue;
				}
				GUI.refresh(this.grid);
				generate();
				if(end) {
					words = null;
					bestColumn = null;
					return;
				}
				this.grid.clearColumn(words.get(i), bestColumn);
				
				GUI.refresh(this.grid);
			}	
		}
		
		words = null;
		bestColumn = null;
		return;
		
	}
	
	/**
	 * Veletlen random boolenan generator
	 * @return veletlen igaz vagy hamis ertek
	 */
	public boolean getRandomBoolean() {
	  return Math.random() < 0.5;
	}
	
	/*
	 * Kiválasztjuk a legjobb oszlopot a beszurashoz.
	 * A mar megkezdettek kozul azt, amire a leheto legkevesebb kitöltés létezik.
	 */
	private Column getBestColumn() throws SQLException {
//		long startTime = System.currentTimeMillis();
//		long stopTime = System.currentTimeMillis();
//		System.out.println((stopTime - startTime));
		Column c = null;
		int minNumberOfWords = 0;
		int numberOfWords;
		// Az kell amiben a legkevesebb szabad hely van, de nem 0-a.
		for (int i = 0; i < this.grid.columns.size(); i++) {
			if( !this.grid.columns.get(i).isFilled() && this.grid.columns.get(i).isStarted()) { // ki van-e mar toltve?
				
				if(c == null) {
					c = this.grid.columns.get(i);
					minNumberOfWords = words.getWordCountByColumn(this.grid.columns.get(i));
				}
								
				numberOfWords = words.getWordCountByColumn(this.grid.columns.get(i));
				if( (numberOfWords < minNumberOfWords || (numberOfWords == minNumberOfWords/* && getRandomBoolean()*/))) {
					c = this.grid.columns.get(i);
					minNumberOfWords = numberOfWords;
				}
			}
		}
		
		return c;
	}
	
	private ArrayList<Word> getBestsWord(Column bestColumn) throws SQLException {
		return words.getWordsByColumn(bestColumn);
	}
	
	private ArrayList<Word> getBestsWordWithLookAhead(Column bestColumn) throws SQLException {
		ArrayList<Word> w = words.getWordsByColumn(bestColumn); 
		Column c = null;
		
		for (int i = 0; i < w.size(); i++) {
			this.grid.setWorToColumn(w.get(i), bestColumn);
			if( !this.grid.isFull() ) {
				c = getBestColumn();
				w.get(i).setLookAhead(words.getWordCountByColumn(c));
			}
			this.grid.clearColumn(w.get(i), bestColumn);
		}
				
		Collections.sort(w, new WordComparator());
		
		return w;
	}
	
	/**
	 * Megvizsgalja, hogy kitolheto-e a racs egy adott lepes utan
	 * @return igaz vagy hamis ertek
	 * @throws SQLException 
	 */
	private boolean isNotFillable(Column c) throws SQLException {
		int[] pair = new int[2];
		int x,y;
		int id = this.grid.getColumnId(c);

		for (int i = 0; i < c.getLength(); i++) {
			x = c.getStartX();
			y = c.getStartY();
			for (int j = 0; j < this.grid.gridMatrix[x][y].size(); j++) {
				pair = this.grid.gridMatrix[x][y].get(j);
				if(pair[0] != id && !words.isFillable(this.grid.columns.get(i))) {
					return true;	
				}
			}

			if(c.isVertical()) {
				x++;
			} else {
				y++;
			}
		}

//		for (int i = 0; i < this.grid.columns.size(); i++) {
//			if( this.grid.columns.get(i).isStarted() && !words.isFillable(this.grid.columns.get(i)) ) {
//
//				return true;
//			}
//		}
		return false;
	}
	
}
