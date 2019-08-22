package com.zf.executor;

/**
 * Created by zhangfei on 2018/11/4.
 */
public class ExecutorInfo {

	private String executeCommand;

	private String executeArgs;

	private ExecutorStatus status;

	private String cid;//客户端IP

	private String sessionId;

	private String browserSessionId;

	private long executeId;

	private int isExecutorId;

	private String beforeFromServerPath;
	private String beforeToClientPath;
	private String beforeFromClientPath;
	private String beforeToServerPath;

	private String afterFromServerPath;
	private String afterToClientPath;
	private String afterFromClientPath;
	private String afterToServerPath;

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

	public long getExecuteId() {
		return executeId;
	}

	public void setExecuteId(long executeId) {
		this.executeId = executeId;
	}

	public int getIsExecutorId() {
		return isExecutorId;
	}

	public void setIsExecutorId(int isExecutorId) {
		this.isExecutorId = isExecutorId;
	}

	public String getBeforeFromServerPath() {
		return beforeFromServerPath;
	}

	public void setBeforeFromServerPath(String beforeFromServerPath) {
		this.beforeFromServerPath = beforeFromServerPath;
	}

	public String getBeforeToClientPath() {
		return beforeToClientPath;
	}

	public void setBeforeToClientPath(String beforeToClientPath) {
		this.beforeToClientPath = beforeToClientPath;
	}

	public String getBeforeFromClientPath() {
		return beforeFromClientPath;
	}

	public void setBeforeFromClientPath(String beforeFromClientPath) {
		this.beforeFromClientPath = beforeFromClientPath;
	}

	public String getBeforeToServerPath() {
		return beforeToServerPath;
	}

	public void setBeforeToServerPath(String beforeToServerPath) {
		this.beforeToServerPath = beforeToServerPath;
	}

	public String getAfterFromServerPath() {
		return afterFromServerPath;
	}

	public void setAfterFromServerPath(String afterFromServerPath) {
		this.afterFromServerPath = afterFromServerPath;
	}

	public String getAfterToClientPath() {
		return afterToClientPath;
	}

	public void setAfterToClientPath(String afterToClientPath) {
		this.afterToClientPath = afterToClientPath;
	}

	public String getAfterFromClientPath() {
		return afterFromClientPath;
	}

	public void setAfterFromClientPath(String afterFromClientPath) {
		this.afterFromClientPath = afterFromClientPath;
	}

	public String getAfterToServerPath() {
		return afterToServerPath;
	}

	public void setAfterToServerPath(String afterToServerPath) {
		this.afterToServerPath = afterToServerPath;
	}

	public String getBrowserSessionId() {
		return browserSessionId;
	}

	public void setBrowserSessionId(String browserSessionId) {
		this.browserSessionId = browserSessionId;
	}
}
