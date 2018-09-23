package four;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

public class Game {
	public HttpSession[] session = new HttpSession[2];
	String gameState = "";
	ArrayList<Point> book = new ArrayList<Point>();
	ArrayList<String> talk = new ArrayList<String>();
	int[] ti = new int[2];
	int first;
	int now;
	int winner;
	boolean gotit = true;
	void over() {
		Player p0 = (Player) session[0].getAttribute("user");
		Player p1 = (Player) session[1].getAttribute("user");
		if (winner == -1) {
			p0.writeBack("peace");
			p1.writeBack("peace");
		} else {
			String res[] = {"win", "lose"};
			p0.writeBack(res[winner]);
			p1.writeBack(res[1 - winner]);
		}
	}
	boolean canPut(byte x, byte y) {
		for (Point p : book)
			if (p.x == x && p.y == y)
				return false;
		return true;
	}
	byte[][] getBoard() {
		byte[][] board = new byte[15][15];
		byte who = 1;
		for (Point p : book) {
			board[p.x][p.y] = who;
			who = (byte) (who % 2 + 1);
		}
		return board;
	}
	boolean checkPeace() {
		return book.size() == 225;
	}
	boolean checkWin(byte x, byte y) {
		byte[][] chessBoard = getBoard();
		int[][] dir = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
		int sum = 1;
		for (int i = 0; i < dir.length; i++) {
			for (int m = x + dir[i][0], n = y + dir[i][1]; m >= 0 && n >= 0
					&& m < 15 && n < 15; m += dir[i][0], n += dir[i][1]) {
				if (chessBoard[x][y] == chessBoard[m][n])
					sum++;
				else
					break;
			}
			for (int m = x - dir[i][0], n = y - dir[i][1]; m >= 0 && n >= 0
					&& m < 15 && n < 15; m -= dir[i][0], n -= dir[i][1]) {
				if (chessBoard[x][y] == chessBoard[m][n])
					sum++;
				else
					break;
			}
			if (sum >= 5)
				return true;
			else
				sum = 1;
		}
		return false;
	}
}
