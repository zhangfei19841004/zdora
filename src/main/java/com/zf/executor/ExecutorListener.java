package com.zf.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zhangfei on 2018/11/5/005.
 */
@Component
public class ExecutorListener {

	@Autowired
	private ExecutorCenter executorCenter;

	@Autowired
	private ApplicationArguments applicationArguments;

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
			if (!flag && ExecutorCenter.ALL_EXECUTOR.size() > 0) {
				System.out.println("开始执行");
				ExecutorInfo info = ExecutorCenter.ALL_EXECUTOR.get(0);
				info.setStatus(ExecutorStatus.STATUS2);
				List<String> list = applicationArguments.getNonOptionArgs();
				if (list != null && list.size() > 0) {
					info.setExecuteCommand(list.get(0));
				}
				executorCenter.executorCenter(1, info);
			}
		}
		System.out.println("释放锁");
	}


}
