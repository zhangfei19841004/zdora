package com.zf.zdora.common;

import com.zf.zdora.client.ZdoraClient;
import com.zf.zdora.executor.ExecutorClientInfo;
import com.zf.zdora.executor.ExecutorStatus;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by zhangfei on 2019/5/31/031.
 */
public class CommandUtil {

	private static final String os = System.getProperty("os.name");

	private static String charset = "UTF-8";

	public static void executeCommand(ZdoraClient zdoraClient, String command, String args, long executeId, int isExecutorId) {
		try {
			CommandLine commandline = CommandLine.parse(command);
			if (StringUtils.isNotBlank(args)) {
				String[] argss = args.split("(?<!\\\\)\\s+");//参数以空格进行分割，如果有参数中确实带有空格的，则加上转义符号\
				commandline.addArguments(argss);
			}
			if (isExecutorId == 1) {
				commandline.addArgument(executeId + "");
			}
			DefaultExecutor exec = new DefaultExecutor();
			exec.setExitValues(null);

			if (os.toLowerCase().startsWith("win") && StringUtils.isNotBlank(commandline.getExecutable()) &&
					(commandline.getExecutable().toLowerCase().endsWith("cmd") ||
							commandline.getExecutable().toLowerCase().endsWith("bat"))) {
				charset = "GBK";
			}
			PumpStreamHandler streamHandler = new PumpStreamHandler(new CollectingLogOutputStream(zdoraClient, executeId, charset));
			exec.setStreamHandler(streamHandler);
			exec.execute(commandline);// exit code: 0=success, 1=error
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class CollectingLogOutputStream extends LogOutputStream {

		private ZdoraClient zdoraClient;

		private long executeId;

		public CollectingLogOutputStream(ZdoraClient zdoraClient, long executeId, String charset) {
			super(charset);
			this.zdoraClient = zdoraClient;
			this.executeId = executeId;
		}

		@Override
		protected void processLine(String line, int level) {
			System.out.println(line);
			ExecutorClientInfo info = new ExecutorClientInfo();
			info.setType(2);
			info.setExecuteId(executeId);
			info.setExecuteStatus(ExecutorStatus.STATUS2.getStatus());
			info.setMessage(line);
			zdoraClient.send(info.toString());
		}

	}

}
