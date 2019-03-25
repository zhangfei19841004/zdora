package com.zf.executor;

import com.zf.message.MessageInfo;
import com.zf.message.MessageType;
import com.zf.service.WebSocketServer;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * Created by zhangfei on 2018/10/31.
 */
public class ExecutorHandler implements Callable<Object> {

	private ExecutorInfo info;

	private ApplicationContext applicationContext;

	public void setInfo(ExecutorInfo info) {
		this.info = info;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object call() throws Exception {
		WebSocketServer.sendInfo(new MessageInfo(MessageType.CONTENT, "开始执行").toString(), info.getCid());
		for (String s : info.getExecuteCases()) {
			this.executorCommand(info.getExecuteCommand() + " " + s);
		}
		WebSocketServer.sendInfo(new MessageInfo(MessageType.CONTENT, "全部执行完毕!").toString(), info.getCid());
		WebSocketServer.sendInfo(new MessageInfo(MessageType.CLOSE, "关闭!").toString(), info.getCid());
		ExecutorCenter.removeExecutorInfo(info.getExecutorId());
		applicationContext.publishEvent(new ExecutorEvent("zdora"));
		return null;
	}

	private void executorCommand(String command) {
		Process process = null;
		BufferedReader br = null;
		try {
			process = Runtime.getRuntime().exec(command);
			br = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
			String line;
			while ((line = br.readLine()) != null) {
				WebSocketServer.sendInfo(new MessageInfo(MessageType.CONTENT, line).toString(), info.getCid());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (process != null) {
				process.destroy();
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
