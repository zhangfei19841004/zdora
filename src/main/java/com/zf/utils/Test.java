package com.zf.utils;

import java.io.*;

/**
 * Created by zhangfei on 2018/11/4.
 */
public class Test {

    public void test(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File("/Users/zhangfei/Desktop/123.txt");
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true),"UTF-8"));
                    int index=0;
                    while(index<100){
                        bw.write(""+index);
                        bw.newLine();
                        bw.flush();
                        Thread.sleep(500);
                        index++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        File file = new File("/Users/zhangfei/Desktop/123.txt");
        try {
            Thread.sleep(5000);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8")) ;
            String line;
            while(true){
                if((line=br.readLine())!=null){
                    System.out.println(line);
                    if(line.equals("99")){
                        break;
                    }
                }else{
                    Thread.sleep(500);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Test().test();
    }


}
