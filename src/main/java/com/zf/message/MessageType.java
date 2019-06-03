package com.zf.message;

/**
 * Created by zhangfei on 2018/11/15/015.
 */
public enum MessageType {

	TITLE(1),//标题

	MESSAGE(2),//内容

	CLOSE(3),//关闭

	BROWSER(4),//浏览器信息

	CLIENT(5),//客户端信息

	SESSIONID(6),

	COMMAND(7),

	LOOK(8), //浏览器查看执行的LOG信息
	;

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
