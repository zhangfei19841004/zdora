package com.zf.zdora.common;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.List;

public class HttpClientUtils {

	private final static int TIMOUT = 1000 * 60 * 5;

	public static String upload(String filePath, List<String> targetPath, String url) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpClient = HttpClientBuilder.create().build();
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(TIMOUT).setSocketTimeout(TIMOUT).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			//multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
			File file = new File(filePath);
			//multipartEntityBuilder.addBinaryBody("file", file,ContentType.create("image/png"),"abc.pdf");
			//当设置了setSocketTimeout参数后，以下代码上传PDF不能成功，将setSocketTimeout参数去掉后此可以上传成功。上传图片则没有个限制
			//multipartEntityBuilder.addBinaryBody("file",file,ContentType.create("application/octet-stream"),"abd.pdf");
			if (file.isFile()) {
				multipartEntityBuilder.addBinaryBody("file", file);
			} else {
				File[] files = file.listFiles();
				for (File f : files) {
					multipartEntityBuilder.addBinaryBody("file", f);
				}
			}
			multipartEntityBuilder.addPart("fn", new StringBody(JSON.toJSONString(targetPath), ContentType.TEXT_PLAIN));
			HttpEntity httpEntity = multipartEntityBuilder.build();
			httpPost.setEntity(httpEntity);
			httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

			}
			return EntityUtils.toString(httpResponse.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpResponse != null) {
					httpResponse.close();
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

	public static void downLoad(String url, String serverRootPath,String fileName, String localDir) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse httpResponse;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(TIMOUT).setSocketTimeout(TIMOUT).build();
		BufferedWriter out = null;
		BufferedReader in = null;

		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(requestConfig);
			httpGet.addHeader("fn", serverRootPath+File.separator+fileName);

			httpResponse = httpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));

			long length = entity.getContentLength();
			if (length <= 0) {
				return;
			}

			File file = new File(localDir + File.separator + fileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (file.exists()) {
				file.delete();
			}
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			String line;
			boolean flag = false;
			while ((line = in.readLine()) != null) {
				if (flag) {
					out.write("\r\n");
				} else {
					flag = true;
				}
				out.write(line);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

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

	public static void main(String[] args) throws IOException {
		//HttpClientUtils.upload("C:\\Users\\zhangfei\\Desktop" + File.separator + "part-r-00000.gz", "http://localhost:8081/dailyuser/callback", "ecc3fed47446c989");
		//HttpClientUtils.downLoad("http://localhost:8081/datapost/send", "tv_info.txt", "d:/data/sync_data1");
	}

}
