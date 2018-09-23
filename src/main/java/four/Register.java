package four;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("a new user has registered");
		PreparedStatement insert = null;
		try {
			String user = request.getParameter("user");
			String password = request.getParameter("password");
			String motto = request.getParameter("motto");
			System.out.println(user+"\t"+password+"\t"+motto);
			insert = Data.connection
					.prepareStatement("insert into user values(?,?,?,0,0,0)");
			insert.setString(1, user);
			insert.setString(2, password);
			insert.setString(3, motto);
			insert.execute();
			response.sendRedirect("login.jsp?registered");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("register.jsp?registered");
		}finally{ 
			try {
				insert.close();
			} catch (SQLException e) { 
				e.printStackTrace();
			}
		}
	}
}
