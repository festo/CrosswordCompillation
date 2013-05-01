import java.io.IOException;

import com.lowagie.text.DocumentException;

/**
 * @author Munkácsy Gergely
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws DocumentException 
	 */
	
	public static void main(String[] args) throws DocumentException, Exception {
		int type = 2;
		
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
