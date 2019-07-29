package com.zf.service;

import com.alibaba.fastjson.JSON;
import com.zf.executor.ExecutorCenter;
import com.zf.executor.ExecutorClientInfo;
import com.zf.executor.ExecutorInfo;
import com.zf.executor.ExecutorStatus;
import com.zf.message.MessageInfo;
import com.zf.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhangfei on 2018/10/19/019.
 */
@ServerEndpoint("/websocket/{cid}/{mt}")
@Component
public class WebSocketServer {

	private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);

	//静态变量，用来记录当前在线连接数。
	private static AtomicInteger atomic = new AtomicInteger(0);

	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	public static ConcurrentHashMap<String, WebSocketServer> WEBSOCKET_INFOS = new ConcurrentHashMap();

	private WebSocketInfo webSocketInfo = new WebSocketInfo();

	public WebSocketInfo getWebSocketInfo() {
		return webSocketInfo;
	}

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("cid") String cid, @PathParam("mt") int messageType) {
		webSocketInfo.setSession(session);
		webSocketInfo.setCid(cid);
		webSocketInfo.setMessageType(messageType);
		if (messageType == MessageType.BROWSER.getType()) {
			try {
				this.sendMessage(MessageInfo.getInstance(MessageType.SESSIONID, session.getId()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (messageType == MessageType.CLIENT.getType()) {
			try {
				this.sendMessage(MessageInfo.getInstance(MessageType.SESSIONID, session.getId()));
				this.sendMessage(MessageType.MESSAGE, "欢迎你: " + cid);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return;
		}
		WEBSOCKET_INFOS.put(session.getId(), this); //加入set中
		addOnlineCount(); //在线数加1
		log.info("欢迎:" + cid + ",sessionId为:" + session.getId() + ",当前在线人数为:" + getOnlineCount());
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		WEBSOCKET_INFOS.remove(this.webSocketInfo.getSession().getId()); //从set中删除
		subOnlineCount(); //在线数减1
		log.info("连接关闭" + this.webSocketInfo.getCid() + ", 当前在线人数为:" + getOnlineCount());
		this.webSocketInfo.setSession(null);
		this.webSocketInfo = null;
		closeWebSocket(ExecutorCenter.LOOKING_CLIENTS);
		closeWebSocket(ExecutorCenter.EXECUTING_CLIENTS);
	}

	/**
	 * 收到客户端消息后调用的方法 * * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		//log.info("收到来自窗口" + this.webSocketInfo.getCid() + "的信息:" + message);
		WebSocketServer serverInfo = WebSocketServer.WEBSOCKET_INFOS.get(session.getId());
		try {
			ExecutorClientInfo clientInfo = JSON.parseObject(message, ExecutorClientInfo.class);
			if (clientInfo.getType() == MessageType.MESSAGE.getType()) {//从JAVA CLIENT传过来的clientInfo
				if (clientInfo.getExecuteStatus() == ExecutorStatus.STATUS3.getStatus()) {
					ExecutorCenter.ALL_EXECUTOR.get(clientInfo.getExecuteId()).setStatus(ExecutorStatus.STATUS3);
					if (ExecutorCenter.EXECUTING_CLIENTS.containsKey(clientInfo.getExecuteId())) {
						ExecutorCenter.EXECUTING_CLIENTS.get(clientInfo.getExecuteId()).forEach(t -> {
							try {
								t.sendMessage(ExecutorClientInfo.getInstance(MessageType.MESSAGE.getType(), clientInfo.getExecuteId(), clientInfo.getMessage(), clientInfo.getExecuteStatus()).toString());
							} catch (IOException e) {
							}
						});
						ExecutorCenter.EXECUTING_CLIENTS.remove(clientInfo.getExecuteId());
					}
				}
				ExecutorCenter.EXECUTE_LOGS.get(clientInfo.getExecuteId()).add(clientInfo.getMessage());
				for (WebSocketServer webSocketServer : ExecutorCenter.LOOKING_CLIENTS.get(clientInfo.getExecuteId())) {
					if (webSocketServer.getWebSocketInfo() != null && serverInfo.webSocketInfo.getSession() != null) {
						webSocketServer.sendMessage(ExecutorClientInfo.getInstance(MessageType.MESSAGE.getType(), clientInfo.getExecuteId(), clientInfo.getMessage(), clientInfo.getExecuteStatus()).toString());
					}
				}
			} else if (clientInfo.getType() == MessageType.LOOK.getType()) {//从浏览器过来的
				ExecutorCenter.LOOKING_CLIENTS.get(clientInfo.getExecuteId()).add(WEBSOCKET_INFOS.get(session.getId()));
				ExecutorInfo executorInfo = ExecutorCenter.ALL_EXECUTOR.get(clientInfo.getExecuteId());
				ExecutorCenter.EXECUTE_LOGS.get(clientInfo.getExecuteId()).forEach(t -> {
					try {
						if (serverInfo.webSocketInfo != null && serverInfo.webSocketInfo.getSession() != null) {
							serverInfo.sendMessage(ExecutorClientInfo.getInstance(MessageType.MESSAGE.getType(), clientInfo.getExecuteId(), t, executorInfo.getStatus().getStatus()).toString());
						}
					} catch (IOException e) {
					}
				});
			} else if (clientInfo.getType() == MessageType.UNLOOK.getType()) {//从浏览器过来的
				Iterator<WebSocketServer> it = ExecutorCenter.LOOKING_CLIENTS.get(clientInfo.getExecuteId()).iterator();
				while (it.hasNext()) {
					WebSocketServer info = it.next();
					if (info.getWebSocketInfo() != null && info.getWebSocketInfo().getSession() != null && info.getWebSocketInfo().getSession().getId().equals(session.getId())) {
						it.remove();
					}
				}
			} else if (clientInfo.getType() == MessageType.OPEN.getType()) {
				for (Integer executing : ExecutorCenter.EXECUTING_CLIENTS.keySet()) {
					List<WebSocketServer> list = ExecutorCenter.EXECUTING_CLIENTS.get(executing);
					WebSocketServer ws = getWebSocket(list, session);
					if (ws == null) {
						list.add(serverInfo);
					}
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * * @param session * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发生错误:" + this.webSocketInfo.getCid());
	}

	/**
	 * 实现服务器主动推送
	 */
	public void sendMessage(MessageInfo info) throws IOException {
		this.webSocketInfo.getSession().getBasicRemote().sendText(info.toString());
	}

	public void sendMessage(MessageType messageType, String message) throws IOException {
		this.webSocketInfo.getSession().getBasicRemote().sendText(MessageInfo.getInstance(messageType, message).toString());
	}

	public void sendMessage(String message) throws IOException {
		this.webSocketInfo.getSession().getBasicRemote().sendText(message);
	}

	/**
	 * 群发自定义消息 *
	 */
	public static void sendInfo(String cid, String sessionId, MessageType messageType, String message) throws IOException {
		log.info("推送消息到" + cid + "，推送内容:" + message);
		if (cid == null && sessionId == null) {
			for (WebSocketServer item : WEBSOCKET_INFOS.values()) {
				try {
					item.sendMessage(messageType, message);
				} catch (IOException e) {

				}
			}
			return;
		}
		WebSocketServer item = WEBSOCKET_INFOS.get(sessionId);
		if (item == null) {
			return;
		}
		if (item.webSocketInfo.getCid().equals(cid) && item.webSocketInfo.getSession().getId().equals(sessionId)) {
			item.sendMessage(messageType, message);
		}
	}

	public static int getOnlineCount() {
		return atomic.get();
	}

	public static void addOnlineCount() {
		atomic.incrementAndGet();
	}

	public static void subOnlineCount() {
		atomic.decrementAndGet();
	}

	private static WebSocketServer getWebSocket(List<WebSocketServer> list, Session session) {
		for (WebSocketServer webSocketServer : list) {
			if (webSocketServer.getWebSocketInfo().getSession().getId().equals(session.getId())) {
				return webSocketServer;
			}
		}
		return null;
	}

	private static void closeWebSocket(ConcurrentHashMap<Integer, List<WebSocketServer>> map) {
		for (Integer key : map.keySet()) {
			Iterator<WebSocketServer> it = map.get(key).iterator();
			while (it.hasNext()) {
				WebSocketServer ws = it.next();
				if (ws.getWebSocketInfo() == null || ws.getWebSocketInfo().getSession() == null) {
					it.remove();
				}
			}
		}
	}
}
