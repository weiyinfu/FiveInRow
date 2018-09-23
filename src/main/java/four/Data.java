package four;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

public class Data {
	public static HashMap<String, Game> playing = new HashMap<String, Game>();
	public static HashMap<String, Game> waiting = new HashMap<String, Game>();
	static Connection connection;
	static {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/five?user=root&password=weidiao";
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
