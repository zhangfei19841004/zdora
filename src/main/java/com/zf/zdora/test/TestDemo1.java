package com.zf.zdora.test;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class TestDemo1 {

	@BeforeMethod(description = "测试方法前初始化")
	public void beforeMethod(Method m) {
		for (int i = 0; i < 5; i++) {
			Reporter.log("正在初始化...");
			this.sleep(1);
		}
	}

	@Test(description = "测试DEMO")
	public void testDemo() {
		Reporter.log("this is demo!");
		int a = 1 / 0;
		System.out.println(a);
		Assert.assertEquals("a", "b", "should be equals.");
	}

	@Test(description = "测试DEMO1")
	public void testDemo1() {
		Reporter.log("this is demo!");
		Assert.assertEquals("a", "b", "should be equals.");
	}

	@Test(description = "测试DEMO2", dataProvider = "test")
	public void testDemo2(int a) {
		Reporter.log("this is demo!");
		Assert.assertEquals(a, 1, "should be equals.");
	}

	@Test(description = "测试DEMO3")
	public void testDemo3() {
		Reporter.log("this is demo!");
		Assert.assertEquals("a", "a", "should be equals.");
	}

	public void sleep(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@DataProvider(name = "test")
	public Object[][] dataProvider() {
		return new Object[][]{{1}, {2}};
	}

}
