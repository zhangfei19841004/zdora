package com.zf.zdora.common;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.IOException;
import java.nio.charset.Charset;

public class CommandUtil1 {

	public static void executeCommand() {
		try {
			PumpStreamHandler streamHandler = new PumpStreamHandler(new CollectingLogOutputStream());
			CommandLine commandline = new CommandLine("E:\\workspace\\zdora-test\\run.bat");
			commandline.addArgument("123456789");
			DefaultExecutor exec = new DefaultExecutor();
			exec.setExitValues(null);

			exec.setStreamHandler(streamHandler);
			exec.execute(commandline);// exit code: 0=success, 1=error

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class CollectingLogOutputStream extends LogOutputStream {

		@Override
		protected void processLine(String line, int level) {
			System.out.println(new String(new String(line.getBytes(Charset.forName("utf-8"))).getBytes(Charset.forName("GBK"))));
		}

	}

	public static void main(String[] args) {
		CommandUtil1.executeCommand();
	}

}
