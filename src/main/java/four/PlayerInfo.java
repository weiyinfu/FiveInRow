package four;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class PlayerInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		Game g = Data.playing.get(session.getId());
		if (g == null)
			return;
		System.out.println("g :" + (g == null) + "\n" + " g.session[0] :"
				+ (g.session[0] == null) + "\n");
		String one = ((Player) g.session[0].getAttribute("user")).toTable();
		String two = ((Player) g.session[1].getAttribute("user")).toTable();
		response.setHeader("one", URLEncoder.encode(one, "utf-8"));
		response.setHeader("two", URLEncoder.encode(two, "utf-8"));
	}
}
