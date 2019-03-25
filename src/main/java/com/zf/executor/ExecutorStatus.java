package com.zf.executor;

/**
 * Created by zhangfei on 2018/11/4.
 */
public enum ExecutorStatus {

	STATUS1(0, "等待执行"),
	STATUS2(1, "正在执行"),
	STATUS3(2, "执行完成");

	private int status;

	private String statusName;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	ExecutorStatus(int status, String statusName) {
		this.status = status;
		this.statusName = statusName;
	}
}
