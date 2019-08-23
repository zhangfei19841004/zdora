package com.zf.zdora.common;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class CommandUtil1 {

	private static final String os = System.getProperty("os.name");

	private static String charset = "UTF-8";

	public static void executeCommand() {
		try {
			CommandLine commandline = new CommandLine("E:\\workspace\\zdora-test\\run.bat");
			commandline.addArgument("123456789");
			DefaultExecutor exec = new DefaultExecutor();
			exec.setExitValues(null);
			if(os.toLowerCase().startsWith("win") && StringUtils.isNotBlank(commandline.getExecutable()) &&
					(commandline.getExecutable().toLowerCase().endsWith("cmd") ||
							commandline.getExecutable().toLowerCase().endsWith("bat"))){
				charset = "GBK";
			}
			PumpStreamHandler streamHandler = new PumpStreamHandler(new CollectingLogOutputStream(CommandUtil1.charset));
			exec.setStreamHandler(streamHandler);
			exec.execute(commandline);// exit code: 0=success, 1=error

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class CollectingLogOutputStream extends LogOutputStream {

		public CollectingLogOutputStream(String charset) {
			super(charset);
		}

		@Override
		protected void processLine(String line, int level) {
			System.out.println(line);
		}

	}

	public static void main(String[] args) {
		//CommandUtil1.executeCommand();
		System.out.println(System.getProperty("os.name"));
	}

}
