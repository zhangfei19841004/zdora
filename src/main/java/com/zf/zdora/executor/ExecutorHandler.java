package com.zf.zdora.executor;

import com.zf.zdora.client.Run;
import com.zf.zdora.client.ZdoraClient;
import com.zf.zdora.common.CommandUtil;
import com.zf.zdora.common.FileUtils;
import com.zf.zdora.common.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfei on 2019/05/31.
 */
public class ExecutorHandler extends Thread {

    private static Logger logger = LoggerFactory.getLogger(ExecutorHandler.class);

    private ZdoraClient zdoraClient;

    public void setZdoraClient(ZdoraClient zdoraClient) {
        this.zdoraClient = zdoraClient;
    }

    private ExecutorFromServerInfo fromServerInfo;

    public void setFromServerInfo(ExecutorFromServerInfo fromServerInfo) {
        this.fromServerInfo = fromServerInfo;
    }

    @Override
    public void run() {
        ExecutorClientInfo begin = new ExecutorClientInfo();
        begin.setType(2);
        begin.setExecuteId(fromServerInfo.getExecuteId());
        begin.setExecuteStatus(ExecutorStatus.STATUS2.getStatus());
        if (StringUtils.isNotBlank(fromServerInfo.getBeforeFromClientPath())) {
            List<String> paths = new ArrayList<>();
            FileUtils.getAllFilePaths(paths, fromServerInfo.getBeforeFromClientPath());
            for (String path : paths) {
                //HttpClientUtils.upload(path, targetPath, "http://" + Run.serverHost + ":" + Run.serverPort + "/upload");
            }
        }
        begin.setMessage("开始执行: " + fromServerInfo.getCommand() + " " + fromServerInfo.getArgs());
        zdoraClient.send(begin.toString());
        CommandUtil.executeCommand(zdoraClient, fromServerInfo.getCommand(), fromServerInfo.getArgs(), fromServerInfo.getExecuteId());
        logger.info("执行完命令: " + fromServerInfo.getCommand() + " " + fromServerInfo.getArgs());
        ExecutorClientInfo end = new ExecutorClientInfo();
        end.setType(2);
        end.setExecuteId(fromServerInfo.getExecuteId());
        end.setExecuteStatus(ExecutorStatus.STATUS3.getStatus());
        end.setMessage("执行完成: " + fromServerInfo.getCommand() + " " + fromServerInfo.getArgs());
        zdoraClient.send(end.toString());
    }

    public static void main(String[] args) throws Exception {
        String path = "/Users/zhangfei/Desktop/zdora-client";
        List<String> paths = new ArrayList<>();
        FileUtils.getAllFilePaths(paths, path);
        paths.forEach(System.out::println);
    }
}
