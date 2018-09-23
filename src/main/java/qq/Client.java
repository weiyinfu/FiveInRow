package qq;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Client extends Common {
	Socket me = null;
	PrintWriter cout;
	Scanner cin;

	public static void main(String[] args) {
		Client c = new Client();
		c.connect();
	}

	void connect() {
		try {
			me = new Socket();
			// me.connect(new InetSocketAddress("58.154.191.86", 12345));
			me.connect(new InetSocketAddress("127.0.0.1", 12345));
			JOptionPane.showMessageDialog(this, "Connection succeeded");
			cout = new PrintWriter(me.getOutputStream());
			cin = new Scanner(me.getInputStream());
			Thread listenTalk = new Thread(listen);
			listenTalk.start();
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(this, "He has exited");
			System.exit(0);
		}
	}

	@Override
	String cin() {
		return cin.nextLine();
	}

	@Override
	void cout(String s) {
		cout.println(s);
		cout.flush();
	}
}
