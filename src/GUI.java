import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class GUI extends JFrame implements ActionListener  {
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private static JPanel table;
	private JPanel panel;
	private JButton refreshButton;
	private static JButton[][] gridButtons;
	private static int SIZE = 11;
	private static Grid grid;
	
	private static int[][] indexes;
	
	/** A tabla szelessege */
	public static final int WIDTH = 600;
	/** A tabla magassaga */
	public static final int HEIGHT = 600;
	/** Az oldalso panel szelessege */
	public static final int PANEL = 200;
	
	public static void createAndShowGUI(Grid g) {
		grid = g;
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
		
		refreshButton.setText("Frissítés");	//helpButton
		refreshButton.addActionListener(this);
		
		panel.setLayout(new BorderLayout());
		panel.add("South",refreshButton);
		panel.setSize(PANEL, HEIGHT);
		
		frame.setLayout(new BorderLayout());
		frame.add("East",panel);
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
		panel = new JPanel();
		gridButtons = new JButton[SIZE][SIZE];
		refreshButton = new JButton();
		indexes = grid.getIndexes();
		
	}
	
	public void makeTable(){
		int[][] shape = grid.getShape();
		
		Border thickBorder = new LineBorder(Color.BLACK, 1);
		
        for(int y=0; y<SIZE; y++){
          	for(int x=0; x<SIZE; x++){
           		gridButtons[x][y]=new JButton(""); //gomb letrehozasa
//            	grid[x][y].setPreferredSize(new Dimension(WIDTH/SIZE,HEIGHT/SIZE));	//beallitjuk a meretet az ablak fuggvenyeben
//            	grid[x][y].setMinimumSize(new Dimension(WIDTH/SIZE,HEIGHT/SIZE));
//            	
            	gridButtons[x][y].setPreferredSize(new Dimension(40,40));	//beallitjuk a meretet az ablak fuggvenyeben
            	gridButtons[x][y].setMinimumSize(new Dimension(40,40));
            	
            	
            	gridButtons[x][y].setBorder(thickBorder);
            	gridButtons[x][y].setFocusPainted(false);
            	
            	gridButtons[x][y].setMargin(new Insets(0, 0, 0, 0));	//margot 0-ra allitjuk
            	
            	if(shape[x][y] == 1) {
            		gridButtons[x][y].setEnabled(true); // Vilagos negyzet
            		gridButtons[x][y].setBackground(Color.WHITE);
            	} else {
            		gridButtons[x][y].setEnabled(false); // Sotet negyzet
            		gridButtons[x][y].setBackground(Color.BLACK);
            	}
            	
            	table.add(gridButtons[x][y]); 	//hozzaadjuk a gombot a halohoz
            }
		}
		paintCells();
	}
	
	public static void paintCells() {
		char chars[][];
		chars = grid.getChars();
		String label;
		for(int x=0; x<SIZE; x++){
            for(int y=0; y<SIZE; y++){
            	label = "";
            	if(indexes[x][y] != 0) {
            		label = ""+indexes[x][y];
            	}
//            	grid[x][y].setText("<html><table style=\"width:25px;height:25px\"><tr style=\"font-size: 8px;\">"+label+"</tr><tr style=\"font-size: 18px;margin:0;padding:0;\">"+chars[x][y]+"</tr></table></html>");
            	gridButtons[x][y].setText("<html><table style=\"width:25px;height:25px\"><tr style=\"font-size: 8px;\">"+label+"</tr><tr style=\"color: red;\">"+chars[x][y]+"</tr></table></html>");
            }
		}
	}
	
	public static void refresh() {
		paintCells();
		for(int x=0; x<SIZE; x++){
            for(int y=0; y<SIZE; y++){
            	gridButtons[x][y].revalidate();
            }
		}
//		table.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.refresh();
	}

}
