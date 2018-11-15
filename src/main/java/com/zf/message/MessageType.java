package com.zf.message;

/**
 * Created by zhangfei on 2018/11/15/015.
 */
public enum MessageType {

	TITLE(1),

	CONTENT(2),

	CLOSE(3);

	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	MessageType(int type) {
		this.type = type;
	}
}
