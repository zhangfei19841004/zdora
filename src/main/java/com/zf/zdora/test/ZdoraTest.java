package com.zf.zdora.test;

import com.zf.zdora.common.HttpClientUtils;
import com.zf.zson.ZSON;
import com.zf.zson.result.ZsonResult;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class ZdoraTest {

	@Test(description = "测试GET请求")
	public void test() {
		Reporter.log("开始测试IP请求", true);
		String res = HttpClientUtils.get("http://ip-api.com/json/?lang=zh-CN");
		Assert.assertNotNull(res);
		ZsonResult zr = ZSON.parseJson(res);
		Assert.assertEquals(zr.getString("/countryCode"), "CN");
		Assert.assertEquals(zr.getString("/country"), "中国");
		Reporter.log("结束测试IP请求", true);
	}


}
