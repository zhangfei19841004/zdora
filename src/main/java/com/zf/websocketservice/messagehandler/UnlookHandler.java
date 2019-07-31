package com.zf.websocketservice.messagehandler;

import com.zf.executor.ExecutorCenter;
import com.zf.executor.ExecutorClientInfo;
import com.zf.executor.ExecutorInfo;
import com.zf.message.MessageType;
import com.zf.websocketservice.IMessageHandler;
import com.zf.websocketservice.WebSocketServer;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Iterator;

@Service
public class UnlookHandler implements IMessageHandler {

	@Override
	public MessageType getMessageType() {
		return MessageType.UNLOOK;
	}

	@Override
	public void messageHandler(ExecutorClientInfo clientInfo, Session session) {
		try {
			Iterator<WebSocketServer> it = ExecutorCenter.LOOKING_CLIENTS.get(clientInfo.getExecuteId()).iterator();
			while (it.hasNext()) {
				WebSocketServer info = it.next();
				if (info.getWebSocketInfo() != null && info.getWebSocketInfo().getSession() != null && info.getWebSocketInfo().getSession().getId().equals(session.getId())) {
					it.remove();
				}
			}
		} catch (Exception e) {
		}
	}
}
