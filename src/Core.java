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
//		GUI.refresh();
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
	
}
