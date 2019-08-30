package com.zf.web.controller;

import com.zf.task.ExecutorTask;
import com.zf.utils.ResponseUtil;
import com.zf.utils.ResponseUtil.ResponseInfo;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;


@Controller
public class TestController {

	@ResponseBody
	@RequestMapping("/test")
	public ResponseInfo test() {
		Iterator<ScheduledTask> it = ExecutorTask.TASK_REGISTRAR.getScheduledTasks().iterator();
		Runnable runable = ExecutorTask.SCHEDULED_RUNABLE.get("测试");
		while(it.hasNext()){
			ScheduledTask task = it.next();
			if(task.getTask().getRunnable() == runable){
				task.cancel();
				ExecutorTask.SCHEDULED_RUNABLE.remove("测试");
			}
		}
		return ResponseUtil.getSuccessResponse();
	}


}
