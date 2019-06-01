package com.zf.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zf.message.MessageType;
import com.zf.service.WebSocketServer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfei on 2018/10/31.
 */
@Service
public class ExecutorCenter {

	public static List<ExecutorInfo> ALL_EXECUTOR = new ArrayList<>();

	public void executor(ExecutorInfo info) {
		try {
			JSONObject json = new JSONObject();
			json.put("command",info.getExecuteCommand());
			json.put("args",info.getExecuteArgs());
			json.put("executeId",info.getExecuteId());
			WebSocketServer.sendInfo(info.getCid(), info.getSessionId(), MessageType.COMMAND, JSON.toJSONString(json));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
