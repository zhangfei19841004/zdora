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
		ExecutorFromServerInfo fromServerInfo = JSON.parseObject(message, ExecutorFromServerInfo.class);
		ExecutorHandler eh = new ExecutorHandler();
		eh.setFromServerInfo(fromServerInfo);
		eh.setZdoraClient(zdoraClient);
		service.execute(eh);
	}
}
