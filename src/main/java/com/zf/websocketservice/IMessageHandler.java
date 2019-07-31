package com.zf.websocketservice;

import com.zf.executor.ExecutorClientInfo;
import com.zf.message.MessageType;

import javax.websocket.Session;

public interface IMessageHandler {

	MessageType getMessageType();

	void messageHandler(ExecutorClientInfo clientInfo, Session session);

}
