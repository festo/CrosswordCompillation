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
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Grid grid = new Grid();
		grid.init("src/grids/grid1.txt");
		
		grid.setChar(0, 0, 'A');
		grid.setChar(1, 1, 'B');
				
		grid.showColumns();

	}

}
