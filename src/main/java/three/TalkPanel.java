package three;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class TalkPanel extends JPanel {
Common window;
public JTextArea talked;
public JTextField saying;

TalkPanel(Common window) {
    this.window = window;
    setLayout(new BorderLayout());

    talked = new JTextArea(15, 5);
    saying = new JTextField(10);
    talked.setFont(new Font("serif", Font.BOLD, 20));
    talked.setLineWrap(true);
    talked.setEditable(false);
    saying.setFont(new Font("serif", Font.BOLD, 20));
    add(new JScrollPane(talked), BorderLayout.NORTH);
    add(saying, BorderLayout.SOUTH);
    saying.addKeyListener(listenKey);
}

KeyListener listenKey = new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                byte[] words = saying.getText().getBytes();
                window.cout.writeByte(1);
                window.cout.writeByte(words.length);
                window.cout.write(words);
            } catch (IOException e1) {
            }
            talked.append("我说 : " + saying.getText() + "\n");
            saying.setText("");
        }
    }
};
}
