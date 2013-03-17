import java.io.IOException;
import java.sql.SQLException;
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
						
			generate(grid);
		} catch(SQLException e) {
		      System.err.println(e.getMessage());
		} finally {
			words.closeMemoryConnection();
		}
		System.out.println("-- VÉGE --");
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
				g.clearColumn(words.get(i), longest);				
				GUI.refresh(g);
			}
			
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
				g.clearColumn(words.get(i), bestColumn);
				
				GUI.refresh(g);
			}	
		}
		
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
		for (int i = 1; i < g.columns.size(); i++) {
			if( !g.columns.get(i).isFilled() && g.columns.get(i).isStarted()) { // ki van-e mar toltve?
				
				if(c == null) {
					c = g.columns.get(i);
					minNumberOfWords = words.getWordCountByColumn(g.columns.get(i));
				}
				
				numberOfWords = words.getWordCountByColumn(g.columns.get(i));
				if( (numberOfWords < minNumberOfWords || (numberOfWords == minNumberOfWords && getRandomBoolean()))) {
//				if(g.columns.get(i).getFilledSpaaces() >= c.getFilledSpaaces() || numberOfWords <= minNumberOfWords) {
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
			if( !words.isFillable(g.columns.get(i)) ) {
				return true;
			}
		}
		return false;
	}
	
}
