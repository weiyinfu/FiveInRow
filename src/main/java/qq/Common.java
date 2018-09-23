package qq;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public abstract class Common extends JFrame {
	JTextArea talked = new JTextArea();
	JTextField saying = new JTextField();

	Common() {
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		talked.setEditable(false);
		talked.setFont(new Font("serif", Font.BOLD, 20));
		talked.setLineWrap(true);
		saying.setFont(new Font("serif", Font.BOLD, 20));
		add(new JScrollPane(talked), BorderLayout.CENTER);
		add(saying, BorderLayout.SOUTH);
		setVisible(true);

		saying.getInputMap().put(KeyStroke.getKeyStroke('\n'), "say");
		saying.getActionMap().put("say", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				say();
			}
		});
	}

	Runnable listen = new Runnable() {
		@Override
		public void run() {
			try {
				while (true) {
					String line = cin();
					line = URLDecoder.decode(line, "utf-8");
					if (line == null)
						continue;
					line = line.trim();
					if (line.isEmpty())
						continue;
					talked.append("he:" + line + "\n");
				}
			} catch (Exception e) {
				JOptionPane.showConfirmDialog(Common.this, "He has exited");
				System.exit(0);
			}
		}
	};

	void say() {
		try {
			cout(URLEncoder.encode(saying.getText(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		talked.append("me: " + saying.getText() + "\n");
		talked.setCaretPosition(talked.getText().length());
		saying.setText("");
	}

	abstract String cin();

	abstract void cout(String s);
}
