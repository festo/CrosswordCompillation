import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A generálásért felelős osztály, az egész magja
 * @author Munkácsy Gergely
 *
 */
public class Core {
	
	private Grid grid; // a grid
	private WordsDAO words; // A szevek elérését biztosító objektum
	private boolean end = false; // Befejezte-e a generálást?
	private int tryCounter = 0; // beszúrások száma
	private int gridId = 1; // A Grid azonsoítója
	private String lang = "enghun"; // Alapértelmezett nyelv

	/**
	 * Beállítja a rács típusát
	 * @param id
	 */
	public void setGrid(int id) {
		if(id <5 && id > 0) {
			this.gridId = id;
		}
	}
	
	/**
	 * Elkezdi a generálást
	 */
	public void start() {
		try {
			grid = new Grid(); // Új rács
			grid.init("resources/grids/grid"+gridId+".txt"); // Minta beolvasása
//			System.out.println("A rács nehézsége: " + grid.getGridDifficulty());
			GUI.createAndShowGUI(grid); // GUI megjelenítése
			
			words = new WordsDAO(); // Szavak elérése
			words.setLang(this.lang); // Nyelv beállítása
			words.setLengthStat(grid.getlengthStat()); // Statisztika átadása
			words.fillTheMemory(); // Memória inicializálása
			
			long startTime = System.currentTimeMillis(); // Futásidő mérés
			
			generate(); // generálás
			
			long stopTime = System.currentTimeMillis(); // A generálás vége
			// A felhasználó értesítése
			GUI.end(this.grid, (stopTime - startTime), tryCounter, this.grid.getWordsDifficulty());
			
		} catch(SQLException e) {
		      System.err.println(e.getMessage());	
		} catch (IOException e) {
			// TODO: handle exception
		} finally {
			if(words != null)
				words.closeMemoryConnection();
		}	
		
	}
	
	
	public Core() {
	}
	
	/**
	 * A gereálásért felelős osztály back-track algoritmussal
	 * @throws SQLException
	 */
	public void generate() throws SQLException {
		
		// Ha teljes a racs akkor keszen vagyunk
		if(this.grid.isFull() || end) {
			end = true;
			return;
		}
		
		ArrayList<Word> words;
		
		// Ha most kezdunk akkor elteroen mukodik az algoritmus 
		if(this.grid.isStart()) {
			// A leghosszab kivalasztasa
			Column longest = this.grid.getLongest();
			
			// A beleillo szavak kivalasztasa
			words = getBestsWord(longest);
			
			// Vegigiteralunk rajtuk
			for (int i = 0; i < words.size(); i++) {
				this.grid.setWorToColumn(words.get(i), longest); // szó beszúrása
				tryCounter++;
				GUI.refresh(this.grid);
				generate();
				if(end) {
					words = null;
					longest = null;
					return;
				}
				this.grid.clearColumn(words.get(i), longest); // szó törlése				
				GUI.refresh(this.grid);
			}
			
			words = null;
			longest = null;
			return;
		}
		
		// Kivalasztjuk a megfelelo oszlopot
		Column bestColumn = getBestColumn();
		
		// Lekerjuk a beleillesztheto szavakat
		words = getBestsWord(bestColumn);
		
		// használjuk a heurisztikát
		
//		words = getBestsWordWithLookAhead(bestColumn);
		
		// Végigmegyünk a beszúrható szavakon
		for (int i = 0; i < words.size(); i++) {
			// Ha még nem használtuk fel
			if( !this.grid.isUsedWord(words.get(i)) ) {
				
				// beszúrjuk a rácsba
				this.grid.setWorToColumn(words.get(i), bestColumn);
				tryCounter++;
				GUI.refresh(this.grid);
				// megnézzük, hogy elrontotta-e az eddigieket
				if(isNotFillable(bestColumn)) {
					this.grid.clearColumn(words.get(i), bestColumn);
					continue;
				}
				GUI.refresh(this.grid);
				generate(); // rekurzió
				if(end) { // ha vége
					words = null;
					bestColumn = null;
					return;
				}
				// töröljük a beállított szót
				this.grid.clearColumn(words.get(i), bestColumn);
				
				GUI.refresh(this.grid);
			}	
		}
		
		words = null;
		bestColumn = null;
		return;
		
	}
	
	/**
	 * Veletlen random boolenan generator
	 * @return veletlen igaz vagy hamis ertek
	 */
	public boolean getRandomBoolean() {
	  return Math.random() < 0.5;
	}
	
	/*
	 * Kiválasztjuk a legjobb oszlopot a beszurashoz.
	 * A mar megkezdettek kozul azt, amire a leheto legkevesebb kitöltés létezik.
	 */
	private Column getBestColumn() throws SQLException {
		Column c = null;
		int minNumberOfWords = 0;
		int numberOfWords;
		// Az kell amiben a legkevesebb szabad hely van, de nem 0-a.
		for (int i = 0; i < this.grid.columns.size(); i++) {
			if( !this.grid.columns.get(i).isFilled() && this.grid.columns.get(i).isStarted()) { // ki van-e mar toltve?
				
				// biztos legyen kivalasztva valami
				if(c == null) {
					c = this.grid.columns.get(i);
					minNumberOfWords = words.getWordCountByColumn(this.grid.columns.get(i));
				}
								
				numberOfWords = words.getWordCountByColumn(this.grid.columns.get(i));
				if( (numberOfWords < minNumberOfWords || (numberOfWords == minNumberOfWords/* && getRandomBoolean()*/))) {
					c = this.grid.columns.get(i);
					minNumberOfWords = numberOfWords;
				}
			}
		}
		
		return c;
	}
	
	/**
	 * A legjobb szavak listája
	 * @param bestColumn a hasáb ahova keresünk szavakat
	 * @return szavak listája
	 * @throws SQLException
	 */
	private ArrayList<Word> getBestsWord(Column bestColumn) throws SQLException {
		return words.getWordsByColumn(bestColumn);
	}
	
	/**
	 * A legjobb szavak listája egyszeres előretekintéssel, a heurisztika alapja
	 * @param bestColumn
	 * @return szavak listája
	 * @throws SQLException
	 */
	private ArrayList<Word> getBestsWordWithLookAhead(Column bestColumn) throws SQLException {
		ArrayList<Word> w = words.getWordsByColumn(bestColumn); 
		Column c = null;
		
		for (int i = 0; i < w.size(); i++) {
			this.grid.setWorToColumn(w.get(i), bestColumn);
			if( !this.grid.isFull() ) {
				c = getBestColumn();
				w.get(i).setLookAhead(words.getWordCountByColumn(c));
			}
			this.grid.clearColumn(w.get(i), bestColumn);
		}
				
		Collections.sort(w, new WordComparator());
		
		return w;
	}
	
	/**
	 * Megvizsgalja, hogy kitolheto-e a racs egy adott lepes utan
	 * @return igaz vagy hamis ertek
	 * @throws SQLException 
	 */
	private boolean isNotFillable(Column c) throws SQLException {
		int[] pair = new int[2];
		int x,y;
		int id = this.grid.getColumnId(c);

		x = c.getStartX();
		y = c.getStartY();
		
		for (int i = 0; i < c.getLength(); i++) {
			
			for (int j = 0; j < this.grid.gridMatrix[x][y].size(); j++) {
				pair = this.grid.gridMatrix[x][y].get(j);
				if(pair[0] != id && !words.isFillable(this.grid.columns.get(pair[0]))) {
					return true;	
				}
			}

			if(c.isVertical()) {
				x++;
			} else {
				y++;
			}
		}

		return false;
	}

	/**
	 * A nyelv beállítása
	 * @param lang
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	
}
