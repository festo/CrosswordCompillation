import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class WordsDAO {
	private int[] lengthStat;
	private static final String dbfile = "database/dictonaries.db";
	private static String dbtable = "hun_eng";
	private Connection connection = null;
	private Connection memoryConnection = null;
	
	private static final String SQL_createTable = "CREATE TABLE words (" +
														"id integer primary key, " +
														"answer varchar(19), " +
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
	private static final String SQL_addIndexes = "CREATE INDEX word_chars_indexes on words(length, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c13, c14, c15, c16, c17, c18, c19); ";
	private static final String SQL_clearMemory = "DELETE * FROM words";
	private static final String SQL_selectFromDatabase = "SELECT * FROM "+dbtable+" WHERE length = ? ORDER BY RANDOM() LIMIT "+Settings.MAX_WORD_COUNT;
	
	public WordsDAO() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		createMemoryTable();
	}
	
	private void connectToDatabase() throws SQLException {
		if (connection == null || connection.isClosed())
			connection = DriverManager.getConnection("jdbc:sqlite:"+dbfile);
	}
	
	private void connectToMemory() throws SQLException {
		if (memoryConnection == null || memoryConnection.isClosed())
			memoryConnection = DriverManager.getConnection("jdbc:sqlite:");
//			memoryConnection = DriverManager.getConnection("jdbc:sqlite::memory:");
	}
	
	public void createMemoryTable() {
		try {
			connectToMemory();
			
			Statement statement = memoryConnection.createStatement();
			statement.executeUpdate(SQL_createTable);
			statement.executeUpdate(SQL_addIndexes);
		    
		} catch(SQLException e) {
		      System.err.println(e.getMessage());
		} finally {
//			try {
//				if(memoryConnection != null)
//					memoryConnection.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
		}
		
	}
	
	public void clearMemory() {
		try {
			
			connectToMemory();
			
			Statement statement = memoryConnection.createStatement();
			statement.executeUpdate(SQL_clearMemory);
			
		} catch(SQLException e) {
		      System.err.println(e.getMessage());
		} finally {
			try {
				if(memoryConnection != null)
					memoryConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<Word> selectFromDatabase(int length) {
		PreparedStatement pst = null;
		ArrayList<Word> words = new ArrayList<Word>();
		try {
			connectToDatabase();
			pst = connection.prepareStatement(SQL_selectFromDatabase);
			int index = 1;
			pst.setInt(index++, length);
			ResultSet rs = pst.executeQuery();
			int id = 0;
			String answer = null;
			while(rs.next()) {
				id = rs.getInt("id");
				answer = rs.getString("answer");
				Word w = new Word(id, answer);
				words.add(w);
		    }
		      
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
	
	public void fillTheMemory() {
		String SQL_insertIntoMemory = null;
		int j;
		try {
			connectToMemory();
//			"insert into words (id, answer, length, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c13, c14, c15, c16, c17, c18, c19)";
			Statement statement = memoryConnection.createStatement();
			
			for (int i = 0; i < lengthStat.length; i++) {
				if(lengthStat[i] != 0) {
					ArrayList<Word> words = selectFromDatabase(i);
					
					for (Iterator<Word> iterator = words.iterator(); iterator.hasNext();) {
						Word w = (Word) iterator.next();

						SQL_insertIntoMemory = "insert into words (id, answer, length, ";
						for (j = 1; j < w.getLength(); j++) {
							SQL_insertIntoMemory += "c"+j+", ";
						}
//						j++;
						SQL_insertIntoMemory += "c"+j+") VALUES ( ";
						
						SQL_insertIntoMemory += w.getId() + ", ";
						SQL_insertIntoMemory += "'" + w.getAnswer() + "', ";
						SQL_insertIntoMemory += w.getLength() + ", ";
						
						for (j = 0; j < w.getLength()-1; j++) {
							SQL_insertIntoMemory += "'" + w.getChar(j) + "', ";
						}
						
//						j++;
						SQL_insertIntoMemory += "'" + w.getChar(j) + "' ";

						SQL_insertIntoMemory += ")";
						statement.executeUpdate(SQL_insertIntoMemory);

					}
					
				}
			}			
		      
		} catch(SQLException e) {
		      System.err.println(e.getMessage());
		} finally {
//			try {
//				if(connection != null)
//					connection.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
		}
	}
	
	public Word getWordByColumn(Column c) {
		Word w = null;
		String SQL;
		try {
			connectToMemory();
	
			Statement statement = memoryConnection.createStatement();
			
			SQL = "select * from words where ";
			
			SQL += c.getSQL()+" ";
			
			for (int i = c.getLength(); i < Settings.MAX_WORD_LENGTH; i++) {
				SQL += "and c"+ (i+1) +" is NULL ";
			}
			
			SQL += "ORDER BY RANDOM() LIMIT 1";
			
//			System.out.println(SQL);
			
			ResultSet rs = statement.executeQuery(SQL);
			
			int id = 0;
			String answer = null;
			while(rs.next()) {
				id = rs.getInt("id");
				answer = rs.getString("answer");
				w = new Word(id, answer);
		    }
			
			System.out.println(w);
			
			
		} catch(SQLException e) {
		      System.err.println(e.getMessage());
		}
		return w;
	}
	
	public int getWordCountByColumn(Column c) {
		int count = 0;
		String SQL;
		try {
			connectToMemory();
	
			Statement statement = memoryConnection.createStatement();
			
			SQL = "select count(*) as db from words where ";
			
			SQL += c.getSQL();
			
			ResultSet rs = statement.executeQuery(SQL);
			
			count = rs.getInt("db");
			
		} catch(SQLException e) {
		      System.err.println(e.getMessage());
		}
		return count;
	}
	
	public void setLengthStat(int[] ls) {
		lengthStat = ls;
	}
}
