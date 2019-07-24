package com.zf.executor;

import com.alibaba.fastjson.JSON;

/**
 * Created by zhangfei on 2019/6/3/003.
 */
public class ExecutorClientInfo {

	private int type;

	private int executeId;

	private String message;

	private int executeStatus;

	public int getExecuteId() {
		return executeId;
	}

	public void setExecuteId(int executeId) {
		this.executeId = executeId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(int executeStatus) {
		this.executeStatus = executeStatus;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ExecutorClientInfo() {
	}

	public ExecutorClientInfo(int type, int executeId, String message, int executeStatus) {
		this.type = type;
		this.executeId = executeId;
		this.message = message;
		this.executeStatus = executeStatus;
	}

	public static ExecutorClientInfo getInstance(int type, int executeId, String message, int executeStatus) {
		return new ExecutorClientInfo(type, executeId, message, executeStatus);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
