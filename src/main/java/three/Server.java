package three;

import javax.swing.JOptionPane;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

public static void main(String[] args) {
    new Server();
}

Server() {
    Common c = new Common();
    c.setTitle("Five Server--made by weidiao.neu");
    ServerSocket server = null;
    System.out.println("haha");
    try {
        server = new ServerSocket(2014);
        Socket client = server.accept();
        c.cin = new DataInputStream(client.getInputStream());
        c.cout = new DataOutputStream(client.getOutputStream());
        JOptionPane.showMessageDialog(c, "connected : " + client.getLocalAddress().getHostName());
    } catch (Exception e) {
        e.printStackTrace();
    }
    c.myColor = 2;
    System.out.println("haha");
    new Thread(c.run).start();
}
}
