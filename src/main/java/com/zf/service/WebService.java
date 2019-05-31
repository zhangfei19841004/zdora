package com.zf.service;

import com.zf.executor.ExecutorCenter;
import com.zf.executor.ExecutorEvent;
import com.zf.executor.ExecutorInfo;
import com.zf.executor.ExecutorStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by zhangfei on 2018/10/31.
 */
@Service
public class WebService {

	@Autowired
	private ApplicationContext applicationContext;

	public void executor(ExecutorInfo info) {
		applicationContext.publishEvent(new ExecutorEvent(info));
		//executorCenter.executorCenter(1);
	}

/*	public String getExecutorId() {
		synchronized (this) {
			return this.getId();
		}
	}

	public String getId() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String id = sdf.format(new Date());
		boolean flag = false;
		for (ExecutorInfo executorInfo : ExecutorCenter.ALL_EXECUTOR) {
			if (executorInfo.getExecutorId().equals(id)) {
				flag = true;
				break;
			}
		}
		if (flag) {
			CommonService.sleep(1);
			return this.getId();
		} else {
			return id;
		}
	}*/


}
