package learnWebsocket.chat;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public class MyClientApp {
public static void main(String args[]) throws DeploymentException, IOException {
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    String url = "ws://localhost:8080/chat/main/2134/12345";
    System.out.println("Connecting to" + url);
    Session session = container.connectToServer(MyClient.class, URI.create(url));
    Scanner cin = new Scanner(System.in);
    while (true) {
        String input = cin.nextLine();
        if (!input.equals("exit"))
            session.getBasicRemote().sendText(input);
        else
            break;
    }
    cin.close();
}
}