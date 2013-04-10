import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class MainGUI extends JFrame implements ActionListener  {
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private static JPanel table;
	private static int SIZE = 2;
		
	/** A tabla szelessege */
	public static final int WIDTH = 600;
	/** A tabla magassaga */
	public static final int HEIGHT = 600;
	/** Az oldalso panel szelessege */
	public static final int PANEL = 200;
	
	public static void createAndShowGUI() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
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
	public MainGUI() {
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
        
        frame.setTitle("Keresztrejtvény generátor");
        frame.setLocation(Math.max(0, (screenSize.width  - windowSize.width ) /2), Math.max(0, (screenSize.height - windowSize.height) /2));    


	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		table = new JPanel();
	}
	
	public void makeTable(){
		JButton button;
		int buttonSize = 200;
		ImageIcon img;
		
		button = new JButton("5x5");
		button.setPreferredSize(new Dimension(buttonSize,buttonSize));
		button.setMinimumSize(new Dimension(buttonSize,buttonSize));
		button.setHorizontalTextPosition(AbstractButton.CENTER);
		button.setVerticalTextPosition(AbstractButton.BOTTOM);
		img = new ImageIcon("resources/grid1.png");
	    button.setIcon(img);
		table.add(button);
		
		button = new JButton("5x5");
		button.setPreferredSize(new Dimension(buttonSize,buttonSize));
		button.setMinimumSize(new Dimension(buttonSize,buttonSize));
		button.setHorizontalTextPosition(AbstractButton.CENTER);
		button.setVerticalTextPosition(AbstractButton.BOTTOM);
		img = new ImageIcon("resources/grid2.png");
	    button.setIcon(img);
		table.add(button);
		
		button = new JButton("9x9");
		button.setPreferredSize(new Dimension(buttonSize,buttonSize));
		button.setMinimumSize(new Dimension(buttonSize,buttonSize));
		button.setHorizontalTextPosition(AbstractButton.CENTER);
		button.setVerticalTextPosition(AbstractButton.BOTTOM);
		img = new ImageIcon("resources/grid3.png");
	    button.setIcon(img);
		table.add(button);
		
		button = new JButton("11x11");
		button.setPreferredSize(new Dimension(buttonSize,buttonSize));
		button.setMinimumSize(new Dimension(buttonSize,buttonSize));
		button.setHorizontalTextPosition(AbstractButton.CENTER);
		button.setVerticalTextPosition(AbstractButton.BOTTOM);
		img = new ImageIcon("resources/grid4.png");
	    button.setIcon(img);
		table.add(button);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
//		this.refresh();
	}

}
