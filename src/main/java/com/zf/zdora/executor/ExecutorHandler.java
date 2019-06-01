package com.zf.zdora.executor;

import com.zf.zdora.client.ZdoraClient;
import com.zf.zdora.common.CommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		CommandUtil.executeCommand(zdoraClient, command, args, executeId);
		logger.info("执行完命令: " + command+" "+args);
		zdoraClient.send("执行完成: "+command+" "+args);
	}
}
