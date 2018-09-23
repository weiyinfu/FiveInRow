package three;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/* 640*640 ,15*15 chess board
 * 40*40 every grid
 * */
public class BoardPanel extends JPanel {
public BufferedImage backImg;
Common window;

BoardPanel(Common window) {
    setSize(640, 640);
    addMouseListener(listenMouse);
    addMouseMotionListener(listenMouseMotion);
    this.window = window;
}

Point getPos(Point p) {
    int x = (int) Math.round(p.getX() / 40), y = (int) Math.round(p.getY() / 40);
    if (x <= 15 && y <= 15 && x > 0 && y > 0 && Math.hypot(x * 40 - p.x, y * 40 - p.y) < 15)
        return new Point(x, y);
    return null;
}

MouseMotionListener listenMouseMotion = new MouseMotionAdapter() {
    public void mouseMoved(MouseEvent e) {
        if (!window.myTurn()) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            return;
        }
        Point p = getPos(e.getPoint());
        if (p != null && window.board[p.x - 1][p.y - 1] == 0) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            return;
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
};
MouseListener listenMouse = new MouseAdapter() {
    public void mouseClicked(MouseEvent e) {
        if (!window.myTurn())
            return;
        Point p = getPos(e.getPoint());
        if (p != null & window.board[p.x - 1][p.y - 1] == 0) {
            try {
                window.putChess(p.x - 1, p.y - 1, window.myColor);
                window.cout.writeByte(0);
                window.cout.writeByte(p.x - 1);
                window.cout.writeByte(p.y - 1);
                repaint();
                if (window.checkWin(p.x - 1, p.y - 1)) {
                    window.win(window.myColor);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }
    }
};

public void paint(Graphics gg) {
    BufferedImage bit = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
    Graphics2D g = (Graphics2D) bit.getGraphics();
    g.drawImage(backImg, 0, 0, bit.getWidth(), bit.getHeight(), null);
    // draw lines
    g.setColor(Color.BLACK);
    g.setStroke(new BasicStroke(3));
    for (int i = 0; i < 15; i++) {
        for (int j = 0; j < 15; j++) {
            g.drawLine(40 + i * 40, 40, 40 + i * 40, 600);
            g.drawLine(40, i * 40 + 40, 600, i * 40 + 40);
        }
    }
    // draw center
    g.fillOval(8 * 40 - 7, 8 * 40 - 7, 14, 14);
    g.setStroke(new BasicStroke(4));
    // draw frame
    g.drawRect(30, 30, 580, 580);
    // draw chess
    boolean black = true;
    for (Point p : window.book) {
        if (black)
            g.setColor(Color.black);
        else
            g.setColor(Color.white);
        black = !black;
        g.fillOval(p.x * 40 + 40 - 10, p.y * 40 + 40 - 10, 20, 20);
    }
    // draw the red point
    if (!window.book.empty()) {
        g.setColor(Color.RED);
        Point p = window.book.peek();
        g.fillRect(p.x * 40 + 40 - 6, p.y * 40 + 40 - 6, 12, 12);
    }
    gg.drawImage(bit, 0, 0, null);
}
}
