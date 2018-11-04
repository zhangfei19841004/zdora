package com.zf.executor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangfei on 2018/10/31.
 */
@Service
public class ExecutorCenter {

    public static List<ExecutorInfo> ALL_EXECUTOR = new ArrayList<>();

    private int threadPoolSize = 10;

    private ExecutorService service = Executors.newFixedThreadPool(threadPoolSize);

    @Async
    public void executorCenter(int caseCount) {
        List<ExecutorHandler> c = new ArrayList<>(caseCount);
        for (int i = 0; i < caseCount; i++) {
            ExecutorHandler eh = new ExecutorHandler();
            c.add(eh);
        }
        try {
            service.invokeAll(c);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
