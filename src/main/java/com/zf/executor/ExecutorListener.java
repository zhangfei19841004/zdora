package com.zf.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by zhangfei on 2018/11/5/005.
 */
@Component
public class ExecutorListener {

	@Autowired
	private ExecutorCenter executorCenter;

	@Async
	@EventListener
	public void executorListener(ExecutorEvent event) {
		System.out.println("listener : " + event.getSource());
		synchronized (this) {
			boolean flag = false;
			for (ExecutorInfo executorInfo : ExecutorCenter.ALL_EXECUTOR) {
				if (executorInfo.getStatus().getStatus() == ExecutorStatus.STATUS2.getStatus()) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				System.out.println("开始执行");
				executorCenter.executorCenter(1);
			}
		}
		System.out.println("释放锁");
	}


}
