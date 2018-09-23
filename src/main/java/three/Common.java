package three;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class Common extends JFrame {
public static void main(String[] args) {
    new Common();
}

DataInputStream cin;
DataOutputStream cout;

TalkPanel talkPanel;
BoardPanel boardPanel;
JToolBar toolBar;

final static byte BLACK = 1;
final static byte WHITE = 2;

byte myColor;// The chess color of this client
Stack<Point> book = new Stack<Point>();
byte[][] board = new byte[15][15];
File[] woodBoardImages = new File("woodBoard").listFiles();
int woodBoardImagesIndex = 0;

JToolBar getToolBar() {
    JToolBar toolBar = new JToolBar();

    JButton regret = new JButton("regret");
    regret.addActionListener(listenRegret);
    toolBar.add(regret);

    JButton prevBoardButton = new JButton("prev board");
    prevBoardButton.addActionListener(listenChangeBoard);
    toolBar.add(prevBoardButton);

    JButton nextBoardButton = new JButton("next board");
    nextBoardButton.addActionListener(listenChangeBoard);
    toolBar.add(nextBoardButton);

    return toolBar;
}

Common() {
    setSize(880, 750);
    Dimension size = getToolkit().getScreenSize();
    setLocation(size.width / 2 - getWidth() / 2, size.height / 2 - getHeight() / 2);

    setLayout(new BorderLayout());

    boardPanel = new BoardPanel(this);
    listenChangeBoard.actionPerformed(new ActionEvent(this, 0, ""));
    talkPanel = new TalkPanel(this);
    toolBar = getToolBar();
    add(talkPanel, BorderLayout.EAST);
    add(boardPanel, BorderLayout.CENTER);
    add(toolBar, BorderLayout.NORTH);


    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
}

// check whether it's my turn
boolean myTurn() {
    byte who;
    if (book.size() % 2 == 0)
        who = BLACK;
    else
        who = WHITE;
    return who == myColor;
}

// the mainloop of the communication
Runnable run = new Runnable() {
    public void run() {
        try {
            while (true) {
                System.out.println("weidiao");
                byte o = cin.readByte();
                dispatch(o);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Common.this, "connection failed !");
            System.exit(-1);
        }
    }

    void dispatch(byte o) throws Exception {
        switch (o) {
            case 0: // he put one chess
                byte x = cin.readByte(), y = cin.readByte();
                putChess(x, y, 3 - myColor);
                boardPanel.repaint();
                if (checkWin(x, y)) {
                    win(3 - myColor);
                }
                break;
            case 1:// talk
                byte length = cin.readByte();
                byte[] words = new byte[length];
                cin.read(words);
                talkPanel.talked.append("他说:" + new String(words) + "\n");
                break;
            case 2:// regret
                int ans = JOptionPane.showConfirmDialog(Common.this, " Do you allow him to regret ?", "regret",
                        JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.OK_OPTION) {
                    cout.writeByte(3);
                    if (myTurn()) {
                        popChess();
                    } else {
                        popChess();
                        popChess();
                    }
                    boardPanel.repaint();
                } else
                    cout.writeByte(4);
                break;
            case 3:// he allow me to regret
                talkPanel.talked.append("\nHe allows you to regret\n");
                if (myTurn()) {
                    popChess();
                    popChess();
                } else {
                    popChess();
                }
                boardPanel.repaint();
                break;
            case 4:// he don't allow me to regret
                talkPanel.talked.append("\n He rejected you to regret !\n");
                break;
            default:
                JOptionPane.showMessageDialog(Common.this, "error communication");
                System.exit(-1);
        }
    }
};

void anotherGame() {
    book.clear();
    board = new byte[15][15];
    boardPanel.repaint();
}

void popChess() {
    if (book.isEmpty()) return;
    Point p = book.pop();
    board[p.x][p.y] = 0;
}

public void putChess(int x, int y, int color) {
    book.push(new Point(x, y));
    board[x][y] = (byte) color;
}

void win(int who) {
    int result = JOptionPane.showConfirmDialog(this, "you " + (who == myColor ? "win" : "lose") + "\n Do you want another game?", "game result",
            JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.CANCEL_OPTION) {
        System.exit(0);
    } else {
        anotherGame();
    }
}

boolean checkWin(int x, int y) {
    byte[][] chessBoard = board;
    int[][] dir = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
    int sum = 1;
    for (int i = 0; i < dir.length; i++) {
        for (int m = x + dir[i][0], n = y + dir[i][1]; m >= 0 && n >= 0 && m < 15
                && n < 15; m += dir[i][0], n += dir[i][1]) {
            if (chessBoard[x][y] == chessBoard[m][n])
                sum++;
            else
                break;
        }
        for (int m = x - dir[i][0], n = y - dir[i][1]; m >= 0 && n >= 0 && m < 15
                && n < 15; m -= dir[i][0], n -= dir[i][1]) {
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

ActionListener listenRegret = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            cout.writeByte(2);
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(Common.this, "exception detected");
            e1.printStackTrace();
            System.exit(-1);
        }
    }
};
ActionListener listenChangeBoard = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        woodBoardImagesIndex = (woodBoardImagesIndex + woodBoardImages.length
                + (e.getActionCommand().equals("next board") ? 1 : -1)) % woodBoardImages.length;
        try {
            boardPanel.backImg = ImageIO.read(woodBoardImages[woodBoardImagesIndex]);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        boardPanel.repaint();
    }
};
}