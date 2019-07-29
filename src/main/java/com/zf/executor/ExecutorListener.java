package com.zf.executor;

import com.zf.service.WebSocketServer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfei on 2018/11/5/005.
 */
@Component
public class ExecutorListener {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ExecutorListener.class);

	@Autowired
	private ExecutorCenter executorCenter;

	@Async
	@EventListener
	public void executorListener(ExecutorEvent event) {
		ExecutorInfo info = (ExecutorInfo) event.getSource();
		info.setStatus(ExecutorStatus.STATUS2);
		ExecutorCenter.ALL_EXECUTOR.put(info.getExecuteId(), info);
		ExecutorCenter.EXECUTE_LOGS.put(info.getExecuteId(), new ArrayList<>());
		ExecutorCenter.LOOKING_CLIENTS.put(info.getExecuteId(), new ArrayList<>());
		List<WebSocketServer> list = new ArrayList<>();
		WebSocketServer wsInfo = WebSocketServer.WEBSOCKET_INFOS.get(info.getBrowserSessionId());
		list.add(wsInfo);
		ExecutorCenter.EXECUTING_CLIENTS.put(info.getExecuteId(), list);
		executorCenter.executor(info);
	}

}
