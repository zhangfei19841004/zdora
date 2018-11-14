package com.zf.executor;

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
	    /*for (int i = 0; i < 10; i++) {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String d = sdf.format(new Date());
            WebSocketServer.sendInfo(d, "0:0:0:0:0:0:0:1");
            WebSocketServer.sendInfo(d, "127.0.0.1");
            CommonService.sleep(2);
        }*/
		WebSocketServer.sendInfo("开始执行", info.getCid());
		this.executorCommand("");
		WebSocketServer.sendInfo("全部执行完毕!", info.getCid());
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
			String line = null;
			while ((line = br.readLine()) != null) {
				WebSocketServer.sendInfo(line, info.getCid());
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
