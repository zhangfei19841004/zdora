package com.zf.executor;

import com.alibaba.fastjson.JSON;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by zhangfei on 2018/11/5/005.
 */
@Component
public class ExecutorListener {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ExecutorListener.class);

	@Autowired
	private ExecutorCenter executorCenter;

	@Async
	@EventListener
	public void executorListener(ExecutorEvent event) {
		logger.info("listener : " + JSON.toJSONString(event.getSource()));
		ExecutorInfo info = (ExecutorInfo) event.getSource();
		info.setStatus(ExecutorStatus.STATUS2);
		logger.info("开始执行");
		ExecutorCenter.ALL_EXECUTOR.add(info);
		executorCenter.executor(info);
	}


}
