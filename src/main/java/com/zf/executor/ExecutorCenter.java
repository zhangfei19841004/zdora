package com.zf.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangfei on 2018/10/31.
 */
@Service
public class ExecutorCenter {

	@Autowired
	private ApplicationContext applicationContext;

	public static List<ExecutorInfo> ALL_EXECUTOR = new ArrayList<>();

	private int threadPoolSize = 10;

	private ExecutorService service = Executors.newFixedThreadPool(threadPoolSize);

	@Async
	public void executorCenter(int caseCount, ExecutorInfo info) {
		List<ExecutorHandler> c = new ArrayList<>(caseCount);
		for (int i = 0; i < caseCount; i++) {
			ExecutorHandler eh = new ExecutorHandler();
			eh.setInfo(info);
			eh.setApplicationContext(applicationContext);
			c.add(eh);
		}
		try {
			service.invokeAll(c);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static ExecutorInfo getExecutorInfo(String executorId) {
		for (ExecutorInfo executorInfo : ALL_EXECUTOR) {
			if (executorId.equals(executorInfo.getExecutorId())) {
				return executorInfo;
			}
		}
		return null;
	}

	public static void removeExecutorInfo(String executorId) {
		Iterator<ExecutorInfo> it = ALL_EXECUTOR.iterator();
		while (it.hasNext()) {
			ExecutorInfo info = it.next();
			if (executorId.equals(info.getExecutorId())) {
				it.remove();
			}
		}
	}
}
