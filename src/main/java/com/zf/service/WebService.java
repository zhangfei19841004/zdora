package com.zf.service;

import com.zf.executor.ExecutorArgs;
import com.zf.executor.ExecutorEvent;
import com.zf.executor.ExecutorInfo;
import com.zf.executor.ExecutorStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;

/**
 * Created by zhangfei on 2018/10/31.
 */
@Service
public class WebService {

	@Autowired
	private ApplicationContext applicationContext;

	private AtomicLong atomic = new AtomicLong(System.currentTimeMillis());

	public void executor(ExecutorInfo info) {
		long executeId = atomic.incrementAndGet();
		info.setExecuteId(executeId);
		info.setStatus(ExecutorStatus.STATUS1);
		this.executorInfoReplace(info);
		applicationContext.publishEvent(new ExecutorEvent(info));
	}

	private void executorInfoReplace(ExecutorInfo info) {
		info.setBeforeFromServerPath(this.replaceArg(info.getBeforeFromServerPath(), info));
		info.setBeforeToClientPath(this.replaceArg(info.getBeforeToClientPath(), info));
		info.setBeforeFromClientPath(this.replaceArg(info.getBeforeFromClientPath(), info));
		info.setBeforeToServerPath(this.replaceArg(info.getBeforeToServerPath(), info));

		info.setAfterFromServerPath(this.replaceArg(info.getAfterFromServerPath(), info));
		info.setAfterToClientPath(this.replaceArg(info.getAfterToClientPath(), info));
		info.setAfterFromClientPath(this.replaceArg(info.getAfterFromClientPath(), info));
		info.setAfterToServerPath(this.replaceArg(info.getAfterToServerPath(), info));
	}

	private String replaceArg(String arg, ExecutorInfo info) {
		for (ExecutorArgs value : ExecutorArgs.values()) {
			String vn = value.getValueName();
			try {
				Field vnField = info.getClass().getField(vn);
				String vnValue = vnField.get(info).toString();
				arg = arg.replaceAll("\\$\\{" + Matcher.quoteReplacement(value.getArgName()) + "}", vnValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return arg;
	}

}
