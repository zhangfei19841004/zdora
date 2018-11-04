package com.zf.executor;

import com.zf.service.CommonService;
import com.zf.service.WebSocketServer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Created by zhangfei on 2018/10/31.
 */
public class ExecutorHandler implements Callable<Object> {

    @Override
    public Object call() throws Exception {
        for (int i = 0; i < 10; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String d = sdf.format(new Date());
            WebSocketServer.sendInfo(d, "0:0:0:0:0:0:0:1");
            CommonService.sleep(2);
        }
        return null;
    }

}
