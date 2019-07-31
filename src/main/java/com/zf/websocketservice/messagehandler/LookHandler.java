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

@Service
public class LookHandler implements IMessageHandler {

	@Override
	public MessageType getMessageType() {
		return MessageType.LOOK;
	}

	@Override
	public void messageHandler(ExecutorClientInfo clientInfo, Session session) {
		WebSocketServer serverInfo = WebSocketServer.WEBSOCKET_INFOS.get(session.getId());
		ExecutorCenter.LOOKING_CLIENTS.get(clientInfo.getExecuteId()).add(serverInfo);
		ExecutorInfo executorInfo = ExecutorCenter.ALL_EXECUTOR.get(clientInfo.getExecuteId());
		ExecutorCenter.EXECUTE_LOGS.get(clientInfo.getExecuteId()).forEach(t -> {
			try {
				if (serverInfo.getWebSocketInfo() != null && serverInfo.getWebSocketInfo().getSession() != null) {
					serverInfo.sendMessage(ExecutorClientInfo.getInstance(MessageType.MESSAGE.getType(), clientInfo.getExecuteId(), t, executorInfo.getStatus().getStatus()).toString());
				}
			} catch (IOException e) {
			}
		});
	}
}
