package com.zf.zdora.executor;

import com.alibaba.fastjson.JSON;
import com.zf.zdora.client.Run;
import com.zf.zdora.client.ZdoraClient;
import com.zf.zdora.common.CommandUtil;
import com.zf.zdora.common.FileUtils;
import com.zf.zdora.common.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

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
		if (StringUtils.isNotBlank(fromServerInfo.getBeforeToClientPath())) {
			this.downloadFormServerToClient(fromServerInfo.getBeforeToClientPath(), fromServerInfo.getBeforeFromServerPath());
			begin.setMessage("拷贝文件: 从服务端" + fromServerInfo.getBeforeFromServerPath() + " 至客户端" + fromServerInfo.getBeforeToClientPath());
			zdoraClient.send(begin.toString());
		}

		if (StringUtils.isNotBlank(fromServerInfo.getBeforeFromClientPath())) {
			this.uploadFromClientToServer(fromServerInfo.getBeforeFromClientPath(), fromServerInfo.getBeforeToServerPath());
			begin.setMessage("拷贝文件: 从客户端" + fromServerInfo.getBeforeFromClientPath() + " 至服务端" + fromServerInfo.getBeforeToServerPath());
			zdoraClient.send(begin.toString());
		}

		begin.setMessage("开始执行: " + fromServerInfo.getCommand() + " " + fromServerInfo.getArgs());
		zdoraClient.send(begin.toString());
		CommandUtil.executeCommand(zdoraClient, fromServerInfo.getCommand(), fromServerInfo.getArgs(), fromServerInfo.getExecuteId(), fromServerInfo.getIsExecutorId());
		logger.info("执行完命令: " + fromServerInfo.getCommand() + " " + fromServerInfo.getArgs());
		if (StringUtils.isNotBlank(fromServerInfo.getAfterFromClientPath())) {
			this.uploadFromClientToServer(fromServerInfo.getAfterFromClientPath(), fromServerInfo.getAfterToServerPath());
		}
		ExecutorClientInfo end = new ExecutorClientInfo();
		end.setType(2);
		end.setExecuteId(fromServerInfo.getExecuteId());
		end.setExecuteStatus(ExecutorStatus.STATUS2.getStatus());
		end.setMessage("执行完成: " + fromServerInfo.getCommand() + " " + fromServerInfo.getArgs());
		zdoraClient.send(end.toString());
		if (StringUtils.isNotBlank(fromServerInfo.getAfterToClientPath())) {
			this.downloadFormServerToClient(fromServerInfo.getAfterToClientPath(), fromServerInfo.getAfterFromServerPath());
			end.setMessage("拷贝文件: 从服务端" + fromServerInfo.getAfterFromServerPath() + " 至客户端" + fromServerInfo.getAfterToClientPath());
			zdoraClient.send(end.toString());
		}
		if (StringUtils.isNotBlank(fromServerInfo.getAfterFromClientPath())) {
			this.uploadFromClientToServer(fromServerInfo.getAfterFromClientPath(), fromServerInfo.getAfterToServerPath());
			end.setMessage("拷贝文件: 从客户端" + fromServerInfo.getAfterFromClientPath() + " 至服务端" + fromServerInfo.getAfterToServerPath());
			zdoraClient.send(end.toString());
		}
		end.setExecuteStatus(ExecutorStatus.STATUS3.getStatus());
		end.setMessage("执行结束!");
		zdoraClient.send(end.toString());
	}

	private void uploadFromClientToServer(String clientPath, String serverPath) {
		if (StringUtils.isNotBlank(clientPath)) {
			List<String> paths = new ArrayList<>();
			FileUtils.getAllFilePaths(paths, clientPath);
			for (String path : paths) {
				List<String> targetPath = this.getTargetPath(path, clientPath, serverPath);
				HttpClientUtils.upload(path, targetPath, "http://" + Run.serverHost + ":" + Run.serverPort + "/upload");
			}
		}
	}

	private List<String> getTargetPath(String path, String clientRootPath, String serverRootPath) {
		List<String> list = new ArrayList<>();
		if (StringUtils.isNotBlank(serverRootPath)) {
			File serverRootFile = new File(serverRootPath);
			list = Arrays.stream(serverRootFile.getPath().split(Matcher.quoteReplacement(File.separator))).filter(StringUtils::isNotBlank).collect(Collectors.toList());
		}
		File root = new File(clientRootPath);
		clientRootPath = root.getAbsolutePath();
		path = path.substring(0, path.lastIndexOf(File.separator));
		path = path.substring(clientRootPath.length());
		if (StringUtils.isBlank(path) || path.length() == 1) {
			return list;
		}
		path = path.substring(1);
		list.addAll(Arrays.asList(path.split(Matcher.quoteReplacement(File.separator))));
		return list;
	}

	private void downloadFormServerToClient(String clientRootPath, String serverRootPath) {
		String res = HttpClientUtils.get("http://" + Run.serverHost + ":" + Run.serverPort + "/all/files?basePath=" + serverRootPath);
		List<String> paths = JSON.parseArray(res, String.class);
		for (String path : paths) {
			File file = new File(path);
			File srp = new File(serverRootPath);
			HttpClientUtils.downLoad("http://" + Run.serverHost + ":" + Run.serverPort + "/download", srp.getPath(), file.getPath(), clientRootPath);
		}
	}

	public static void main(String[] args) throws Exception {
		String path = "";
		File f = new File(path);
		Arrays.stream(f.getPath().split(Matcher.quoteReplacement(File.separator))).filter(StringUtils::isNotBlank).forEach(System.out::println);
	}
}
