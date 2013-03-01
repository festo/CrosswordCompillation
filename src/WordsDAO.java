import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WordsDAO {
	private int[] lengthStat;
	private static final String dbfile = "dictonaries.db";
	private Connection conn = null;
	private Connection memoryConn = null;
	
	public WordsDAO() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		init();
	}
	
	public void init() {
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbfile);	
		} catch(SQLException e) {
		      // if the error message is "out of memory", 
		      // it probably means no database file is found
		      System.err.println(e.getMessage());
		}
		
		
//		TODO: Adatbázis elkészítése
	}
	
	public void setLengthStat(int[] ls) {
		lengthStat = ls;
	}
}
