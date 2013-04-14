import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.lowagie.text.DocumentException;

public class GUI extends JFrame implements ActionListener  {
	
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static JPanel table;
	private static JButton[][] gridButtons;
	private static int SIZE = 0;
	private static Grid grid;
	
	private static int[][] indexes;
	
	/** A tabla szelessege */
	public static final int WIDTH = 600;
	/** A tabla magassaga */
	public static final int HEIGHT = 600;
	
	public static void createAndShowGUI(Grid g) {
		grid = g;
		SIZE = g.getHeight();
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
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.pack();		//megfelelo meretet allitunk az ablaknak
        frame.setVisible(true);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	//lekerjuk a tajolashoz a monitor meretet
        Dimension windowSize = frame.getSize();
        
        frame.setTitle("Generálás ...");
        frame.setLocation(Math.max(0, (screenSize.width  - windowSize.width ) /2), Math.max(0, (screenSize.height - windowSize.height) /2));
        
        frame.addWindowListener(new WindowAdapter() { 
        	Object[] options = {"Igen", "Nem"};
            @Override
            public void windowClosing(WindowEvent e) {
            	int n = JOptionPane.showOptionDialog(frame, "Biztos bezárod ?", "Bezárás ?", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null, options, options[0]); 
                if(n == JOptionPane.OK_OPTION){
                    frame.setVisible(false);
                    MainGUI.stopGenerate();
                    return;
                }
            }
        });


	}
	
	public static void end(Grid g, long timestamp) {
		Object[] options = {"Igen", "Nem"};
		Date date = new Date(timestamp);
		DateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
		String dateFormatted = formatter.format(date);
     	int n = JOptionPane.showOptionDialog(frame, "A Generálás kész!\nFutásidő: "+dateFormatted+" (perc)\nEl akarod menteni?", "Kész!", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null, options, options[0]); 
        if(n != JOptionPane.OK_OPTION){
            frame.setVisible(false);
            MainGUI.stopGenerate();
            return;
        } else {
        	try {       		
				PDF pdf = new PDF(g);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		table = new JPanel();
		gridButtons = new JButton[SIZE][SIZE];
		indexes = grid.getIndexes();
		
	}
	
	public void makeTable(){
		int[][] shape = grid.getShape();
		
		Border thickBorder = new LineBorder(Color.BLACK, 1);

      	for(int x=0; x<SIZE; x++){
      		for(int y=0; y<SIZE; y++){
           		gridButtons[x][y]=new JButton(""); //gomb letrehozasa
	
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
		paintCells(grid);
	}
	
	public static void paintCells(Grid g) {
		char chars[][];
		chars = g.getChars();
		String label = "";
		for(int x=0; x<SIZE; x++){
            for(int y=0; y<SIZE; y++){
            	label = "&nbsp;";
            	if(indexes[x][y] != 0) {
            		label += ""+indexes[x][y];
            	}
            	gridButtons[x][y].setText("<html><table style=\"width:25px;height:25px\"><tr style=\"font-size: 6px;\">"+label+"</tr><tr style=\"color: red;\">&nbsp;"+chars[x][y]+"</tr></table></html>");
            }
		}
	}
	
	public static void refresh(Grid g) {
		paintCells(g);
		for(int x=0; x<SIZE; x++){
            for(int y=0; y<SIZE; y++){
            	gridButtons[x][y].revalidate();
            }
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}
