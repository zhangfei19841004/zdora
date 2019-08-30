package com.zf.task;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@Component
public class ExecutorTask1 implements SchedulingConfigurer {

	private String cron = "0/10 * * * * ?";

	private String name = "测试1";

	private boolean stop = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
		scheduledTaskRegistrar.addTriggerTask(doTask(), getTrigger());
		/*Iterator<ScheduledTask> it = scheduledTaskRegistrar.getScheduledTasks().iterator();
		while(it.hasNext()){
			ScheduledTask task = it.next();
			System.out.println(task.getTask().toString());
		}*/
	}

	/**
	 * 业务执行方法
	 *
	 * @return
	 */
	private Runnable doTask() {
		return () -> {
			if (stop) {
				return;
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 业务逻辑
			System.out.println(name + ",时间为:" + simpleDateFormat.format(new Date()));
			System.out.println(Thread.currentThread().getName());
		};
	}

	/**
	 * 业务触发器
	 *
	 * @return
	 */
	private Trigger getTrigger() {
		return triggerContext -> {
			// 触发器
			CronTrigger trigger = new CronTrigger(cron);
			return trigger.nextExecutionTime(triggerContext);
		};
	}

}
