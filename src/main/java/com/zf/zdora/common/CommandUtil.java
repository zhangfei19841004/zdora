package com.zf.zdora.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

    public static void executeCommand(ZdoraClient zdoraClient, String command, String args, int executeId) {
        try {
            PumpStreamHandler streamHandler = new PumpStreamHandler(new CollectingLogOutputStream(zdoraClient, executeId));
            CommandLine commandline = new CommandLine(command);
            if (StringUtils.isNotBlank(args)) {
                String[] argss = args.split("(?<!\\\\)\\s+");//参数以空格进行分割，如果有参数中确实带有空格的，则加上转义符号\
                commandline.addArguments(argss);
            }
            commandline.addArgument(executeId+"");
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

        private int executeId;

        public CollectingLogOutputStream(ZdoraClient zdoraClient, int executeId) {
            this.zdoraClient = zdoraClient;
            this.executeId = executeId;
        }

        @Override
        protected void processLine(String line, int level) {
            System.out.println(line);
            JSONObject json = new JSONObject();
            json.put("executeid",executeId);
            json.put("message",line);
            zdoraClient.send(JSON.toJSONString(json));
        }

    }

}
