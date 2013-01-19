import java.io.*; 
import java.util.ArrayList;

/**
 * @author Munkácsy Gergely
 *
 */
public class Grid {
	
	private int height;
	private int width;
	private ArrayList<Column> columns;
	
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
	
	public void loadGrid(String filename) throws IOException {
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
	
	

}
