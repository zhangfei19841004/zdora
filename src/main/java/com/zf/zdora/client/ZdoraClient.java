package com.zf.zdora.client;

import com.alibaba.fastjson.JSON;
import com.zf.zdora.executor.ExecutorCenter;
import com.zf.zdora.message.MessageInfo;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by zhangfei on 2019/5/30/030.
 */
public class ZdoraClient extends WebSocketClient {

	private static Logger logger = LoggerFactory.getLogger(ZdoraClient.class);

	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public ZdoraClient(String url) throws URISyntaxException {
		super(new URI(url));
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		logger.info("------ client onOpen ------");
	}

	@Override
	public void onMessage(String s) {
		logger.info("-------- 接收到服务端数据： " + s + "--------");
		MessageInfo info = JSON.parseObject(s, MessageInfo.class);
		if (info.getType() == 6) {
			sessionId = info.getMessage();
		}else if(info.getType() == 7){
			ExecutorCenter ec = new ExecutorCenter();
			ec.executorCenter(info.getMessage(), this);
			logger.info("执行命令: "+info.getMessage());
		}
	}

	@Override
	public void onClose(int i, String s, boolean b) {
		logger.info("------ client onClose ------");
	}

	@Override
	public void onError(Exception e) {
		logger.info("------ client onError ------");
	}
}
