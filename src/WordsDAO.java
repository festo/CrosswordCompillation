import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Adatbázis elérést biztosító osztály
 * @author Munkácsy Gergely
 *
 */
public class WordsDAO {
	private int[] lengthStat;
	private String dblang = "enghun"; // alapértelmezett nyelv
	private static String dbtable = "words"; // a tábla
	private Connection connection = null; // A fájl elérése
	private Connection memoryConnection = null; // A memóriában lévő szavak elérése
	
	// A memóriában lévő adatbázis elkészítése
	private static final String SQL_createTable = "CREATE TABLE words (" +
														"id integer primary key, " +
														"answer varchar(19), " +
														"clue varchar(255)," +
														"frequency integer, " +
														"length integer, " +		
														"c1 char, " +
														"c2 char, " +
														"c3 char, " +
														"c4 char, " +
														"c5 char, " +
														"c6 char, " +
														"c7 char, " +
														"c8 char, " +
														"c9 char, " +
														"c10 char, " +
														"c11 char, " +
														"c12 char, " +
														"c13 char, " +
														"c14 char, " +
														"c15 char, " +
														"c16 char, " +
														"c17 char, " +
														"c18 char, " +
														"c19 char); ";	
	private static final String SQL_clearMemory = "DELETE * FROM words";
	
	/**
	 * JDBC beállítása
	 * @throws SQLException
	 */
	public WordsDAO() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		createMemoryTable();
	}
	
	/**
	 * Kapcsolódik az SQLite adatbázishoz
	 * @throws SQLException
	 */
	private void connectToDatabase() throws SQLException {
		if (connection == null || connection.isClosed())
			connection = DriverManager.getConnection("jdbc:sqlite:database/"+dblang+".db");
	}
	
	/**
	 * Kapcsolódik a memórában lévő adatbázishoz
	 * @throws SQLException
	 */
	private void connectToMemory() throws SQLException {
		if (memoryConnection == null || memoryConnection.isClosed())
			memoryConnection = DriverManager.getConnection("jdbc:sqlite:");
	}
	
	/**
	 * A memóriában létrehozza a táblát és az indexeket
	 * @throws SQLException
	 */
	public void createMemoryTable() throws SQLException {
		String SQL = " on words(length";
		String indexName;
		connectToMemory();
			
		Statement statement = memoryConnection.createStatement();
		statement.executeUpdate(SQL_createTable);
		
		for (int i = 1; i <= 19; i++) {
			indexName = "word_chars_index_"+i;
			SQL += ", c"+i;
			statement.executeUpdate("CREATE INDEX " + indexName + SQL + " ); ");		
		}
		
	}
	
	/**
	 * Törli a memóriából a szavakat ha szükséges
	 * @throws SQLException
	 */
	public void clearMemory() throws SQLException {			
			Statement statement = memoryConnection.createStatement();
			statement.executeUpdate(SQL_clearMemory);
	}
	
	/**
	 * A memóráan lévő szavakról egy statisztika
	 * @throws SQLException
	 */
	private void memoryStat() throws SQLException {
		System.out.println("-- Memory Database Statistics --");
		
		Statement statement = memoryConnection.createStatement();
			
		String SQL = "select length as length, count(id) as db from words group by length;";
		int count = 0;
		int length = 0;
		ResultSet rs = statement.executeQuery(SQL);
		while(rs.next()) {
			length = rs.getInt("length");
			count = rs.getInt("db");
			System.out.println("Length: "+length+" Count: "+count);
	    }
			
		rs.close();
		
		System.out.println("-- End Memory Database Statistics --");
	}
	
	/**
	 * Az SQLite fájlból beolvas kellően sok szót
	 * @param length
	 * @param count
	 * @return
	 */
	private ArrayList<Word> selectFromDatabase(int length, int count) {
		Statement statement = null;
		ArrayList<Word> words = new ArrayList<Word>();
		count *= Settings.MAX_WORD_COUNT;
		String SQL = "SELECT * FROM "+dbtable+" WHERE length = "+length+" GROUP BY answer ORDER BY RANDOM() LIMIT "+count;
		try {
			connectToDatabase();
			statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(SQL);
			int id = 0;
			String answer = null;
			String clue = null;
			int freq = 0;
			while(rs.next()) {
				id = rs.getInt("id");
				freq = rs.getInt("frequency");
				answer = rs.getString("answer");
				clue = rs.getString("clue");
				Word w = new Word(id, answer, clue, freq);
				words.add(w);
		    }
			
			rs.close();
			statement = null;
		      
		} catch(SQLException e) {
		      System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return words;
	}
	
	// A SQLite fájlból olvasott szavakat elmenti a memórába
	public void fillTheMemory() throws SQLException {
		String SQL_insertIntoMemory = null;
		int j;
			
//		"insert into words (id, answer, length, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c13, c14, c15, c16, c17, c18, c19)";
		Statement statement = memoryConnection.createStatement();
		
		for (int i = 0; i < lengthStat.length; i++) {
			if(lengthStat[i] != 0) {
				ArrayList<Word> words = selectFromDatabase(i, lengthStat[i]);
				
				for (Iterator<Word> iterator = words.iterator(); iterator.hasNext();) {
					Word w = (Word) iterator.next();
						SQL_insertIntoMemory = "insert into words (id, answer, clue, frequency, length, ";
					for (j = 1; j < w.getLength(); j++) {
						SQL_insertIntoMemory += "c"+j+", ";
					}

					SQL_insertIntoMemory += "c"+j+") VALUES ( ";
					
					SQL_insertIntoMemory += w.getId() + ", ";
					SQL_insertIntoMemory += "'" + w.getAnswer() + "', ";
					SQL_insertIntoMemory += "'" + StringEscapeUtils.escapeSql(w.getClue()) + "', ";
					SQL_insertIntoMemory += w.getFreq() + ", ";
					SQL_insertIntoMemory += w.getLength() + ", ";
					
					if( w.getLength() < 3 ) {
						w.getLength();
					}
					
					for (j = 0; j < w.getLength()-1; j++) {
						SQL_insertIntoMemory += "'" + w.getChar(j) + "', ";
					}
					
					SQL_insertIntoMemory += "'" + w.getChar(j) + "' ";
						SQL_insertIntoMemory += ")";
					statement.executeUpdate(SQL_insertIntoMemory);
					
					words = null;
				}
				
			}
		}			
		      
		statement = null;
//		memoryStat();
	}
	
	/**
	 * Lezárja a kapcsolatot
	 */
	public void closeMemoryConnection() {
		try {
			if(memoryConnection != null)
				memoryConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * egy, a hasábba beleillő szót ad vissza
	 * @param c hasáb
	 * @return a szó
	 * @throws SQLException
	 */
	public Word getWordByColumn(Column c) throws SQLException {
		Word w = null;
		String SQL;
		// connectToMemory();

		Statement statement = memoryConnection.createStatement();

		SQL = "select * from words where ";

		SQL += c.getSQL() + " ";

		SQL += "AND length = "+c.getLength();

		SQL += "ORDER BY RANDOM() LIMIT 1";

		ResultSet rs = statement.executeQuery(SQL);

		int id = 0;
		String answer = null;
		String clue = null;
		int freq = 0;
		while (rs.next()) {
			id = rs.getInt("id");
			answer = rs.getString("answer");
			clue = rs.getString("clue");
			freq = rs.getInt("frequency");
			w = new Word(id, answer, clue, freq);
		}

		rs.close();
		
		statement = null;
		SQL = null;
		answer = null;
		return w;
	}
	
	/**
	 * A hasábba beleillő szavak listáját adja vissza
	 * @param c a hasáb
	 * @return a szavak listája
	 * @throws SQLException
	 */
	public ArrayList<Word> getWordsByColumn(Column c) throws SQLException {
		Word w = null;
		String SQL;
		ArrayList<Word> words = new ArrayList<Word>();

		Statement statement = memoryConnection.createStatement();

		SQL = "select DISTINCT * from words where ";
		SQL += c.getSQL() + " ";
		SQL += "AND length = "+c.getLength();
		
		SQL += " ORDER BY RANDOM()";
		SQL += " LIMIT 10";

		ResultSet rs = statement.executeQuery(SQL);

		int id = 0;
		String answer = null;
		String clue = null;
		int freq = 0;
		while (rs.next()) {
			id = rs.getInt("id");
			answer = rs.getString("answer");
			clue = rs.getString("clue");
			freq = rs.getInt("frequency");
			w = new Word(id, answer, clue, freq);
			words.add(w);
		}

		rs.close();
		statement = null;
		SQL = null;

		return words;
	}
	
	/**
	 * Megadja, hogy egy hasábba hány szó illeszthető be az adatbázis alapján
	 * @param c adott hasáb
	 * @return beillesztehtő szavak száma
	 * @throws SQLException
	 */
	public int getWordCountByColumn(Column c) throws SQLException {
		
		int count = 0;
		String SQL;
		// connectToMemory();

		Statement statement = memoryConnection.createStatement();

		SQL = "select count(id) as db from words where ";

		SQL += c.getSQL();

		ResultSet rs = statement.executeQuery(SQL);

		count = rs.getInt("db");
		
//		ResultSet rs = statement.executeQuery(SQL);
//		while(rs.next()) {
//			count += 1;
//		}		
		rs.close();
		statement = null;
		SQL = null;
		return count;
	}
	
	/**
	 * Egy hasábhoz van-e szó az adatbázisban
	 * @param c hasáb
	 * @return true/false
	 * @throws SQLException
	 */
	public boolean isFillable(Column c) throws SQLException {
		int count = 0;
		String SQL;

		Statement statement = memoryConnection.createStatement();

		SQL = "select count(*) as db from words where ";

		SQL += c.getSQL();
		
		ResultSet rs = statement.executeQuery(SQL);

		count = rs.getInt("db");
		rs.close();
		statement = null;
		
		return (count > 0);
		
	}
	
	public void setLengthStat(int[] ls) {
		lengthStat = ls;
	}

	public void setLang(String lang) {
		this.dblang = lang;
	}
}
