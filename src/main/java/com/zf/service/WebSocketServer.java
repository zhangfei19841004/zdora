package com.zf.service;

import com.zf.message.MessageInfo;
import com.zf.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by zhangfei on 2018/10/19/019.
 */
@ServerEndpoint("/websocket/{cid}")
@Component
public class WebSocketServer {

	private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);

	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	//接收客户端ip
	private String cid = "";

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("cid") String cid) {
		this.session = session;
		webSocketSet.add(this); //加入set中
		addOnlineCount(); //在线数加1
		log.info("有新窗口开始监听:" + cid + ",当前在线人数为" + getOnlineCount());
		this.cid = cid;
		try {
			this.sendMessage(MessageInfo.getInstance(MessageType.SESSIONID, session.getId()));
			this.sendMessage(MessageType.MESSAGE, "欢迎你: " + cid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		webSocketSet.remove(this); //从set中删除
		subOnlineCount(); //在线数减1
		log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法 * * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("收到来自窗口" + cid + "的信息:" + message); //群发消息
	}

	/**
	 * * @param session * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发生错误");
		//error.printStackTrace();
	}

	/**
	 * 实现服务器主动推送
	 */
	public void sendMessage(MessageInfo info) throws IOException {
		this.session.getBasicRemote().sendText(info.toString());
	}

	public void sendMessage(MessageType messageType, String message) throws IOException {
		this.session.getBasicRemote().sendText(MessageInfo.getInstance(messageType, message).toString());
	}

	/**
	 * 群发自定义消息 *
	 */
	public static void sendInfo(String cid, String sessionId, MessageType messageType, String message) throws IOException {
		log.info("推送消息到" + cid + "，推送内容:" + message);
		for (WebSocketServer item : webSocketSet) {
			try {
				//这里可以设定只推送给这个sid的，为null则全部推送
				if (cid == null && sessionId == null) {
					item.sendMessage(messageType, message);
				} else if (item.cid.equals(cid) && item.session.getId().equals(sessionId)) {
					item.sendMessage(messageType, message);
				}
			} catch (IOException e) {
				continue;
			}
		}
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketServer.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketServer.onlineCount--;
	}


}
