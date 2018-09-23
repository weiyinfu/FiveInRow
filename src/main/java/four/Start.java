package four;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L; 
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String type = request.getParameter("type");
			HttpSession session=request.getSession();
			if (type.equals("enter")) { 
				String one = request.getParameter("one"); 
				Game g = Data.waiting.remove(one);
				g.session[1]=session;
				g.gameState = "running";
				g.first = g.now = 0; 
				Data.playing.put(session.getId(), g);
				Data.playing.put(one, g); 
			} else if (type.equals("new")) { 
				Game g = new Game();
				g.session[0] = session;
				Data.waiting.put(session.getId(), g);
			}
			response.sendRedirect("five.html");
		} catch (Exception e) {
			response.sendRedirect("fiveRoom.jsp");
		}
	}
}
