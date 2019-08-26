package com.zf.service;

import com.zf.exceptions.BusinessException;
import com.zf.executor.ExecutorArgs;
import com.zf.executor.ExecutorEvent;
import com.zf.executor.ExecutorInfo;
import com.zf.executor.ExecutorStatus;
import com.zf.utils.ResponseUtil.ResponseConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Optional;
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
		this.checkAndTrimExecutorInfo(info);
		this.executorInfoReplace(info);
		applicationContext.publishEvent(new ExecutorEvent(info));
	}

	private void checkAndTrimExecutorInfo(ExecutorInfo info) {
		Optional<ExecutorInfo> infoOptional = Optional.of(info);
		infoOptional.map(t -> t.getExecuteCommand()).map(t -> {
			info.setExecuteCommand(t.trim());
			return t;
		}).orElseThrow(() -> new BusinessException(ResponseConstants.FAILED.getRetCode(), "执行命令不能为空"));
		infoOptional.map(t -> t.getExecuteArgs()).map(t -> {
			info.setExecuteArgs(t.trim());
			return t;
		}).orElse(null);
	}

	private void executorInfoReplace(ExecutorInfo info) {
		info.setExecuteCommand(this.replaceArg(info.getExecuteCommand(), info));
		info.setExecuteArgs(this.replaceArg(info.getExecuteArgs(), info));
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
		if (StringUtils.isBlank(arg)) {
			return arg;
		}
		for (ExecutorArgs value : ExecutorArgs.values()) {
			String vn = value.getValueName();
			try {
				Field vnField = info.getClass().getDeclaredField(vn);
				vnField.setAccessible(true);
				String vnValue = vnField.get(info).toString();
				arg = arg.replaceAll("\\$\\{" + Matcher.quoteReplacement(value.getArgName()) + "}", vnValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return arg;
	}

}
