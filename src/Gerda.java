import java.io.IOException;

/**
 * @author Munkácsy Gergely
 *
 */
public class Gerda {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	private static Grid grid;
	private static WordsDAO words;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		grid = new Grid();
		grid.init("src/grids/grid1.txt");
		
//		grid.setChar(1, 1, 'B');
				
//		grid.debug();
								
		words = new WordsDAO();
		words.setLengthStat(getlengthStat());
		words.fillTheMemory();
		
		firstStep();
		
		GUI.createAndShowGUI();

	}
	
	public static char[][] getChars() {
		return grid.getChars();
	}
	
	public static int[][] getShape() {
		return grid.getShape();
	}
	
	public static int[][] getIndexes() {
		return grid.getIndexes();
	}
	
	public static int[] getlengthStat() {
		return grid.getLenthStat();
	}
	
	/**
	 * Az elso lepesben egy veletlenul kivalasztott rekordot toltunk ki
	 */
	public static void firstStep() {
		// A leghosszab kivalasztasa
		Column longest = grid.getLongest();
		
		// Egy megfelelo szo kereses
		Word word = words.getWordByColumn(longest);
		
		// A racsaba beillesztese
		grid.setWorToColumn(word, longest);
		
	}

}
