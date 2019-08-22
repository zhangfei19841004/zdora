package com.zf.executor;

import com.alibaba.fastjson.JSON;
import com.zf.message.MessageType;
import com.zf.pojo.ExecutorToClientInfo;
import com.zf.websocketservice.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangfei on 2018/10/31.
 */
@Service
public class ExecutorCenter {

	public static ConcurrentHashMap<Long, ExecutorInfo> ALL_EXECUTOR = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<Long, List<String>> EXECUTE_LOGS = new ConcurrentHashMap();//执行LOG

	public static ConcurrentHashMap<Long, List<WebSocketServer>> LOOKING_CLIENTS = new ConcurrentHashMap();//浏览器中正在执行或执行完成或等待执行的，用于回显运行LOG

	public static ConcurrentHashMap<Long, List<WebSocketServer>> EXECUTING_CLIENTS = new ConcurrentHashMap();//浏览器中正在执行的，用于回显运行状态

	public void executor(ExecutorInfo info) {
		try {
			ExecutorToClientInfo toClientInfo = new ExecutorToClientInfo();
			BeanUtils.copyProperties(info, toClientInfo);
			toClientInfo.setExecuteId(info.getExecuteId());
			toClientInfo.setCommand(info.getExecuteCommand());
			toClientInfo.setArgs(info.getExecuteArgs());
			WebSocketServer.sendInfo(info.getCid(), info.getSessionId(), MessageType.COMMAND, JSON.toJSONString(toClientInfo));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
