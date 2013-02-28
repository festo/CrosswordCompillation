import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class GUI extends JFrame {
	
	private JFrame frame;
	private static JPanel table;
	private JButton[][] grid;
	private int SIZE = 11;
	
	private int[][] indexes;
	
	/** A tabla szelessege */
	public static final int WIDTH = 600;
	/** A tabla magassaga */
	public static final int HEIGHT = 600;
	
	public static void createAndShowGUI() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
		
		makeTable();
		
		table.setLayout(new GridLayout(SIZE,SIZE));	//letrehozunk egy negyzet alaku tablat
		table.setSize(WIDTH,HEIGHT);	//beallitjuk a meretet pixelben
		
		frame.setLayout(new BorderLayout());
		frame.add("West",table);
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();		//megfelelo meretet allitunk az ablaknak
        frame.setVisible(true);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	//lekerjuk a tajolashoz a monitor meretet
        Dimension windowSize = frame.getSize();
        
        frame.setTitle("Gerda");
        frame.setLocation(Math.max(0, (screenSize.width  - windowSize.width ) /2), Math.max(0, (screenSize.height - windowSize.height) /2));    


	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		table = new JPanel();
		grid = new JButton[SIZE][SIZE];
		indexes = Gerda.getIndexes();
		
	}
	
	public void makeTable(){
		int[][] shape = Gerda.getShape();
		
		Border thickBorder = new LineBorder(Color.BLACK, 1);
		
        for(int y=0; y<SIZE; y++){
          	for(int x=0; x<SIZE; x++){
           		grid[x][y]=new JButton(""); //gomb letrehozasa
//            	grid[x][y].setPreferredSize(new Dimension(WIDTH/SIZE,HEIGHT/SIZE));	//beallitjuk a meretet az ablak fuggvenyeben
//            	grid[x][y].setMinimumSize(new Dimension(WIDTH/SIZE,HEIGHT/SIZE));
//            	
            	grid[x][y].setPreferredSize(new Dimension(40,40));	//beallitjuk a meretet az ablak fuggvenyeben
            	grid[x][y].setMinimumSize(new Dimension(40,40));
            	
            	
            	grid[x][y].setBorder(thickBorder);
            	grid[x][y].setFocusPainted(false);
            	
            	grid[x][y].setMargin(new Insets(0, 0, 0, 0));	//margot 0-ra allitjuk
            	
            	if(shape[x][y] == 1) {
            		grid[x][y].setEnabled(true); // Vilagos negyzet
            		grid[x][y].setBackground(Color.WHITE);
            	} else {
            		grid[x][y].setEnabled(false); // Sotet negyzet
            		grid[x][y].setBackground(Color.BLACK);
            	}
            	
            	table.add(grid[x][y]); 	//hozzaadjuk a gombot a halohoz
            }
		}
		paintCells();
	}
	
	public void paintCells() {
		char chars[][];
		chars = Gerda.getChars();
		String label;
		for(int x=0; x<SIZE; x++){
            for(int y=0; y<SIZE; y++){
            	label = "";
            	if(indexes[x][y] != 0) {
            		label = ""+indexes[x][y];
            	}
//            	grid[x][y].setText("<html><table style=\"width:25px;height:25px\"><tr style=\"font-size: 8px;\">"+label+"</tr><tr style=\"font-size: 18px;margin:0;padding:0;\">"+chars[x][y]+"</tr></table></html>");
            	grid[x][y].setText("<html><table style=\"width:25px;height:25px\"><tr style=\"font-size: 8px;\">"+label+"</tr><tr>"+chars[x][y]+"</tr></table></html>");
            }
		}
	}
	
	// TODO: Nem jo a frissites
	public static void refresh() {
		table.revalidate();
	}

}
