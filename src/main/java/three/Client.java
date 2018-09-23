package three;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client {
public static void main(String[] args) {
    new Client();

}

Client() {
    Common c = new Common();

    c.setTitle("Five Client--made by weidiao.neu");
    Socket client = null;
    try {
        client = new Socket(InetAddress.getLocalHost(), 2014);
        c.cin = new DataInputStream(client.getInputStream());
        c.cout = new DataOutputStream(client.getOutputStream());
        JOptionPane.showMessageDialog(c, "connected : " + client.getLocalAddress());
    } catch (Exception e) {
        e.printStackTrace();
    }
    c.myColor = 1;
    new Thread(c.run).start();
}
}
