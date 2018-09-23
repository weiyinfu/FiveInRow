package qq;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JTextField;

public class UDPqq extends Common {
	public static void main(String[] args) {
		UDPqq qq = new UDPqq();
		qq.start();
	}

	DatagramSocket socket;
	JTextField aim = new JTextField(10);

	public UDPqq() {
		add(aim, BorderLayout.NORTH);
		try {
			socket = new DatagramSocket(12345);
		} catch (Exception e) {
		}
	}

	void start() {
		new Thread(listen).start();
	}

	@Override
	String cin() {
		try {
			byte[] buf = new byte[socket.getReceiveBufferSize()];
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			socket.receive(p);
			return new String(p.getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	void cout(String s) {
		try {
			String name = aim.getText().isEmpty() ? InetAddress.getLocalHost().getHostName() : aim.getText();
			DatagramPacket packet = new DatagramPacket(s.getBytes(), s.getBytes().length, InetAddress.getByName(name),
					12345);
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
