package com.zf.web.controller;

import com.zf.executor.ExecutorCenter;
import com.zf.executor.ExecutorInfo;
import com.zf.message.MessageType;
import com.zf.service.CommonService;
import com.zf.service.WebService;
import com.zf.service.WebSocketInfo;
import com.zf.service.WebSocketServer;
import com.zf.utils.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Comparator;
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
		List<ExecutorInfo> executors = ExecutorCenter.ALL_EXECUTOR.values().stream().sorted(Comparator.comparingInt(o -> o.executeId)).collect(Collectors.toList());
		map.put("executors",executors);
		return "welcome-new";
	}

	//推送数据接口,可用于全网推送
	@ResponseBody
	@RequestMapping("/socket/push/{cid}")
	public ResponseUtil.ResponseInfo pushToWeb(@PathVariable String cid, String message) {
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
	public ResponseUtil.ResponseInfo push(ExecutorInfo info) {
		try {
			if (StringUtils.isBlank(info.getCid()) || StringUtils.isBlank(info.getExecuteCommand())) {
				return ResponseUtil.getFailedResponse("参数不全");
			}
			webService.executor(info);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.getFailedResponse(e.getMessage());
		}
		return ResponseUtil.getSuccessResponse();
	}

}
