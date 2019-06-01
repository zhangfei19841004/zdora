package com.zf.zdora.client;

import java.util.stream.Stream;

/**
 * Created by zhangfei on 2019/5/31/031.
 */
public class Test {

    public static void main(String[] args) {
        String id = "";
        if (args != null && args.length > 1) {
            id = args[1];
        }
        String finalId = id;
        Stream.iterate(0, i -> i + 1).limit(10).forEach(i -> {
            try {
                Thread.sleep(2000);
                System.out.println(finalId + "----" + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}
