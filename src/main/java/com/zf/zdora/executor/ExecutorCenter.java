package com.zf.zdora.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zf.zdora.client.ZdoraClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangfei on 2019/5/31/031.
 */
public class ExecutorCenter {

	public static ExecutorService service = Executors.newFixedThreadPool(5);

	public void executorCenter(String message, ZdoraClient zdoraClient) {
		JSONObject json = JSON.parseObject(message);
		ExecutorHandler eh = new ExecutorHandler();
		eh.setCommand(json.getString("command"));
		eh.setArgs(json.getString("args"));
		eh.setZdoraClient(zdoraClient);
		service.execute(eh);
	}
}
