package com.zf.message;

import com.alibaba.fastjson.JSON;

/**
 * Created by zhangfei on 2018/11/15/015.
 */
public class MessageInfo {

	private int type;

	private String message;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageInfo(int type, String message) {
		this.type = type;
		this.message = message;
	}

	public MessageInfo(MessageType type, String message) {
		this.type = type.getType();
		this.message = message;
	}

	public static MessageInfo getInstance(MessageType type, String message) {
		return new MessageInfo(type, message);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
