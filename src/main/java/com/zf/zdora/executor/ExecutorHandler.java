package com.zf.zdora.executor;

import com.zf.zdora.client.Run;
import com.zf.zdora.client.ZdoraClient;
import com.zf.zdora.common.CommandUtil;
import com.zf.zdora.common.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfei on 2019/05/31.
 */
public class ExecutorHandler extends Thread {

	private static Logger logger = LoggerFactory.getLogger(ExecutorHandler.class);

	private ZdoraClient zdoraClient;

	public void setZdoraClient(ZdoraClient zdoraClient) {
		this.zdoraClient = zdoraClient;
	}

	private String command;

	private String args;

	private int executeId;

	public void setCommand(String command) {
		this.command = command;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public void setExecuteId(int executeId) {
		this.executeId = executeId;
	}

	@Override
	public void run() {
		ExecutorClientInfo begin = new ExecutorClientInfo();
		begin.setType(2);
		begin.setExecuteId(executeId);
		begin.setExecuteStatus(ExecutorStatus.STATUS2.getStatus());
		begin.setMessage("开始执行: " + command + " " + args);
		zdoraClient.send(begin.toString());
		CommandUtil.executeCommand(zdoraClient, command, args, executeId);
		logger.info("执行完命令: " + command + " " + args);
		ExecutorClientInfo end = new ExecutorClientInfo();
		end.setType(2);
		end.setExecuteId(executeId);
		end.setExecuteStatus(ExecutorStatus.STATUS3.getStatus());
		end.setMessage("执行完成: " + command + " " + args);
		zdoraClient.send(end.toString());
		List<String> targetPath = new ArrayList<>();
		targetPath.add("123");
		targetPath.add("456");
		//HttpClientUtils.upload("C:\\Users\\zhangfei\\Desktop\\zdora\\123\\zdora-1.0.jar", targetPath, "http://" + Run.serverHost + ":" + Run.serverPort + "/upload");
	}
}
