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
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		grid = new Grid();
		grid.init("src/grids/grid1.txt");
		
		grid.setChar(0, 0, 'A');
		grid.setChar(1, 1, 'B');
				
//		grid.debug();
						
//		GUI.createAndShowGUI();
		
		WordsDAO words = new WordsDAO();
		words.setLengthStat(getlengthStat());
		words.fillTheMemory();

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

}
