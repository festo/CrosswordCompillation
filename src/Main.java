import java.io.IOException;

/**
 * @author Munkácsy Gergely
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	public static void main(String[] args) throws IOException {
		int type = 3;
		
		Core c = new Core();
		
		if(type == 1 ) {
			c.setGrid(1);
		} else if (type == 2) {
			c.setGrid(2);
		} else if (type == 3) {
			c.setGrid(3);
		} else if (type == 4) {
			c.setGrid(4);
		}
		
		c.start();
	}

}
