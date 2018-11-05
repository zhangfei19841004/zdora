package com.zf.executor;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * Created by zhangfei on 2018/11/5/005.
 */
public class ExecutorEvent extends ApplicationEvent {
	public ExecutorEvent(Object source) {
		super(source);
	}
}
