package com.zf.websocketservice;

import javax.websocket.Session;

/**
 * Created by zhangfei on 2019/6/3/003.
 */
public class WebSocketInfo {

	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	//接收客户端ip
	private String cid = "";
	//区分是从浏览器过来，还是client过来
	private int messageType;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
}
