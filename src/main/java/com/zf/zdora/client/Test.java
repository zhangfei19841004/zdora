package com.zf.zdora.client;

/**
 * Created by zhangfei on 2019/5/31/031.
 */
public class Test {

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(2000);
				System.out.println(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
