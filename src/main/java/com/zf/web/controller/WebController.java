package com.zf.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.zf.executor.ExecutorCenter;
import com.zf.executor.ExecutorInfo;
import com.zf.executor.ExecutorStatus;
import com.zf.message.MessageType;
import com.zf.service.CommonService;
import com.zf.service.WebService;
import com.zf.service.WebSocketInfo;
import com.zf.service.WebSocketServer;
import com.zf.utils.ResponseUtil;
import com.zf.utils.ResponseUtil.ResponseInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class WebController {

	@Value("${workspace.dir}")
	private String workspaceDir;

	@Autowired
	private WebService webService;

	@RequestMapping(value = "/index")
	public String index(Map<String, Object> map, HttpServletRequest request) throws Exception {
		map.put("cid", CommonService.getIpAddr(request));
		List<WebSocketInfo> list = WebSocketServer.WEBSOCKET_INFOS.values().stream()
				.filter(ws -> ws.getWebSocketInfo().getMessageType() == MessageType.CLIENT.getType())
				.filter(ws -> ws != null).map(ws -> ws.getWebSocketInfo()).collect(Collectors.toList());
		map.put("clients", list);
		map.put("clientsCount", list.size());
		List<ExecutorInfo> executors = ExecutorCenter.ALL_EXECUTOR.values().stream().sorted((o1, o2) -> o2.executeId - o1.executeId).collect(Collectors.toList());
		map.put("executors", executors);
		return "welcome-new";
	}

	//推送数据接口,可用于全网推送
	@ResponseBody
	@RequestMapping("/socket/push/{cid}")
	public ResponseInfo pushToWeb(@PathVariable String cid, String message) {
		try {
			WebSocketServer.sendInfo(null, null, MessageType.MESSAGE, message);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseUtil.getFailedResponse(cid + "#" + e.getMessage());
		}
		return ResponseUtil.getSuccessResponse();
	}

	@ResponseBody
	@RequestMapping("/push")
	public ResponseInfo push(ExecutorInfo info) {
		try {
			if (StringUtils.isBlank(info.getCid()) || StringUtils.isBlank(info.getExecuteCommand())) {
				return ResponseUtil.getFailedResponse("参数不全");
			}
			webService.executor(info);
			SerializeConfig config = new SerializeConfig();
			config.configEnumAsJavaBean(ExecutorStatus.class);
			String s = JSON.toJSONString(info, config);
			return ResponseUtil.getSuccessResponse(JSON.parseObject(s));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.getFailedResponse(e.getMessage());
		}
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public ResponseInfo upload(@RequestParam("fn") String fn, @RequestParam("file") MultipartFile[] files) throws IOException {
		if (files.length == 0) {
			return ResponseUtil.getFailedResponse();
		}
		String subDirs = null;
		if (StringUtils.isNotBlank(fn)) {
			List<String> dirs;
			try {
				dirs = JSON.parseArray(fn, String.class);
				subDirs = String.join(File.separator, dirs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (MultipartFile file : files) {
			if (file != null) {
				String fileName = file.getOriginalFilename();// 取得当前上传文件的文件名称
				if (fileName.trim() != "") {// 如果名称不为"",说明该文件存在，否则说明该文件不存在
					String filePath;
					if (StringUtils.isNotBlank(subDirs)) {
						filePath = workspaceDir + File.separator + subDirs + File.separator + fileName;
					} else {
						filePath = workspaceDir + File.separator + fileName;
					}
					File localFile = new File(filePath);
					if (!localFile.getParentFile().exists()) {
						localFile.getParentFile().mkdirs();
					}
					file.transferTo(localFile);
				}
			}
		}
		return ResponseUtil.getSuccessResponse();
	}

	@RequestMapping(value = "/download")
	public void download(String fn, HttpServletResponse response) {
		String filePath = workspaceDir + File.separator + fn;
		File file = new File(filePath);
		if (file.isDirectory()) {
			return;
		}
		if (!file.exists()) {
			return;
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());
		InputStream inputStream = null;
		OutputStream os = null;
		try {
			inputStream = new FileInputStream(file);
			os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
			os.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
