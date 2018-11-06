package com.zf.executor;

import org.springframework.context.ApplicationEvent;

/**
 * Created by zhangfei on 2018/11/5/005.
 */
public class ExecutorEvent extends ApplicationEvent {

	private String source;

	public ExecutorEvent(String source) {
		super(source);
		this.source = source;
	}
}
