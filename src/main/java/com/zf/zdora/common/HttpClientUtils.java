package com.zf.zdora.common;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {

	private final static int TIMOUT = 1000 * 60 * 5;

	public static String post(String url, String body) {
		CloseableHttpClient httpClient = null;
		HttpPost httpPost = null;
		try {
			httpClient = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMOUT).setConnectTimeout(TIMOUT).build();
			httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			httpPost.addHeader("Content-type", "application/json; charset=utf-8");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setEntity(new StringEntity(body, Consts.UTF_8));
			CloseableHttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				return EntityUtils.toString(httpEntity, Consts.UTF_8);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpPost != null) {
					httpPost.releaseConnection();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String get(String url) {
		CloseableHttpClient httpClient = null;
		HttpGet httpGet = null;
		try {
			httpClient = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMOUT).setConnectTimeout(TIMOUT).build();
			httpGet = new HttpGet(url);
			httpGet.setConfig(requestConfig);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			HttpEntity httpEntity = response.getEntity();
			return EntityUtils.toString(httpEntity, "utf-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpGet != null) {
					httpGet.releaseConnection();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String post(String url, Map<String, String> params) {
		CloseableHttpClient httpClient = null;
		HttpPost httpPost = null;
		try {
			httpClient = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMOUT).setConnectTimeout(TIMOUT).build();
			httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			for (String pKey : params.keySet()) {
				ps.add(new BasicNameValuePair(pKey, params.get(pKey)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(ps));
			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			return EntityUtils.toString(httpEntity, "utf-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpPost != null) {
					httpPost.releaseConnection();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
