package com.zf.websocketservice.messagehandler;

import com.zf.executor.ExecutorCenter;
import com.zf.executor.ExecutorClientInfo;
import com.zf.message.MessageType;
import com.zf.websocketservice.IMessageHandler;
import com.zf.websocketservice.WebSocketServer;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.List;

@Service
public class OpenHandler implements IMessageHandler {

	@Override
	public MessageType getMessageType() {
		return MessageType.OPEN;
	}

	@Override
	public void messageHandler(ExecutorClientInfo clientInfo, Session session) {
		WebSocketServer serverInfo = WebSocketServer.WEBSOCKET_INFOS.get(session.getId());
		try {
			for (Integer executing : ExecutorCenter.EXECUTING_CLIENTS.keySet()) {
				List<WebSocketServer> list = ExecutorCenter.EXECUTING_CLIENTS.get(executing);
				WebSocketServer ws = getWebSocket(list, session);
				if (ws == null) {
					list.add(serverInfo);
				}
			}
		} catch (Exception e) {
		}
	}

	private static WebSocketServer getWebSocket(List<WebSocketServer> list, Session session) {
		for (WebSocketServer webSocketServer : list) {
			if (webSocketServer.getWebSocketInfo().getSession().getId().equals(session.getId())) {
				return webSocketServer;
			}
		}
		return null;
	}
}
