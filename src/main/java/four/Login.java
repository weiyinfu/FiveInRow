package four;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Statement s = null;
		try {
			s = Data.connection.createStatement();
			String user = request.getParameter("user");
			ResultSet r = s.executeQuery("select*from user where name='" + user
					+ "'");
			if (r.next()) {
				if (r.getString(2).equals(request.getParameter("password"))) {
					request.getSession().setAttribute("user", new Player(r));
					response.sendRedirect("fiveRoom.jsp");
				} else {
					response.sendRedirect("login.jsp?logined");
				}
			} else {
				response.sendRedirect("login.jsp?logined");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendRedirect("login.jsp?logined");
		} finally {
			try {
				s.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
