import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Core {
	
	private Grid grid;
	private Grid filledGrid;
	private WordsDAO words;
	private boolean end = false;

	public Core() throws IOException {
		try {
			grid = new Grid();
			grid.init("src/grids/grid2.txt");
//			grid.init("src/grids/sample.txt");
			
			GUI.createAndShowGUI(grid);
			
			words = new WordsDAO();
			words.setLengthStat(grid.getlengthStat());
			words.fillTheMemory();
			
			long startTime = System.currentTimeMillis();
			generate(grid);
			long stopTime = System.currentTimeMillis();
			//System.out.println("Futásidő: " + ((stopTime - startTime) / 1000) );
			printRunTime((stopTime - startTime));
		} catch(SQLException e) {
		      System.err.println(e.getMessage());
		} finally {
			words.closeMemoryConnection();
		}
		System.out.println("-- VÉGE --");
	}
	
	public void printRunTime(long timestamp) {
		Date date = new Date(timestamp);
		DateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
		String dateFormatted = formatter.format(date);
		System.out.println("Futásidő: " + dateFormatted );
	}
	
	public void generate(Grid g) throws SQLException {
		
		// Ha teljes a racs akkor keszen vagyunk
		if(g.isFull() || end) {
			end = true;
			filledGrid = g;
			return;
		}
		
		ArrayList<Word> words;
		
		// Ha most kezdunk akkor elteroen mukodik az algoritmus 
		if(g.isStart()) {
			// A leghosszab kivalasztasa
			Column longest = g.getLongest();
			
			// A beleillo szavak kivalasztasa
			words = getBestsWord(longest);
			
			// Vegigiteralunk rajtuk
			for (int i = 0; i < words.size(); i++) {
				g.setWorToColumn(words.get(i), longest);
				GUI.refresh(g);
				generate(g);
				if(end) {
					words = null;
					longest = null;
					return;
				}
				g.clearColumn(words.get(i), longest);				
				GUI.refresh(g);
			}
			
			words = null;
			longest = null;
			return;
		}
		
		
		// Kivalasztjuk a megfelelo oszlopot
		Column bestColumn = getBestColumn(g);
		
		// Lekerjuk a beleillesztheto szavakat
		words = getBestsWord(bestColumn);
		
		for (int i = 0; i < words.size(); i++) {
			if( !g.isUsedWord(words.get(i)) ) {

				g.setWorToColumn(words.get(i), bestColumn);
				if(isNotFillable(g)) {
					g.clearColumn(words.get(i), bestColumn);
					continue;
				}
				GUI.refresh(g);
				generate(g);
				if(end) {
					words = null;
					bestColumn = null;
					return;
				}
				g.clearColumn(words.get(i), bestColumn);
				
				GUI.refresh(g);
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
	private Column getBestColumn(Grid g) throws SQLException {
		Column c = null;
		int minNumberOfWords = 0;
		int numberOfWords;
		// Az kell amiben a legkevesebb szabad hely van, de nem 0-a.
		for (int i = 0; i < g.columns.size(); i++) {
			if( !g.columns.get(i).isFilled() && g.columns.get(i).isStarted()) { // ki van-e mar toltve?
				
				if(c == null) {
					c = g.columns.get(i);
					minNumberOfWords = words.getWordCountByColumn(g.columns.get(i));
				}
				
				numberOfWords = words.getWordCountByColumn(g.columns.get(i));
				if( (numberOfWords < minNumberOfWords || (numberOfWords == minNumberOfWords/* && getRandomBoolean()*/))) {
					c = g.columns.get(i);
					minNumberOfWords = numberOfWords;
				}
			}
		}
		
		return c;
	}
	
	//TODO: Ez nem a heurisztikának megfelelő
	private ArrayList<Word> getBestsWord(Column c) throws SQLException {
		return words.getWordsByColumn(c);
	}
	
	/**
	 * Megviszgalja, hogy kitolheto-e a racs egy adott lepes utan
	 * @return igaz vagy hamis ertek
	 * @throws SQLException 
	 */
	private boolean isNotFillable(Grid g) throws SQLException {
		for (int i = 0; i < g.columns.size(); i++) {
			if( g.columns.get(i).isStarted() && !words.isFillable(g.columns.get(i)) ) {
				return true;
			}
		}
		return false;
	}
	
}
