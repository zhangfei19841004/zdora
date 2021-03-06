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

/**
 * ExecutorTask与ExecutorTask1主要是为了动态设置定时任务的调试，
 * 在接收到添加定时任务的命令后，就new一个ExecutorTask对象，configureTasks方法会把当前task任务给添加进去
 * 目前整个流程已调通，在TestController中可以去cancel已添加进去的task任务。
 */
//@Component
public class ExecutorTask implements SchedulingConfigurer {

	private String cron = "0/10 * * * * ?";

	private String name = "测试";

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

	public static ScheduledTaskRegistrar TASK_REGISTRAR = null;

	public static Map<String, Runnable> SCHEDULED_RUNABLE = new ConcurrentHashMap<>();

	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
		Runnable runable = doTask();
		scheduledTaskRegistrar.addTriggerTask(runable, getTrigger());
		TASK_REGISTRAR = scheduledTaskRegistrar;
		SCHEDULED_RUNABLE.put(name, runable);
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
