package com.zf.websocketservice.messagehandler;

import com.zf.executor.ExecutorCenter;
import com.zf.executor.ExecutorClientInfo;
import com.zf.executor.ExecutorStatus;
import com.zf.message.MessageType;
import com.zf.websocketservice.IMessageHandler;
import com.zf.websocketservice.WebSocketServer;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;

@Service
public class MessageHandler implements IMessageHandler {

	@Override
	public MessageType getMessageType() {
		return MessageType.MESSAGE;
	}

	@Override
	public void messageHandler(ExecutorClientInfo clientInfo, Session session) {
		WebSocketServer serverInfo = WebSocketServer.WEBSOCKET_INFOS.get(session.getId());
		try {
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
				if (webSocketServer.getWebSocketInfo() != null && serverInfo.getWebSocketInfo().getSession() != null) {
					webSocketServer.sendMessage(ExecutorClientInfo.getInstance(MessageType.MESSAGE.getType(), clientInfo.getExecuteId(), clientInfo.getMessage(), clientInfo.getExecuteStatus()).toString());
				}
			}
		} catch (IOException e) {
		}
	}
}
