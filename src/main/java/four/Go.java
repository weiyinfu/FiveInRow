package four;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Go extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();

			HttpSession session = request.getSession();
			Game g = Data.playing.get(session.getId());
			if (g == null) {
				out.print("not ready");
				return;
			}
			String type = request.getParameter("type");
			int who = (session == g.session[0]) ? 0 : 1;
			if (type.equals("listen")) {
				if (g.ti[who] < g.talk.size()) {
					out.print("talk");
					response.setHeader("say", g.talk.get(g.ti[who]++));
				} else if (g.gameState.equals("admitLose")) { 
					if (g.winner == who) {
						out.print("win");
						Data.playing.remove(session.getId());
					} else {
						out.print("lose");
						Data.playing.remove(session.getId());
					}
				} else if (g.gameState.equals("over")) {
					if (g.winner == who) {
						out.print("win");
						Data.playing.remove(session.getId());
					} else if (g.winner == 1 - who) {
						out.print("lose");
						Point p = g.book.get(g.book.size() - 1);
						response.setIntHeader("x", p.x);
						response.setIntHeader("y", p.y);
						Data.playing.remove(session.getId());
					} else {
						out.print("peace");
						Data.playing.remove(session.getId());
					}
				} else if (g.gameState.equals("running")) {
					if (g.now == who) {
						if (g.gotit == false) {
							out.print("put");
							Point p = g.book.get(g.book.size() - 1);
							response.setIntHeader("x", p.x);
							response.setIntHeader("y", p.y);
							g.gotit = true;
						} else {
							out.print("yourTurn");
						}
					}
				}
			} else if (type.equals("put")) {
				if (g.gameState.equals("running")) {
					if (g.now == who) {
						byte x = Byte.parseByte(request.getParameter("x"));
						byte y = Byte.parseByte(request.getParameter("y"));
						if (x < 0 || y < 0 || x >= 15 || y >= 15) {
							throw new Exception("x<0||y<0||x>=15||y>=15");
						}
						if (g.canPut(x, y)) {
							g.book.add(new Point(x, y));
							out.print("canPut");
							response.setIntHeader("x", x);
							response.setIntHeader("y", y);
							if (g.checkWin(x, y)) {
								g.winner = who;
								g.gameState = "over";
								g.over();
							} else if (g.checkPeace()) {
								g.gameState = "over";
								g.winner = -1;
								g.over();
							} else {
								g.gotit = false;
								g.now = 1 - who;
							}
						}
					}
				}
			} else if (type.equals("talk")) {
				String s = new String(request.getParameter("say").getBytes("iso-8859-1"), "utf-8");
				String name = ((Player) session.getAttribute("user")).name;
				g.talk.add(name + ":" + s);
			} else if (type.equals("admitLose")) {
				g.gameState = "admitLose";
				g.winner = 1 - who;
				g.over();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void cout(String s) {
		System.out.println(s);
	}
}