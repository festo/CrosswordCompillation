import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class MainGUI extends JFrame implements ActionListener  {
	
	private static final long serialVersionUID = 2L;
	private JFrame frame;
	private static JPanel table;
	private static int buttonMatrixSize = 2;
	
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	
	volatile static Thread core;
		
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
		
		table.setLayout(new GridLayout(buttonMatrixSize,buttonMatrixSize));	//letrehozunk egy negyzet alaku tablat
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
		int buttonSize = 200;
		ImageIcon img;
		
		button1 = new JButton("5x5");
		button1.setPreferredSize(new Dimension(buttonSize,buttonSize));
		button1.setMinimumSize(new Dimension(buttonSize,buttonSize));
		button1.setHorizontalTextPosition(AbstractButton.CENTER);
		button1.setVerticalTextPosition(AbstractButton.BOTTOM);
		button1.addActionListener(this);
		img = new ImageIcon("resources/grid1.png");
	    button1.setIcon(img);
		table.add(button1);
		
		button2 = new JButton("5x5");
		button2.setPreferredSize(new Dimension(buttonSize,buttonSize));
		button2.setMinimumSize(new Dimension(buttonSize,buttonSize));
		button2.setHorizontalTextPosition(AbstractButton.CENTER);
		button2.setVerticalTextPosition(AbstractButton.BOTTOM);
		button2.addActionListener(this);
		img = new ImageIcon("resources/grid2.png");
	    button2.setIcon(img);
		table.add(button2);
		
		button3 = new JButton("9x9");
		button3.setPreferredSize(new Dimension(buttonSize,buttonSize));
		button3.setMinimumSize(new Dimension(buttonSize,buttonSize));
		button3.setHorizontalTextPosition(AbstractButton.CENTER);
		button3.setVerticalTextPosition(AbstractButton.BOTTOM);
		button3.addActionListener(this);
		img = new ImageIcon("resources/grid3.png");
	    button3.setIcon(img);
		table.add(button3);
		
		button4 = new JButton("11x11");
		button4.setPreferredSize(new Dimension(buttonSize,buttonSize));
		button4.setMinimumSize(new Dimension(buttonSize,buttonSize));
		button4.setHorizontalTextPosition(AbstractButton.CENTER);
		button4.setVerticalTextPosition(AbstractButton.BOTTOM);
		button4.addActionListener(this);
		img = new ImageIcon("resources/grid4.png");
	    button4.setIcon(img);
		table.add(button4);

	}
	
	@SuppressWarnings("deprecation")
	public static void stopGenerate() {
		core.stop();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		core = new Thread(){
	        public void run(){
				Core c = new Core();
				if(e.getSource() == button1 ) {
					c.setGrid(1);
				} else if (e.getSource() == button2) {
					c.setGrid(2);
				} else if (e.getSource() == button3) {
					c.setGrid(3);
				} else if (e.getSource() == button4) {
					c.setGrid(4);
				}
				c.start();
	        }
	    };  
	    core.start();
	}

}
