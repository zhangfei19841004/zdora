package com.zf.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zf.message.MessageType;
import com.zf.service.WebSocketInfo;
import com.zf.service.WebSocketServer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by zhangfei on 2018/10/31.
 */
@Service
public class ExecutorCenter {

	public static ConcurrentHashMap<Integer, ExecutorInfo> ALL_EXECUTOR = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<Integer, List<String>> EXECUTE_LOGS = new ConcurrentHashMap();

	public static ConcurrentHashMap<Integer, List<WebSocketServer>> LOOKING_CLIENTS = new ConcurrentHashMap();

	public void executor(ExecutorInfo info) {
		try {
			JSONObject json = new JSONObject();
			json.put("command", info.getExecuteCommand());
			json.put("args", info.getExecuteArgs());
			json.put("executeId", info.getExecuteId());
			WebSocketServer.sendInfo(info.getCid(), info.getSessionId(), MessageType.COMMAND, JSON.toJSONString(json));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
