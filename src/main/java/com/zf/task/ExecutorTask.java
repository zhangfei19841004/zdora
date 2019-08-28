package com.zf.task;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

@Component
public class ExecutorTask implements SchedulingConfigurer {



	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {

	}

}
