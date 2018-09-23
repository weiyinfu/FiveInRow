package one;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class PlayChess extends JFrame {// �̳�JFrame֮��playChess����һ������

private Image imageBG;// ����ͼƬ����
private Image imageBlack;// ��ɫ����ͼƬ����
private Image imageWhite;// ��ɫ����ͼƬ����
private Image imageMenu;// �˵�ͼƬ����
private Image imageW;// û��Ц���̶���ͼƬ����
private Image imageX;// ��Ц���̶���ͼƬ����
private Image imageRim;// ��ɫ��ͼƬ����
private Color[][] allChesses = new Color[14][14];// ���е����
private MyCanvas canvas;// �Զ����MyCanvas�ಢ�̳���JPanel
private boolean isGameBegin = false;// ��Ϸ�Ƿ�ʼ
private boolean isGameOver = true;// ��Ϸ�Ƿ����
private Stack stack;// ��ջ����
private Robot robot;// �����˶���
private Thread PlayerThread;// ���ʱ������Ӧ���߳�
private Thread robotThread;// ������ʱ������Ӧ���߳�
private Thread unDoThread;// ����ʱ������Ӧ���߳�
private int unDoTime = 5;// �����ʱ����
private int playerTime = 1800;// ��ҵ���ʱ��
private int robotTime = 1800;// �����˵���ʱ��
private Color whoSmile = null;// ˭Ӯ��˭��ʾ��Ц���̶���ͼƬ����
private boolean isUndo = false;

public PlayChess() {// ���캯��
    this.setCursor(Toolkit.getDefaultToolkit()
            .createCustomCursor(Toolkit.getDefaultToolkit().createImage("res/pointer.png"), new Point(0, 0), ""));
    // Applet.newAudioClip(PlayChess.class.getResource("res/bg.mid")).loop();

    stack = new Stack();
    robot = new Robot();

    imageBG = Toolkit.getDefaultToolkit().getImage("res/bg_game.JPG");// ��ȡͼƬ��Դ

    imageBlack = Toolkit.getDefaultToolkit().getImage("res/black.png");// ��ȡͼƬ��Դ

    imageWhite = Toolkit.getDefaultToolkit().getImage("res/white.png");// ��ȡͼƬ��Դ

    imageMenu = Toolkit.getDefaultToolkit().getImage("res/menu.png");// ��ȡͼƬ��Դ

    imageW = Toolkit.getDefaultToolkit().getImage("res/W.png");// ��ȡͼƬ��Դ

    imageX = Toolkit.getDefaultToolkit().getImage("res/X.gif");// ��ȡͼƬ��Դ

    imageRim = Toolkit.getDefaultToolkit().getImage("res/rim.png");// ��ȡͼƬ��Դ
    this.setSize(700, 550);// ���ô����С
    setWindowCenter();// ���������
    canvas = new MyCanvas();// ����һ��MyCanvas����
    this.getContentPane().add(canvas);// ��canvas������ӵ������

    robotThread = new Thread(new Runnable() {
        public void run() {
            while (true) {
                if (robot.getMyIsPlayingChess() == true && isGameBegin == true && isUndo == false) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    robotTime--;
                    canvas.repaint();
                    if (robotTime == 0) {
                        isGameBegin = false;
                        isGameOver = true;
                        whoSmile = Color.white;
                        canvas.repaint();
                        JOptionPane.showMessageDialog(null, "��Ӯ�ˣ�");
                    }
                }
            }
        }
    });

    PlayerThread = new Thread(new Runnable() {
        public void run() {// ��ִ��start����ʱ���߳�����
            while (true) {// ��ѭ������Ҫ����ʱ���߻�ֹͣ��������������ʱ����߷���Ͳ���
                if (robot.getMyIsPlayingChess() == false && isGameBegin == true && isUndo == false) {
                    try {// ���������û�����������Ϸ�Ѿ���ʼ��ô�ͱ�ʾ�������
                        Thread.sleep(1000);// �߳�˯һ����
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    playerTime--;// ���ʱ���һ����
                    canvas.repaint();// ���ƴ���
                    if (playerTime == 0) {// �����ҵ�ʱ�������ˣ���ʾ�����
                        isGameBegin = false;// ��Ϸû�п�ʼ
                        isGameOver = true;// ��Ϸ����
                        whoSmile = Color.black;// ��ɫЦ
                        canvas.repaint();// ���ƴ���
                        JOptionPane.showMessageDialog(null, "������Ӯ�ˣ�");
                    }
                }
            }
        }
    });
    unDoThread = new Thread(new Runnable() {
        public void run() {// ��ִ��start����ʱ�߳�����
            while (true) {
                if (unDoTime == 0) {// ����ʱ�䵽,��ʼ����
                    unDoTime--;// ���û���ʱ��Ϊ-1
                    if (stack.getStackTop() != null) {// ���ջ����Ϊ�ձ�ʾ�м�¼
                        if (stack.getStackTop().getChessColor() == Color.white) {
                            {// ���Ϊ��ɫ��ʾ�û���������
                                robot.cerebra(allChesses, canvas, Color.black, stack);// ����������
                                if (checkRowIsFive(robot.getRow(), robot.getCol())
                                        || checkColIsFive(robot.getRow(), robot.getCol())
                                        || checkRightBias(robot.getRow(), robot.getCol())
                                        || checkLeftBias(robot.getRow(), robot.getCol())) {// �������Ƿ�Ӯ
                                    isGameBegin = false;
                                    isGameOver = true;
                                    whoSmile = Color.black;
                                    canvas.repaint();
                                    JOptionPane.showMessageDialog(null, "������Ӯ�ˣ�");
                                    return;
                                }
                                if (isDogfall() == true) {// �Ƿ�ƽ��
                                    isGameBegin = false;
                                    isGameOver = true;
                                    canvas.repaint();
                                    JOptionPane.showMessageDialog(null, "ƽ���ˣ�");
                                    return;
                                }
                            }
                        }
                    } else {// ��ʾ������û�������ˣ�Ҳ�û���������
                        robot.cerebra(allChesses, canvas, Color.black, stack);
                        if (checkRowIsFive(robot.getRow(), robot.getCol())
                                || checkColIsFive(robot.getRow(), robot.getCol())
                                || checkRightBias(robot.getRow(), robot.getCol())
                                || checkLeftBias(robot.getRow(), robot.getCol())) {
                            isGameBegin = false;
                            isGameOver = true;
                            whoSmile = Color.black;
                            canvas.repaint();
                            JOptionPane.showMessageDialog(null, "������Ӯ�ˣ�");
                            return;
                        }
                    }
                    isUndo = false;// ��������
                } else {
                    try {
                        Thread.sleep(1000);// �߳�˯һ����
                        unDoTime--;// ����ʱ���һ
                        if (unDoTime == -10) {
                            unDoTime = -1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });

    canvas.addMouseListener(new MouseInputAdapter() {

        public void mousePressed(MouseEvent e) {
            if ((e.getX() >= 179 && e.getX() <= 652) && (e.getY() >= 30 && e.getY() <= 497) && isGameBegin == true
                    && robot.getMyIsPlayingChess() == false && isUndo == false) { // ���������������򵥻�������Ϸ��ʼ���һ�����û��������ô��ִ��if�����
                int row = (int) ((e.getY() - 30) / 33.4);// ������ת�����±���
                int col = (int) ((e.getX() - 179) / 33.8);// ������ת�����±���
                if (allChesses[row][col] == null) {// ����ڸ�����λ�õ��ڿվͱ�ʾ��λ��û�������������
                    allChesses[row][col] = Color.white;// ����ɫ��ֵ����λ�õĶ�ά����
                    stack.Push(row, col, Color.white);// ������Ϣѹ��ջ
                    canvas.repaint();// ���»���,����������
                    if (checkRowIsFive(row, col) || checkColIsFive(row, col) || checkRightBias(row, col)
                            || checkLeftBias(row, col)) { // �����Զ��庯���ж��ĸ������Ƿ����������飬�Ƿ���
                        isGameBegin = false;// ��Ϸû�п�ʼ
                        isGameOver = true;// ��Ϸ����
                        whoSmile = Color.white;// ����������Ц
                        canvas.repaint();// ���»���
                        JOptionPane.showMessageDialog(null, "��Ӯ�ˣ�");
                        return;
                    }
                    if (isDogfall() == true) {// ������û�����ж��Ƿ���ƽ��
                        isGameBegin = false;// ��Ϸû�п�ʼ
                        isGameOver = true;// ��Ϸ����
                        canvas.repaint();// ���»���
                        JOptionPane.showMessageDialog(null, "ƽ���ˣ�");
                        return;
                    }
                    robot.cerebra(allChesses, canvas, Color.black, stack);// ���û����������������
                    if (checkRowIsFive(robot.getRow(), robot.getCol())
                            || checkColIsFive(robot.getRow(), robot.getCol())
                            || checkRightBias(robot.getRow(), robot.getCol())
                            || checkLeftBias(robot.getRow(), robot.getCol())) { //// �����Զ��庯���ж��ĸ������Ƿ����������飬�Ƿ���
                        isGameBegin = false;// ��Ϸû�п�ʼ
                        isGameOver = true;// ��Ϸ����
                        whoSmile = Color.black;// ����������������Ц
                        canvas.repaint();// ���»���
                        JOptionPane.showMessageDialog(null, "������Ӯ�ˣ�");
                        return;
                    }
                    if (isDogfall() == true) {// ���������û��Ӯ���ж��Ƿ���ƽ��
                        isGameBegin = false;// ��Ϸû�п�ʼ
                        isGameOver = true;// ��Ϸ����
                        canvas.repaint();// ���»���
                        JOptionPane.showMessageDialog(null, "ƽ���ˣ�");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "��λ���������ˣ�");
                }
            } else {
                if ((e.getX() >= 17 && e.getX() <= 39) && (e.getY() >= 249 && e.getY() <= 289)) { // ��������ڸ������ʾ�����˿�ʼ
                    for (int i = 0; i < allChesses.length; i++) {// �������
                        for (int j = 0; j < allChesses.length; j++) {
                            allChesses[i][j] = null;
                        }
                    }

                    canvas.repaint();// ���»���
                    isGameBegin = true;// ��Ϸ��ʼ
                    isGameOver = false;// ��Ϸ����
                    playerTime = 1800;// ���³�ʼ���������ʱ��
                    robotTime = 1800;// ���³�ʼ���������ʱ��
                    whoSmile = null;// ���³�ʼ��˭Ц
                    stack.clearStack();// ���ջ
                    if (PlayerThread.getState() == Thread.State.NEW) {// �������̻߳�û�������������߳�
                        PlayerThread.start();
                    }
                    if (robotThread.getState() == Thread.State.NEW) {// ����������̻߳�û�������������߳�
                        robotThread.start();
                    }
                    JOptionPane.showMessageDialog(null, "���Կ�ʼ�ˣ�");
                    robot.cerebra(allChesses, canvas, Color.black, stack);// ����������
                    if (checkRowIsFive(robot.getRow(), robot.getCol())
                            || checkColIsFive(robot.getRow(), robot.getCol())
                            || checkRightBias(robot.getRow(), robot.getCol())
                            || checkLeftBias(robot.getRow(), robot.getCol())) { // �жϻ������Ƿ���
                        isGameBegin = false;
                        isGameOver = true;
                        whoSmile = Color.black;
                        canvas.repaint();
                        JOptionPane.showMessageDialog(null, "������Ӯ�ˣ�");
                        return;
                    }
                    if (isDogfall() == true) {// �Ƿ���ƽ��
                        isGameBegin = false;
                        isGameOver = true;
                        canvas.repaint();
                        JOptionPane.showMessageDialog(null, "ƽ���ˣ�");
                        return;
                    }
                }
                if ((e.getX() >= 48 && e.getX() <= 72) && (e.getY() >= 249 && e.getY() <= 289)) { // ������ͣ����
                    if (isGameOver == true) {// �����Ϸ�����Ͳ�ִ����ͣ
                        return;
                    }
                    isGameBegin = false;// ������Ϸû�п�ʼ�����ﵽ��ͣЧ��
                    JOptionPane.showMessageDialog(null, "��ͣ��");
                }
                if ((e.getX() >= 80 && e.getX() <= 104) && (e.getY() >= 249 && e.getY() <= 289)) { // ������������
                    if (isGameOver == true) {// �����Ϸ�����Ͳ�ִ����ͣ
                        return;
                    }
                    isGameBegin = true;// ������Ϸ��ʼ�����ﵽ����Ч��
                    JOptionPane.showMessageDialog(null, "������");
                }
                if ((e.getX() >= 113 && e.getX() <= 136) && (e.getY() >= 249 && e.getY() <= 289)) { // ������������
                    if (isGameOver == true || isGameBegin == false) { // �����Ϸû�п�ʼ������Ϸ�Ѿ������Ͳ��ܻ���
                        return;
                    }
                    isUndo = true;
                    for (int i = 0; i < allChesses.length; i++) {// �������
                        for (int j = 0; j < allChesses.length; j++) {
                            allChesses[i][j] = null;
                        }
                    }
                    stack.Pop();// ���ߵ���һ�����ջ
                    LNode p = stack.getHeadNode().getNext();// ��õ�һ�����
                    while (p != null) {// �����һ�����Ϊ�գ���ʾ����Ϊ�գ�����Ϊ�վ�������������ֱ��ջΪ�ս���
                        allChesses[p.getRow()][p.getCol()] = p.getChessColor();
                        p = p.getNext();
                    }
                    canvas.repaint();
                    unDoTime = 5;// ���5���Ӳ����������ʾ�������
                    if (unDoThread.getState() == Thread.State.NEW) {// ������߳�û������������
                        unDoThread.start();
                    }
                }
            }
        }
    });
    this.setTitle("��������Ϸ");// ���ñ���
    this.setResizable(false);// ���ò��ܸı����С
    this.setVisible(true);// ������ʾ����
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// ���ùرմ����Ĳ���,�رմ�����������
}

public boolean isDogfall() {// �Ƿ���ƽ��
    if (stack.StackLength() == 14 * 14) {// ջ�м�¼�����̵���Ϣ��������һ����û�о���ʤ��,ջ�е�Ԫ�ظ����������̵��������ͱ�ʾƽ��
        return true;
    } else {
        return false;
    }
}

public boolean checkLeftBias(int row, int col) {// �����б
    int leftBiasUp = checkLeftBiasUp(row, col);// �õ���б�ϵĸ���
    int leftBiasDown = checkLeftBiasDown(row, col);// �õ���б�µĸ���
    int sum = leftBiasUp + leftBiasDown - 1;// �ڼ�������ж�����һ�����ӣ�����Ҫ��һ������
    if (sum >= 5) {// �Ѿ���������
        return true;
    } else {
        return false;
    }
}

public int checkLeftBiasDown(int row, int col) {
    int i = row;
    int j = col;
    int count = 1;
    while (i < allChesses.length - 1 && j > 0) {
        if (allChesses[i][j] == allChesses[i + 1][j - 1]) {
            count++;
            i = i + 1;
            j = j - 1;
        } else {
            break;
        }
    }
    return count;
}

public int checkLeftBiasUp(int row, int col) {
    int i = row;
    int j = col;
    int count = 1;
    while (i > 0 && j < allChesses.length - 1) {
        if (allChesses[i - 1][j + 1] == allChesses[i][j]) {
            count++;
            i = i - 1;
            j = j + 1;
        } else {
            break;
        }
    }
    return count;
}

public boolean checkRightBias(int row, int col) {
    int rightBiasUp = checkRightBiasUp(row, col);
    int rightBiasDown = checkRightBiasDown(row, col);
    int sum = rightBiasUp + rightBiasDown - 1;
    if (sum >= 5) {
        return true;
    } else {
        return false;
    }
}

public int checkRightBiasUp(int row, int col) {
    int i = row;
    int j = col;
    int count = 1;
    while (i > 0 && j > 0) {
        if (allChesses[i - 1][j - 1] == allChesses[i][j]) {
            count++;
            i = i - 1;
            j = j - 1;
        } else {
            break;
        }
    }
    return count;
}

public int checkRightBiasDown(int row, int col) {
    int i = row;
    int j = col;
    int count = 1;
    while (i < allChesses.length - 1 && j < allChesses.length - 1) {
        if (allChesses[i][j] == allChesses[i + 1][j + 1]) {
            count++;
            i = i + 1;
            j = j + 1;
        } else {
            break;
        }
    }
    return count;
}

public boolean checkColIsFive(int row, int col) {
    int upCount = checkColUpIsFive(row, col);
    int downCount = checkColDownIsFive(row, col);
    int sum = upCount + downCount - 1;
    if (sum >= 5) {
        return true;
    } else {
        return false;
    }
}

public boolean checkRowIsFive(int row, int col) {
    int rightCount = checkRowRightIsFive(row, col);
    int leftCount = checkRowLeftIsFive(row, col);
    int sum = rightCount + leftCount - 1;
    if (sum >= 5) {
        return true;
    } else {
        return false;
    }
}

public int checkColDownIsFive(int row, int col) {
    int count = 1;
    for (int i = row; i < allChesses.length - 1; i++) {
        if (allChesses[i][col] == allChesses[i + 1][col]) {
            count++;
        } else {
            break;
        }
    }
    return count;
}

public int checkColUpIsFive(int row, int col) {
    int count = 1;
    for (int i = row; i > 0; i--) {
        if (allChesses[i - 1][col] == allChesses[i][col]) {
            count++;
        } else {
            break;
        }
    }
    return count;
}

public int checkRowLeftIsFive(int row, int col) {
    int count = 1;
    for (int i = col; i > 0; i--) {
        if (allChesses[row][i - 1] == allChesses[row][i]) {
            count++;
        } else {
            break;
        }
    }
    return count;
}

public int checkRowRightIsFive(int row, int col) {
    int count = 1;
    for (int i = col; i < allChesses.length - 1; i++) {
        if (allChesses[row][i] == allChesses[row][i + 1]) {
            count++;
        } else {
            break;
        }
    }
    return count;
}

public void setWindowPercent(int percent) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize((int) (percent / 100.0 * screenSize.width), (int) (percent / 100.0 * screenSize.height));
}

public void setWindowCenter() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    if (this.getWidth() > screenSize.width || this.getHeight() > screenSize.height) {
        this.setLocation(0, 0);
    } else {
        this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
    }
}

class MyCanvas extends JPanel {
    public void paint(Graphics g) {
        BufferedImage bImage = new BufferedImage(700, 550, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bImage.createGraphics();
        g2.drawImage(imageBG, 0, 0, this.getWidth(), this.getHeight(), this);
        g2.drawImage(imageMenu, 6, 242, 140, 52, this);
        g2.drawImage(imageW, 26, 42, imageW.getWidth(this), imageW.getHeight(this), this);
        g2.drawImage(imageW, 26, 307, imageW.getWidth(this), imageW.getHeight(this), this);
        if (whoSmile == Color.black) {
            g2.drawImage(imageX, 26, 307, imageX.getWidth(this), imageX.getHeight(this), this);
        } else if (whoSmile == Color.white) {
            g2.drawImage(imageX, 26, 42, imageX.getWidth(this), imageX.getHeight(this), this);
        }
        g2.setColor(Color.white);
        int minute = playerTime / 60;
        int second = playerTime % 60;
        String minuteStr = minute < 10 ? "0" + minute : minute + "";
        String secondStr = second < 10 ? "0" + second : second + "";
        g2.drawString(minuteStr + ":" + secondStr, 77, 209);
        minute = robotTime / 60;
        second = robotTime % 60;
        minuteStr = minute < 10 ? "0" + minute : minute + "";
        secondStr = second < 10 ? "0" + second : second + "";
        g2.drawString(minuteStr + ":" + secondStr, 77, 477);
        for (int i = 0; i < allChesses.length; i++) {
            for (int j = 0; j < allChesses.length; j++) {
                if (allChesses[i][j] != null) {
                    if (allChesses[i][j] == Color.black) {
                        g2.drawImage(imageBlack, (int) ((179 + j * 34.8) + 2), (int) ((30 + i * 33.4) + 2), 31, 31,
                                this);
                    }
                    else {
                        g2.drawImage(imageWhite, (int) ((179 + j * 34.8) + 2), (int) ((30 + i * 33.4) + 2), 31, 31,
                                this);
                    }
                }
            }
        }
        if (stack.getStackTop() != null) {
            g2.drawImage(imageRim, (int) ((179 + stack.getStackTop().getCol() * 34.8) + 2),
                    (int) ((30 + stack.getStackTop().getRow() * 33.4) + 2), 31, 31, this);
        }
        g.drawImage(bImage, 0, 0, this);
    }
}

public static void main(String[] args) {
    new PlayChess();
}
}
