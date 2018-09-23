package three;

public class Main {
public static void main(String[] args) {
    new Thread(() -> {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Client();
    }).start();
    new Server();
}
}
