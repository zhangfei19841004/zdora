package com.zf.pojo;

/**
 * Created by zhangfei on 2018/11/4.
 */
public class ExecutorToClientInfo {

	private String command;

	private String args;

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

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public long getExecuteId() {
		return executeId;
	}

	public void setExecuteId(long executeId) {
		this.executeId = executeId;
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

	public int getIsExecutorId() {
		return isExecutorId;
	}

	public void setIsExecutorId(int isExecutorId) {
		this.isExecutorId = isExecutorId;
	}
}
