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

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
