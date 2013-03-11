import java.io.IOException;


public class Core {
	
	private Grid grid;
	private WordsDAO words;

	public Core() throws IOException {
		grid = new Grid();
		grid.init("src/grids/grid1.txt");
		
		GUI.createAndShowGUI(grid);
		
		words = new WordsDAO();
		words.setLengthStat(grid.getlengthStat());
		words.fillTheMemory();
		
		firstStep();
		
		grid = generate(grid);
	}
	
	/**
	 * Az elso lepesben egy veletlenul kivalasztott rekordot toltunk ki
	 */
	public void firstStep() {
		// A leghosszab kivalasztasa
		Column longest = grid.getLongest();
		
		// Egy megfelelo szo kereses
		Word word = words.getWordByColumn(longest);
		
		// A racsaba beillesztese
		grid.setWorToColumn(word, longest);
		
	}
	
	public Grid generate(Grid g) {
		Grid base = g;
		
		// Kivalasztjuk a megfelelo oszlopot
		Column bestColumn = getBestColumn(g);
		Word bestWord = getBestWord(bestColumn);
		System.out.println("Best column: "+bestColumn);
		System.out.println("Best word: "+bestWord);
		
		if(bestWord != null) {
			grid.setWorToColumn(bestWord, bestColumn);
			return generate(g);
		} else {
			return base;
		}
		
	}
	
	/*
	 * Kiválasztjuk a legjobb oszlopot a beszurashoz.
	 * Azt, amire a lehető legkevesebb kitöltés létezik.
	 */
	private Column getBestColumn(Grid g) {
		Column c = new Column();
		int minNumberOfWords = Settings.MAX_WORD_COUNT;
		int numberOfWords;
		// Az kell amiben a legkevesebb szabad hely van, de nem 0-a.
		for (int i = 0; i < g.columns.size(); i++) {
			if(g.columns.get(i).getFreeSpaces() != 0) {
				numberOfWords = words.getWordCountByColumn(g.columns.get(i));
				if(g.columns.get(i).getFilledSpaaces() > c.getFilledSpaaces() || numberOfWords < minNumberOfWords) {
					c = g.columns.get(i);
//					System.out.println(numberOfWords + " < " + minNumberOfWords);
					numberOfWords = minNumberOfWords;
				}
			}
		}
		
		return c;
	}
	
	//TODO: Ez nem a heurisztikának megfelelő
	private Word getBestWord(Column c) {
		return words.getWordByColumn(c);
	}
	
}
