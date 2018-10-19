package com.zf.web.controller;

import com.zf.service.WebSocketServer;
import com.zf.utils.ResponseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;


@Controller
public class WebController {

	@RequestMapping(value = "/index")
	public String index(Map<String, Object> map) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("cid",uuid);
		return "welcome";
	}

	//推送数据接口
	@ResponseBody
	@RequestMapping("/socket/push/{cid}")
	public ResponseUtil.ResponseInfo pushToWeb(@PathVariable String cid, String message) {
		try {
			WebSocketServer.sendInfo(message, cid);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseUtil.getFailedResponse(cid + "#" + e.getMessage());
		}
		return ResponseUtil.getSuccessResponse();
	}

}
