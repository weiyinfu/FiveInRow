package one;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class TEST {
	static Image image;
	public static void main(String[] args) {
		image = Toolkit.getDefaultToolkit().getImage("res/W.png");
		JFrame frame=new JFrame(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(image, 20, 20, null);
			}
		};
		MediaTracker tracker=new MediaTracker(frame);
		tracker.addImage(image, 0);
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setVisible(true);
	}
}
