package qq;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Server extends Common {
	ServerSocket server = null;
	PrintWriter cout;
	Scanner cin;

	public static void main(String[] args) {
		Server s = new Server();
		s.connect();
	}

	void connect() {
		try {
			server = new ServerSocket(12345);
			Socket client = server.accept();
			JOptionPane.showMessageDialog(this, "connected : " + client.getLocalAddress().getHostName());
			cout = new PrintWriter(client.getOutputStream());
			cin = new Scanner(client.getInputStream());
			Thread listenTalk = new Thread(listen);
			listenTalk.start();
		} catch (Exception e) {
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
