package learnWebsocket.chat.src.chat;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

//serverEndPoint必须有一个无参构造函数
@ServerEndpoint("/main/{id}/{s}")
public class Server {
	@OnOpen
	public void open(@PathParam("id") int id, @PathParam("s") String haha, Session session) {
		System.out.println("open");
	}

	@OnClose
	public void close(@PathParam("id") int id, @PathParam("s") String haha, Session session) {
		System.out.println("close");
	}

	@OnError
	public void error(Throwable e, @PathParam("id") int id, @PathParam("s") String haha, Session session) {
		System.out.println("error");
	}

	// 返回值为String类型则直接返回给客户端
	@OnMessage
	public String message(String message, @PathParam("id") int id, @PathParam("s") String haha, Session session)
			throws IOException {
		System.out.println(message);
		System.out.println("message");
		session.getBasicRemote().sendText("baga");
		return "haha";
	}
}
