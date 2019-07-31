package com.zf.websocketservice.messagehandler;

import com.zf.websocketservice.IMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageHandlerFactory {

	@Autowired
	private ApplicationContext applicationContext;

	private final static Map<Integer, IMessageHandler> MESSAGE_HANDLER = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		Map<String, IMessageHandler> messageHandlers = applicationContext.getBeansOfType(IMessageHandler.class);
		for (Map.Entry<String, IMessageHandler> messageHandler : messageHandlers.entrySet()) {
			MESSAGE_HANDLER.put(messageHandler.getValue().getMessageType().getType(), messageHandler.getValue());
		}
	}

	public static IMessageHandler getMessageHandler(int messageType) {
		if (messageType <= 0) {
			return null;
		}
		return MESSAGE_HANDLER.get(messageType);
	}

}
