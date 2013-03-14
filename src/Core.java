import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class Core {
	
	private Grid grid;
	private WordsDAO words;
	private boolean end = false;

	public Core() throws IOException {
		try {
			grid = new Grid();
	//		grid.init("src/grids/grid1.txt");
			grid.init("src/grids/sample.txt");
			
			GUI.createAndShowGUI(grid);
			
			words = new WordsDAO();
			words.setLengthStat(grid.getlengthStat());
			words.fillTheMemory();
			
//			firstStep();
			
			grid = generate(grid);
		} catch(SQLException e) {
		      System.err.println(e.getMessage());
		} finally {
			words.closeMemoryConnection();
		}
		System.out.println("-- VÉGE --");
	}
	
	/**
	 * Az elso lepesben egy veletlenul kivalasztott rekordot toltunk ki
	 * @throws SQLException 
	 */
	public void firstStep() throws SQLException {
		// A leghosszab kivalasztasa
		Column longest = grid.getLongest();
		
		// Egy megfelelo szo kereses
		Word word = words.getWordByColumn(longest);
		
		// A racsaba beillesztese
		grid.setWorToColumn(word, longest);
		
	}
	
	public Grid generate(Grid g) throws SQLException {
		if(g.isFull() || end) {
			end = true;
			return g;
		}
		
		// Kivalasztjuk a megfelelo oszlopot
		Column bestColumn = getBestColumn(g);
		
		if(bestColumn.getLength() == 0) {
			return g;
		}
		
		ArrayList<Word> words = getBestsWord(bestColumn);
		System.out.println("Selected column: "+bestColumn);
		System.out.println("Words count: "+words.size());
		
		for (int i = 0; i < words.size(); i++) {
			System.out.println(g.usedWordsList());
			if( !g.isUsedWord(words.get(i)) ) {
				System.out.println("Beszúrva: "+words.get(i));

				g.setWorToColumn(words.get(i), bestColumn);
				GUI.refresh(g);
				System.out.println(g);
				
				generate(g);
				g.clearColumn(words.get(i), bestColumn);
				System.out.println("Törölve: "+words.get(i));
				
				GUI.refresh(g);
				System.out.println(g);
				// Amit beletettunk azt vegyuk is ki valahogy ...
			}	
		}
		
		System.out.println("-- Lecsorog --");
		return g;
		
	}
	
	/*
	 * Kiválasztjuk a legjobb oszlopot a beszurashoz.
	 * Azt, amire a lehető legkevesebb kitöltés létezik.
	 */
	private Column getBestColumn(Grid g) throws SQLException {
		Column c = new Column();
		int minNumberOfWords = Settings.MAX_WORD_COUNT;
		int numberOfWords;
		// Az kell amiben a legkevesebb szabad hely van, de nem 0-a.
		for (int i = 0; i < g.columns.size(); i++) {
			if( !g.columns.get(i).isFilled() ) {
				numberOfWords = words.getWordCountByColumn(g.columns.get(i));
				if(g.columns.get(i).getFilledSpaaces() >= c.getFilledSpaaces() || numberOfWords <= minNumberOfWords) {
					c = g.columns.get(i);
//					System.out.println(numberOfWords + " < " + minNumberOfWords);
					numberOfWords = minNumberOfWords;
				}
			}
		}
		return c;
	}
	
	//TODO: Ez nem a heurisztikának megfelelő
	private ArrayList<Word> getBestsWord(Column c) throws SQLException {
		return words.getWordsByColumn(c);
	}
	
}
