package four;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Player {
	public String name, password, motto;
	public int win, lose, peace;
	public Player(ResultSet r) {
		try {
			name = r.getString(1);
			password = r.getString(2);
			motto = r.getString(3);
			win = r.getInt(4);
			lose = r.getInt(5);
			peace = r.getInt(6);
			System.out.println("���û���½:" + name + "," + password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	String toTable() {
		String[] key = {"��", "ʤ", "��", "��", "��"};
		String[] value = {name, win + "", lose + "", peace + "", motto};
		String s = "";
		s += "<table><tr>";
		for (int i = 0; i < key.length; i++) {
			s += "<td>" + key[i] + "</td>";
		}
		s += "</tr><tr>";
		for (int i = 0; i < value.length; i++) {
			s += "<td>" + value[i] + "</td>";
		}
		s += "</tr></table>";
		System.out.println(s);
		return s;
	}
	void writeBack(String key) {
		try {
			PreparedStatement s = Data.connection
					.prepareStatement("update user set " + key
							+ "=? where name=?");
			s.setString(2, name);
			int x=win;
			if(key.equals("lose"))x=lose;
			else if(key.equals("peace"))x=peace;
			s.setInt(1, x+1);
			s.execute();
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
