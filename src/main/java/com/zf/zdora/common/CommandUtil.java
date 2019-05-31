package com.zf.zdora.common;

import com.zf.zdora.client.ZdoraClient;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by zhangfei on 2019/5/31/031.
 */
public class CommandUtil {

	public static void executeCommand(ZdoraClient zdoraClient, String command, String args) {
		try {
			PumpStreamHandler streamHandler = new PumpStreamHandler(new CollectingLogOutputStream(zdoraClient));
			CommandLine commandline = new CommandLine(command);
			if(StringUtils.isNotBlank(args)){
				String[] argss = args.split("\\s+");
				commandline.addArguments(argss);
			}
			DefaultExecutor exec = new DefaultExecutor();
			exec.setExitValues(null);
			exec.setStreamHandler(streamHandler);
			exec.execute(commandline);// exit code: 0=success, 1=error
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class CollectingLogOutputStream extends LogOutputStream {

		private ZdoraClient zdoraClient;

		public CollectingLogOutputStream(ZdoraClient zdoraClient) {
			this.zdoraClient = zdoraClient;
		}

		@Override
		protected void processLine(String line, int level) {
			System.out.println(line);
			zdoraClient.send(line);
		}

	}

}
