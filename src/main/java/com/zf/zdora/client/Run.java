package com.zf.zdora.client;

import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by zhangfei on 2019/5/30/030.
 */
public class Run {

	private static Logger logger = LoggerFactory.getLogger(Run.class);

	private String serverHost = "localhost";

	private String serverPort = "8080";

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public List<String> getLocalIPList() {
		List<String> ipList = new ArrayList<>();
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			NetworkInterface networkInterface;
			Enumeration<InetAddress> inetAddresses;
			InetAddress inetAddress;
			String ip;
			while (networkInterfaces.hasMoreElements()) {
				networkInterface = networkInterfaces.nextElement();
				inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					inetAddress = inetAddresses.nextElement();
					if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
						ip = inetAddress.getHostAddress();
						ipList.add(ip);
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ipList;
	}

	public void sleep(int sec) {
		try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void process() throws Exception {
		logger.info("begin to connect");
		String ip = this.getLocalIPList().stream().filter(t -> !"127.0.0.1".equals(t)).findFirst().get();
		ZdoraClient zc = new ZdoraClient("ws://" + serverHost + ":" + serverPort + "/websocket/" + ip+"/5");
		zc.connect();
		while (!zc.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
			logger.info("connecting...");
			this.sleep(500);
		}
		while (zc.getReadyState().equals(WebSocket.READYSTATE.OPEN) && zc.getSessionId() == null) {
			logger.info("waiting for welcome message");
			this.sleep(500);
		}
		logger.info("connected!");
		logger.info("session id is: "+zc.getSessionId());
	}

	public static void main(String[] args) throws Exception {
		Run run = new Run();
		if (args != null && args.length > 1) {
			run.setServerHost(args[0]);
			run.setServerPort(args[1]);
		}
		run.process();
	}

}
