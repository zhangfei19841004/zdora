package com.zf.executor;

/**
 * Created by zhangfei on 2018/11/4.
 */
public class ExecutorInfo {

	public String executeCommand;

	public String executeArgs;

	public ExecutorStatus status;

	public String cid;//客户端IP

	public String sessionId;

	public int executeId;

	public ExecutorStatus getStatus() {
		return status;
	}

	public void setStatus(ExecutorStatus status) {
		this.status = status;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getExecuteCommand() {
		return executeCommand;
	}

	public void setExecuteCommand(String executeCommand) {
		this.executeCommand = executeCommand;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getExecuteArgs() {
		return executeArgs;
	}

	public void setExecuteArgs(String executeArgs) {
		this.executeArgs = executeArgs;
	}

	public int getExecuteId() {
		return executeId;
	}

	public void setExecuteId(int executeId) {
		this.executeId = executeId;
	}
}
